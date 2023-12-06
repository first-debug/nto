package com.vladislav.models;


import java.sql.*;
import java.util.ArrayList;

import static com.vladislav.App.logger;

public class DataBase {
    private static Connection connection = null;

    public DataBase() {
        try
        {
            // костыль для запуска с помощью .bat
            // для работы костыля "jdbc:sqlite:db.db"
            connection = DriverManager.getConnection("jdbc:sqlite:db.db");
            connection.setAutoCommit(true);
        }
        catch (SQLException ex)
        {
            logger.error(20  + " " + ex.getMessage());
        }
    }

    public void closeConnection() {
        try {
			if (connection != null) {
                connection.close();
            }
		} catch (SQLException ex) {
            logger.error(30  + " " + ex.getMessage());
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
            EventType type = EventType.getInstant(typeId, typeName, typeIsEntertainment);
            return Event.getInstant(eventId, title, eventDescription, space, timeToStart, type);
        } catch (SQLException ex) {
            logger.error(54 + " " + ex.getMessage());
            return null;
        }
    }

    private static Space parseSpace(ResultSet answer) {
        try {
            Integer spaceId = answer.getInt("id");
            String spaceName = answer.getString("space");
            String spaceDescription = answer.getString("description");
            Integer area = answer.getInt("area");
            Integer capacity = answer.getInt("capacity");
            Boolean hasSeveralParts = answer.getBoolean("hasSeveralParts");
            int firstPartArea = answer.getInt("firstPartArea");
            int secondPartArea = answer.getInt("secondPartArea");
            return Space.getInstant(spaceId, spaceName, spaceDescription, area, capacity, hasSeveralParts,
                    new Integer[]{firstPartArea, secondPartArea});
        } catch (SQLException ex) {
            logger.error(72 + " " + ex.getMessage());
            return null;
        }
    }

    public static void addEvent(String title, String description, Space space, long timeToStart, EventType type) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE events(title, description, spaceId, timeToStart, typeId, isEntertainment) " +
                            "VALUES (?, ?, ?, ?, ?, ?)"
            );
            pStatement.setString(1, title);
            pStatement.setString(2, description);
            pStatement.setInt(3, space.getId());
            pStatement.setLong(4, timeToStart);
            pStatement.setInt(5, type.getId());
            pStatement.setBoolean(6, type.getIsEntertainment());
            pStatement.execute();
            loadEvents(type.getIsEntertainment());
        } catch (SQLException ex) {
            logger.error(92  + " " + ex.getMessage());
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
            logger.error(104  + " " + ex.getMessage());
        }
    }

    public static void loadEvents(Boolean isEntertainment) {
        String sql = "SELECT e.id as eventId, e.title as eventTitle, e.description as eventDescription, e.timeToStart as eventTimeToStart, " +
                "e.timeToStart as eventTimeToStart, s.id, s.space, s.description, s.area, s.capacity, " +
                "s.hasSeveralParts, s.firstPartArea, s.secondPartArea, te.id as typeId, te.type as typeName, " +
                "te.isEntertainment " +
                "FROM events as e, spaces as s, typesOfEvents as te " +
                "WHERE s.id = e.spaceId AND te.id = e.typeId AND e.id IS NOT NUll";
        try
        {
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
            logger.error(129  + " " + ex.getMessage());
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
            getTypesEventList();
        } catch (SQLException ex) {
            logger.error(172  + " " + ex.getMessage());
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
            logger.error(184  + " " + ex.getMessage());
        }
    }

    public static void getTypesEventList() {
        try
        {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM typesOfEvents"
            );
            ResultSet answer = statement.executeQuery();
            while (answer.next())
            {
                Integer id = answer.getInt("id");
                String name = answer.getString("type");
                boolean isEntertainment = answer.getBoolean("isEntertainment");
                EventType.getInstant(id, name, isEntertainment);
            }
        } catch (SQLException ex) {
            logger.error(158  + " " + ex.getMessage());
        }
    }

    public static void addSpace(String space, String description, Integer area,
                                Integer capacity, Boolean hasSeveralParts, Integer[] partsArea) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO spaces(space, description, area, capacity, hasSeveralParts," +
                            " firstPartArea, secondPartArea) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            pStatement.setString(1, space);
            pStatement.setString(2, description);
            pStatement.setInt(3, area);
            pStatement.setInt(4, capacity);
            pStatement.setInt(5, hasSeveralParts ? 1 : 0);
            pStatement.setInt(6, partsArea[0]);
            pStatement.setInt(7, partsArea[1]);
            pStatement.execute();
            getSpacesList();
        } catch (SQLException ex) {
            logger.error(194  + " " + ex.getMessage());
        }
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
            logger.error(206  + " " + ex.getMessage());
        }
    }

    public static void getSpacesList() {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT * FROM spaces"
            );
            pStatement.execute();
            ResultSet answer = pStatement.getResultSet();
            while (answer.next()) {
                parseSpace(answer);
            }
        } catch (SQLException ex) {
            logger.error(200  + " " + ex.getMessage());
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
            loadTypeTaskList();
        } catch (SQLException ex) {
            logger.error(288  + " " + ex.getMessage());
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
            logger.error(291  + " " + ex.getMessage());
        }
    }

    public static void loadTypeTaskList() {
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
            logger.error(265 + " " + ex.getMessage());
        }
    }

    public static void addTask(Long timeOfReg, TaskType type, Event event,
                               Long deadline, Status status) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO tasks(timeOfReg, typeId, eventId, spaceId, deadline, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?)"
            );
            pStatement.setLong(1, timeOfReg);
            pStatement.setInt(2, type.getId());
            pStatement.setInt(3, event.getId());
            pStatement.setInt(4, event.getSpace().getId());
            pStatement.setLong(5, deadline);
            pStatement.setString(6, status.getName());
            pStatement.execute();
            getTasksList(status.getName().equalsIgnoreCase("К выполнению"), type.getName());
        } catch (SQLException ex) {
            logger.error(284  + " " + ex.getMessage());
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
            logger.error(294  + " " + ex.getMessage());
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
            logger.error(214  + " " + ex.getMessage());
        }
    }

    public static void getTasksList(boolean isExecuted, String typeName) {
        String sql = "SELECT t.id as taskId, t.timeOfReg as taskReg, t.deadline, t.status, " +
                "       tt.id as taskTypeId, tt.type as taskTypeName, tt.description as taskTypeDescription, " +
                "       e.id as eventId, e.title as eventTitle, e.description as eventDescription, e.timeToStart as eventTimeToStart, " +
                "       s.id, s.space, s.description, s.area, s.capacity, s.hasSeveralParts, s.firstPartArea, s.secondPartArea, " +
                "       te.id as typeId, te.type as typeName, te.isEntertainment " +
                "FROM tasks as t, typesOfTasks as tt, " +
                "     events as e, spaces as s, typesOfEvents as te " +
                "WHERE t.typeId = tt.id AND t.eventId = e.id AND t.spaceId = s.id " +
                "  AND e.spaceId = s.id AND e.typeId = te.id";
        try
        {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            ResultSet answer = pStatement.executeQuery();
            while (answer.next())
            {
                Status status = null;
                switch (answer.getString("status")) {
                    case "Создана": status = Status.CREATED; break;
                    case "К выполнению": status = Status.EXECUTED; break;
                    case "Выполнена": status = Status.COMPLETED; break;
                }
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
                Task.getInstant(taskId, timeOfReg, event, type, deadline, status);

            }
        } catch (SQLException ex) {
            logger.error(266  + " " + ex.getMessage());
        }
    }

    public static void addBooking(Long timeOfReg, Event event, Long timeOfStart,
                                  Long timeOfEnd, Space space, Integer halfOfSpace, String comment) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT OR REPLACE INTO booking(timeOfReg, eventId, timeOfStart, timeOfEnd, spaceId, " +
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
            getBookingList(space);
        } catch (SQLException ex) {
            logger.error(284  + " " + ex.getMessage());
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
            logger.error(394  + " " + ex.getMessage());
        }
    }

    // для Места
    public static void getBookingList(Space space) {
        String sql = "SELECT b.id as bookingId, b.timeOfReg as bookingReg, b.timeOfStart as bookingStart, b.timeOfEnd, b.halfOfSpace, b.comment, " +
                "e.id as eventId, e.title as eventTitle, e.description as eventDescription, e.timeToStart as eventTimeToStart, " +
                "s.id, s.space, s.description, s.area, s.capacity, s.hasSeveralParts, s.firstPartArea, s.secondPartArea, " +
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

                    Booking.getInstant(id, timeOfReg, event, timeOfStart, timeOfEnd, space, halfOfSpace, comment);
                }
            }
        } catch (SQLException ex) {
            logger.error(447 + " " + ex.getMessage());
        }
    }

    // для Мероприятия
    public static ArrayList<Booking> getBookingList(Space space, Event event) {
        ArrayList<Booking> result = new ArrayList<>();
        String sql = "SELECT b.id, b.timeOfReg, b.timeOfStart, b.timeOfEnd, b.halfOfSpace, b.comment, " +
                "FROM booking as b, events as e, typesOfEvents as te " +
                "WHERE b.eventId = ? AND b.spaceId = e.spaceId = ? AND e.typeId = te.id";
        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1, event.getId());
            pStatement.setInt(2, space.getId());
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                Long timeOfReg = answer.getLong("timeOfReg");
                Long timeOfStart = answer.getLong("timeOfStart");
                Long timeOfEnd = answer.getLong("timeOfEnd");
                Integer halfOfSpace = answer.getInt("halfOfSpace");
                String comment = answer.getString("comment");

                result.add(Booking.getInstant(id, timeOfReg, event, timeOfStart, timeOfEnd, space, halfOfSpace, comment));
            }
        } catch (SQLException ex) {
            logger.error(474 + " " + ex.getMessage());
        }
        return result;
    }

    public static String[] loginEmployee(String login, String password) {
        String[] result = new String[3];
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT login FROM employees WHERE login=?");
            pStatement.setString(1, login);
            ResultSet answerLogin = pStatement.executeQuery();
            result[0] = String.valueOf(answerLogin.next());

            pStatement = connection.prepareStatement(
                    "SELECT password, role FROM employees WHERE password=?");
            pStatement.setString(1, password);
            ResultSet answer = pStatement.executeQuery();
            result[1] = String.valueOf(answer.next());
            result[2] = answer.getString("role");
        } catch (SQLException ex) {
            logger.error(287 + " " + ex.getMessage());
        }
        return result;
    }
}