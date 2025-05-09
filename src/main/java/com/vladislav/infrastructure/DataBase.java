package com.vladislav.infrastructure;

import com.vladislav.domain.*;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashSet;


@Repository
public class DataBase {
    private static Connection connection = null;
    private static final Logger logger = LoggerFactory.getLogger(DataBase.class);

    public DataBase(@Value("${spring.datasource.url}") String url) {
        try {
            try {
                connection = DriverManager.getConnection(url);
                connection.isValid(5);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    private static Event parseEvent(ResultSet answer, Space space) {
        try {
            Integer eventId = answer.getInt("eventId");
            String title = answer.getString("eventTitle");
            String eventDescription = answer.getString("eventDescription");
            Long timeToStart = answer.getLong("eventTimeToStart");

            if (space == null) {
                space = parseSpace(answer);
            }

            Integer typeId = answer.getInt("typeId");
            String typeName = answer.getString("typeName");
            boolean typeIsEntertainment = answer.getBoolean("isEntertainment");
            EventType type = EventType.getInstance(typeId, typeName, typeIsEntertainment);
            return Event.getInstance(eventId, title, eventDescription, space, timeToStart, type);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }

    private static Space parseSpace(ResultSet answer) {
        try {
            Integer spaceId = answer.getInt("id");
            String spaceName = answer.getString("space");
            String spaceDescription = answer.getString("description");
            String type = answer.getString("type");
            Integer area = answer.getInt("area");
            Integer capacity = answer.getInt("capacity");
            Boolean hasSeveralParts = answer.getBoolean("hasSeveralParts");
            int firstPartArea = answer.getInt("firstPartArea");
            int secondPartArea = answer.getInt("secondPartArea");
            String seats = answer.getString("seats");
            return Space.getInstance(spaceId, spaceName, spaceDescription, area, capacity, hasSeveralParts,
                    new Integer[]{firstPartArea, secondPartArea}, type, seats);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static HashSet<Integer> getOccupiedSpaces() {
        HashSet<Integer> occupiedSpaces = new HashSet<>();
        try {
            PreparedStatement pStatement;
            for (String str : new String[] {"events", "lessons"}){
                pStatement = connection.prepareStatement("SELECT spaceId FROM " + str);
                ResultSet answer = pStatement.executeQuery();
                while (answer.next()) {
                    occupiedSpaces.add(answer.getInt("spaceId"));
                }
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        return occupiedSpaces;
    }

    public static void addEvent(String title, String description, Space space, Long timeToStart, EventType type, int[][] seats) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT id FROM events WHERE title = ?"
            );
            pStatement.setString(1, title);
            int answer = pStatement.executeQuery().getInt(1);
            String statement;
            if (answer == 0)
                statement = "INSERT INTO events(title, description, spaceId, timeToStart, typeId, isEntertainment, seats)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            else
                statement = "UPDATE events SET title=?, description=?, spaceId=?, timeToStart=?, typeId=?," +
                        " isEntertainment=?, seats=? WHERE id=?";

            pStatement = connection.prepareStatement(statement);
            pStatement.setString(1, title);
            pStatement.setString(2, description);
            pStatement.setInt(3, space.getId());
            pStatement.setLong(4, timeToStart);
            pStatement.setInt(5, type.getId());
            pStatement.setBoolean(6, type.getIsEntertainment());
            pStatement.setString(7, getSeatsString(seats));
            if (answer != 0)
                pStatement.setInt(8, answer);
            pStatement.execute();
            loadEvents(type.getIsEntertainment());
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void removeEvent(Event event) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM events WHERE id=?"
            );
            pStatement.setInt(1, event.getId());
            pStatement.execute();
            Event.remove(event);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void loadEvents(Boolean isEntertainment) {
        String sql = "SELECT e.id as eventId, e.title as eventTitle, e.description as eventDescription," +
                " e.timeToStart as eventTimeToStart,  e.timeToStart as eventTimeToStart, " +
                "s.id, s.space, s.description, s.area, s.capacity, s.type, s.hasSeveralParts, s.firstPartArea, " +
                "s.seats as seats," +
                "s.secondPartArea, " +
                "te.id as typeId, te.type as typeName,  te.isEntertainment " +
                "FROM events as e, spaces as s, typesOfEvents as te " +
                "WHERE s.id = e.spaceId AND te.id = e.typeId AND e.id IS NOT NUll";
        try {
            PreparedStatement pStatement;
            if (isEntertainment == null) {
                pStatement = connection.prepareStatement(sql);
            } else {
                pStatement = connection.prepareStatement(sql + " AND te.isEntertainment = ?");
                pStatement.setInt(1, isEntertainment ? 1 : 0);
            }
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                parseEvent(answer, null);
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void addEventType(String name, Boolean isEntertainment) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO typesOfEvents(type, isEntertainment) VALUES (?, ?)"
            );
            pStatement.setString(1, name);
            pStatement.setBoolean(2, isEntertainment);
            pStatement.execute();
            loadEventTypesList();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void removeEventType(EventType eventType) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM typesOfEvents WHERE id=?"
            );
            pStatement.setInt(1, eventType.getId());
            pStatement.execute();
            EventType.remove(eventType);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void loadEventTypesList() {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM typesOfEvents"
            );
            ResultSet answer = statement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                String name = answer.getString("type");
                boolean isEntertainment = answer.getBoolean("isEntertainment");
                EventType.getInstance(id, name, isEntertainment);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void addSpace(String space, String description, Integer area,
                                Integer capacity, Boolean hasSeveralParts, Integer[] partsArea,
                                String type, int[][] seats) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT id FROM spaces WHERE space = ?"
            );
            pStatement.setString(1, space);
            int answer = pStatement.executeQuery().getInt(1);
            String statement;
            if (answer == 0)
                statement = "INSERT INTO spaces(space, description, area, capacity, hasSeveralParts," +
                        " firstPartArea, secondPartArea, type, seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            else
                statement = "UPDATE spaces SET space=?, description=?, area=?, capacity=?, hasSeveralParts=?," +
                        " firstPartArea=?, secondPartArea=?, type=?, seats=? WHERE id=?";
            pStatement = connection.prepareStatement(statement);
            pStatement.setString(1, space);
            pStatement.setString(2, description);
            pStatement.setInt(3, area);
            pStatement.setInt(4, capacity);
            pStatement.setInt(5, hasSeveralParts ? 1 : 0);
            pStatement.setInt(6, partsArea[0]);
            pStatement.setInt(7, partsArea[1]);
            pStatement.setString(8, type);
            pStatement.setString(9, getSeatsString(seats));
            if (answer != 0)
                pStatement.setInt(10, answer);
            pStatement.execute();
            loadSpacesList(null);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    private static String getSeatsString(int[][] seats) {
        StringBuilder builder = new StringBuilder();
        for (int[] row : seats) {
            for (int seat : row)
                builder.append(seat);
            builder.append(" ");
        }
        return builder.toString().strip();
    }

    public static void removeSpace(Space space) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM spaces WHERE id=?"
            );
            pStatement.setInt(1, space.getId());
            pStatement.execute();
            Space.remove(space);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void loadSpacesList(String type) {
        String sql = "SELECT * FROM spaces";
        try {
            PreparedStatement pStatement;
            if (type == null) {
                pStatement = connection.prepareStatement(sql);
            } else {
                pStatement = connection.prepareStatement(sql + " WHERE type = ?");
                pStatement.setString(1, type);
            }
            pStatement.execute();
            ResultSet answer = pStatement.getResultSet();
            while (answer.next()) {
                parseSpace(answer);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void addTaskType(String type, String description) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO typesOfTasks(type, description) VALUES (?, ?)"
            );
            pStatement.setString(1, type);
            pStatement.setString(2, description);
            pStatement.execute();
            loadTaskTypeList();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void removeTaskType(TaskType taskType) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM typesOfTasks WHERE id=?"
            );
            pStatement.setInt(1, taskType.getId());
            pStatement.execute();
            TaskType.remove(taskType);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void loadTaskTypeList() {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT * FROM typesOfTasks"
            );
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                String name = answer.getString("type");
                String description = answer.getString("description");
                TaskType.getInstance(id, name, description);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void addTask(Long timeOfReg, TaskType type, Event event,
                               Long deadline, Status status) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO tasks(time_reg, typeId, eventId, spaceId, deadline, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?)"
            );
            pStatement.setLong(1, timeOfReg);
            pStatement.setInt(2, type.getId());
            pStatement.setInt(3, event.getId());
            pStatement.setInt(4, event.getSpace().getId());
            pStatement.setLong(5, deadline);
            pStatement.setString(6, status.getName());
            pStatement.execute();
            loadTasksList(status.getName().equalsIgnoreCase("К выполнению"), type.getName());
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void removeTask(Task task) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM tasks WHERE id=?"
            );
            pStatement.setInt(1, task.getId());
            pStatement.execute();
            Task.remove(task);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void changeTaskStatus(Task task, Status newStatus) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "UPDATE tasks SET status = ? WHERE id = ?"
            );
            pStatement.setString(1, newStatus.getName());
            pStatement.setInt(2, task.getId());
            pStatement.execute();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void loadTasksList(boolean isExecuted, String typeName) {
        String sql = "SELECT t.id as taskId, t.time_reg as taskReg, t.deadline, t.status, " +
                "tt.id as taskTypeId, tt.type as taskTypeName, tt.description as taskTypeDescription, " +
                "e.id as eventId, e.title as eventTitle, e.description as eventDescription, " +
                "e.timeToStart as eventTimeToStart, " +
                "s.id, s.space, s.description, s.area, s.capacity, s.hasSeveralParts, s.firstPartArea," +
                "s.seats as seats," +
                " s.secondPartArea, s.type, " +
                "te.id as typeId, te.type as typeName, te.isEntertainment " +
                "FROM tasks as t, typesOfTasks as tt, " +
                "     events as e, spaces as s, typesOfEvents as te " +
                "WHERE t.typeId = tt.id AND t.eventId = e.id AND t.spaceId = s.id " +
                "  AND e.spaceId = s.id AND e.typeId = te.id";
        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Status status =  switch (answer.getString("status")) {
                    case "Создана" -> Status.CREATED;
                    case "К выполнению" -> Status.EXECUTED;
                    case "Выполнена" -> Status.COMPLETED;
                    default -> null;
                };
                assert status != null;
                if (isExecuted && !status.equals(Status.EXECUTED)) {
                    continue;
                }
                Integer taskId = answer.getInt("taskId");
                Long timeOfReg = answer.getLong("taskReg");
                Long deadline = answer.getLong("deadline");

                Integer tTypeId = answer.getInt("taskTypeId");
                String tTypeName = answer.getString("taskTypeName");
                String tTypeDescription = answer.getString("taskTypeDescription");
                TaskType type = TaskType.getInstance(tTypeId, tTypeName, tTypeDescription);
                if (typeName != null && !type.getName().equals(typeName)) {
                    continue;
                }
                Event event = parseEvent(answer, null);
                Task.getInstance(taskId, timeOfReg, event, type, deadline, status);

            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void addBooking(Long timeOfReg, Event event, Long timeOfStart,
                                  Long timeOfEnd, Space space, Integer halfOfSpace, String comment) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO booking(time_reg, eventId, timeOfStart, timeOfEnd, spaceId, " +
                            "halfOfSpace, comment) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            pStatement.setLong(1, timeOfReg);
            pStatement.setInt(2, event.getId());
            pStatement.setLong(3, timeOfStart);
            pStatement.setLong(4, timeOfEnd);
            pStatement.setInt(5, space.getId());
            pStatement.setInt(6, halfOfSpace);
            pStatement.setString(7, comment);
            pStatement.execute();
            loadBookingList(space);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void removeBooking(Booking booking) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM typesOfEvents WHERE id=?"
            );
            pStatement.setInt(1, booking.getId());
            pStatement.execute();
            Booking.remove(booking);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    // для Места
    public static void loadBookingList(Space space) {
        String sql = "SELECT b.id as bookingId, b.time_reg as bookingReg, b.timeOfStart as bookingStart, b.timeOfEnd, b.halfOfSpace, b.comment, " +
                "e.id as eventId, e.title as eventTitle, e.description as eventDescription, e.timeToStart as eventTimeToStart, " +
                "s.id, s.space, s.description, s.area, s.capacity, s.hasSeveralParts, s.firstPartArea, s.secondPartArea, s.type, " +
                "s.seats as seats," +
                "te.id as typeId, te.type as typeName, te.isEntertainment " +
                "FROM booking as b, events as e, typesOfEvents as te, spaces as s " +
                "WHERE b.eventId = e.id AND e.spaceId = s.id " +
                "AND e.typeId = te.id AND b.spaceId = s.id ";
        try {
            PreparedStatement pStatement;
            if (space != null) {
                sql += "= ?";
                pStatement = connection.prepareStatement(sql);
                pStatement.setInt(1, space.getId());
            } else pStatement = connection.prepareStatement(sql);
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("bookingId");
                if (!Booking.objectsId.contains(id)) {
                    Long timeOfReg = answer.getLong("bookingReg");
                    Long timeOfStart = answer.getLong("bookingStart");
                    Long timeOfEnd = answer.getLong("timeOfEnd");
                    Integer halfOfSpace = answer.getInt("halfOfSpace");
                    String comment = answer.getString("comment");

                    space = parseSpace(answer);
                    Event event = parseEvent(answer, space);

                    Booking.getInstance(id, timeOfReg, event, timeOfStart, timeOfEnd, space, halfOfSpace, comment);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static String[] loginEmployee(String login, String password) {
        String[] result = new String[2];
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT login, role FROM employees WHERE login=? AND password=?");
            pStatement.setString(1, login);
            pStatement.setString(2, password);
            ResultSet answerLogin = pStatement.executeQuery();
            result[0] = String.valueOf(answerLogin.next());
            result[1] = String.valueOf(answerLogin.next());
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public static void addLesson(String title, Long start_time, LessonType type, Space lessonSpace,
                                 Teacher teacher, Long[][] schedule) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO lessons(title, start_time, typeId, spaceId, teacherId, " +
                            "monday_start, monday_end, tuesday_start, tuesday_end, wednesday_start, wednesday_end, " +
                            "thursday_start, thursday_end, friday_start, friday_end, saturday_start, saturday_end, " +
                            "sunday_start, sunday_end) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            pStatement.setString(1, title);
            pStatement.setLong(2, start_time);
            pStatement.setInt(3, type.getId());
            pStatement.setInt(4, lessonSpace.getId());
            pStatement.setInt(5, teacher.getId());
            for (int arrI = 0, dbI = 6; arrI < schedule.length; arrI++) {
                pStatement.setLong(dbI++, schedule[arrI][0]);
                pStatement.setLong(dbI++, schedule[arrI][1]);
            }
            pStatement.execute();
            loadLesson();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void removeLesson(Lesson lesson) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM lessons WHERE id=?"
            );
            pStatement.setInt(1, lesson.getId());
            pStatement.execute();
            Lesson.remove(lesson);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void loadLesson() {
        String sql = "SELECT c.id as lessonId, c.title as lessonTitle, c.start_time as lessonStartTime, " +
                "monday_start, monday_end, tuesday_start, tuesday_end, wednesday_start, wednesday_end, " +
                "thursday_start, thursday_end, friday_start, friday_end, saturday_start, saturday_end, sunday_start, " +
                "sunday_end, " +
                "ct.id as typeId, ct.title as typeTitle, ct.description as typeDescription, " +
                "s.id, s.space, s.description, s.area, s.capacity, s.type, s.hasSeveralParts, s.firstPartArea, " +
                "s.seats as seats," +
                "s.secondPartArea, " +
                "e.id as teacherId, e.first_name, e.last_name, e.patronymic " +
                "FROM lessons as c, typesOfLessons as ct, spaces as s , employees as e " +
                "WHERE c.typeId = ct.id AND c.spaceId = s.id AND c.teacherId = e.id";
        try {
            PreparedStatement pStatement;
            pStatement = connection.prepareStatement(sql);
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("lessonId");
                String title = answer.getString("lessonTitle");
                Long timeStart = answer.getLong("lessonStartTime");

                Integer typeId = answer.getInt("typeId");
                String typeTitle = answer.getString("typeTitle");
                String description = answer.getString("typeDescription");
                LessonType type = LessonType.getInstance(typeId, typeTitle, description);

                Space space = parseSpace(answer);

                Integer teacherId = answer.getInt("teacherId");
                String firstName = answer.getString("first_name");
                String lastName = answer.getString("last_name");
                String patronymic = answer.getString("patronymic");
                Teacher teacher = Teacher.getInstance(teacherId, firstName, lastName, patronymic);
                Long[][] schedule = new Long[][]{
                        {answer.getLong("monday_start"), answer.getLong("monday_end")}, // monday
                        {answer.getLong("tuesday_start"), answer.getLong("tuesday_end")}, // tuesday
                        {answer.getLong("wednesday_start"), answer.getLong("wednesday_end")}, // wednesday
                        {answer.getLong("thursday_start"), answer.getLong("thursday_end")}, // thursday
                        {answer.getLong("friday_start"), answer.getLong("friday_end")}, // friday
                        {answer.getLong("saturday_start"), answer.getLong("saturday_end")}, // saturday
                        {answer.getLong("sunday_start"), answer.getLong("sunday_end")} // sunday
                };
                Lesson.getInstance(id, title, timeStart, type, space, teacher, schedule);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void addLessonType(String title, String description) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO typesOfLessons(title, description) VALUES (?, ?)"
            );
            pStatement.setString(1, title);
            pStatement.setString(2, description);
            pStatement.execute();
            loadLessonTypesList();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void removeLessonType(LessonType lessonType) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM typesOfLessons WHERE id=?"
            );
            pStatement.setInt(1, lessonType.getId());
            pStatement.execute();
            LessonType.remove(lessonType);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void loadLessonTypesList() {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT * FROM typesOfLessons"
            );
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                String title = answer.getString("title");
                String description = answer.getString("description");
                LessonType.getInstance(id, title, description);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void addTeacher(String login, String password, String first_name, String last_name,
                                  String patronymic) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO employees(login, password, first_name, last_name, patronymic, role)" +
                            " VALUES (?, ?, ?, ?, ?, ?)"
            );
            pStatement.setString(1, login);
            pStatement.setString(2, password);
            pStatement.setString(3, first_name);
            pStatement.setString(4, last_name);
            pStatement.setString(5, patronymic);
            pStatement.setString(6, "teacher");
            pStatement.execute();
            loadLessonTypesList();
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void removeTeacher(Teacher teacher) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM employees WHERE id=? AND role='teacher'"
            );
            pStatement.setInt(1, teacher.getId());
            pStatement.execute();
            Teacher.remove(teacher);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    public static void loadEmployeeList(String role) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT * FROM employees WHERE role=?"
            );
            pStatement.setString(1, role);
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                String firstName = answer.getString("first_name");
                String lastName = answer.getString("last_name");
                String patronymic = answer.getString("patronymic");
                Teacher.getInstance(id, firstName, lastName, patronymic);
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }

    @PreDestroy
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
    }
}