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
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/com/vladislav/db.db");
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
        Event result = null;
        try {
            Integer eventId = answer.getInt("id");
            String title = answer.getString("title");
            String eventDescription = answer.getString("description");
            Long timeToStart = answer.getLong("timeToStart");

            if (space == null) {
                space = parseSpace(answer);
            }

            Integer typeId = answer.getInt("id");
            String typeName = answer.getString("type");
            boolean typeIsEntertainment = answer.getBoolean("isEntertainment");
            EventType type = new EventType(typeId, typeName, typeIsEntertainment);
            result = new Event(eventId, title, eventDescription, space, timeToStart, type);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    private static Space parseSpace(ResultSet answer) {
        Space result = null;
        try {
            Integer spaceId = answer.getInt("id");
            String spaceName = answer.getString("space");
            String spaceDescription = answer.getString("description");
            Integer area = answer.getInt("area");
            Integer capacity = answer.getInt("capacity");
            int firstPartArea = answer.getInt("firstPartArea");
            int secondPartArea = answer.getInt("secondPartArea");
            Boolean hasSeveralParts = answer.getBoolean("hasSeveralParts");
            result = new Space(spaceId, spaceName, spaceDescription, area, capacity, hasSeveralParts, new Integer[]{firstPartArea, secondPartArea});
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public static Event getEvent(Integer id) {
        Event result = null;
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT title, e.description, s.space, timeToStart, te.type\n" +
                    "FROM events as e, spaces as s, typesOfEvents as te\n" +
                    "WHERE s.id = e.spaceId AND te.id = e.typeId " +
                    "AND e.id = ?");
            pStatement.setInt(1, id);
            ResultSet answer = pStatement.executeQuery();
            String title = answer.getString("title");
            String description = answer.getString("description");
            Space space = getSpace(answer.getInt("space"));
            Long timeToStart = answer.getLong("timeToStart");
            EventType type = getEventType(answer.getInt("type"));
            result = new Event(id, title, description, space, timeToStart, type);
        } catch (SQLException ex) {
            logger.error(48  + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Event> getEventsList(Boolean isEntertainment) {
        ArrayList<Event> result = new ArrayList<>();
        String sql = "SELECT e.id, title, e.description, timeToStart, " +
                "s.id, s.space, s.description, s.area, s.capacity, s.hasSeveralParts, s.firstPartArea, s.secondPartArea, " +
                "te.id, te.type, te.isEntertainment " +
                "FROM events as e, spaces as s, typesOfEvents as te " +
                "WHERE s.id = e.spaceId AND te.id = e.typeId ";
        try
        {
            PreparedStatement pStatement;
            if (isEntertainment == null) {
                pStatement = connection.prepareStatement(sql);
            } else {
                pStatement = connection.prepareStatement(sql + "AND te.isEntertainment = ?");
                pStatement.setInt(1, isEntertainment ? 1 : 0);
            }
            ResultSet answer = pStatement.executeQuery();
            while (answer != null && answer.next()) {
                result.add(parseEvent(answer, null));
            }
        } catch (SQLException ex) {
            logger.error(95  + " " + ex.getMessage());
        }
        return result;
    }

    public static void addEvent(String title, String description, Integer spaceId, long timeToStart, EventType type) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT INTO events (title, description, spaceId, timeToStart, typeId, isEntertainment) VALUES (?, ?, ?, ?, ?, ?)"
            );
            pStatement.setString(1, title);
            pStatement.setString(2, description);
            pStatement.setInt(3, spaceId);
            pStatement.setLong(4, timeToStart);
            pStatement.setInt(5, type.getId());
            pStatement.setBoolean(6, type.getIsEntertainment());
            pStatement.execute();
        } catch (SQLException ex) {
            logger.error(124  + " " + ex.getMessage());
        }
    }

    private static EventType getEventType(Integer id) {
        EventType result = null;
        try {
            PreparedStatement pStatement = connection.prepareStatement("SELECT * FROM typesOfEvents WHERE id=?");
            pStatement.setInt(1, id);
            ResultSet answer = pStatement.executeQuery();
            String type = answer.getString("type");
            boolean isEntertainment = answer.getBoolean("isEntertainment");
            result = new EventType(id, type, isEntertainment);
        } catch (SQLException ex) {
            logger.error(138  + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<EventType> getTypesEventList() {
        ArrayList<EventType> result = new ArrayList<>();
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
                result.add(new EventType(id, name, isEntertainment));
            }
        } catch (SQLException ex) {
            logger.error(158  + " " + ex.getMessage());
        }
        return result;
    }

    public static void addEventType(String name, Boolean isEntertainment) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT INTO typesOfEvents (type, isEntertainment) VALUES (?, ?)"
            );
            pStatement.setString(1, name);
            pStatement.setBoolean(2, isEntertainment);
            pStatement.execute();
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
        } catch (SQLException ex) {
            logger.error(184  + " " + ex.getMessage());
        }
    }

    public static Space getSpace(Integer id) {
        Space result = null;
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT * FROM spaces WHERE id = ?"
            );
            pStatement.setInt(1, id);
            pStatement.execute();
            ResultSet answer = pStatement.getResultSet();
            result = parseSpace(answer);
        } catch (SQLException ex) {
            logger.error(190  + " " + ex.getMessage());
        }
        return result;
    }

    public static Space getSpace(String space) {
        Space result = null;
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT * FROM spaces WHERE space = ?");
            pStatement.setString(1, space);
            pStatement.execute();
            ResultSet answer = pStatement.getResultSet();
            result = parseSpace(answer);
        } catch (SQLException ex) {
            logger.error(207  + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Space> getSpacesList() {
        ArrayList<Space> result = new ArrayList<>();
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT * FROM spaces"
            );
            pStatement.execute();
            ResultSet answer = pStatement.getResultSet();
            while (answer.next()) {
                result.add(parseSpace(answer));
            }
        } catch (SQLException ex) {
            logger.error(200  + " " + ex.getMessage());
        }
        return result;
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

    public static TaskType getTaskType(Integer id) {
        TaskType result = null;
        try {
            PreparedStatement pStatement = connection.prepareStatement("SELECT * FROM typesOfTasks WHERE id=?");
            pStatement.setInt(1, id);
            ResultSet answer = pStatement.executeQuery();
            String name = answer.getString("type");
            String description = answer.getString("description");
            result = new TaskType(id, name, description);
        } catch (SQLException ex) {
            logger.error(228 + " " + ex.getMessage());
        }
        return result;
    }

    // ИЗМЕНИТЬ
    public static ArrayList<Task> getTasksList(boolean isExecuted, String name) {
        ArrayList<Task> result = new ArrayList<>();
        String sql = "SELECT t.id, t.timeOfReg, t.deadline, t.status, " +
                "tt.id, tt.type, tt.description, " +
                "e.id, e.title, e.description, e.timeToStart, " +
                "s.id, s.space, s.description, s.area, s.capacity, s.hasSeveralParts, s.firstPartArea, s.secondPartArea, " +
                "te.id, te.type, te.isEntertainment " +
                "FROM tasks as t, typesOfTasks as tt, " +
                "events as e, spaces as s, typesOfEvents as te " +
                "WHERE t.typeId = tt.id AND t.eventId = e.id AND t.spaceId = s.id " +
                "AND e.spaceId = s.id AND e.typeId = te.id";
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
                Integer taskId = answer.getInt("id");
                Long timeOfReg = answer.getLong("timeOfReg");
                Long deadline = answer.getLong("deadline");

                Integer tTypeId = answer.getInt("id");
                String tTypeName = answer.getString("type");
                String tTypeDescription = answer.getString("description");
                TaskType type = new TaskType(tTypeId, tTypeName, tTypeDescription);
                if (name != null && !type.getName().equals(name)) {
                    continue;
                }
                Event event = parseEvent(answer, null);

                result.add(new Task(taskId, timeOfReg, event, type, deadline, status));

            }
        } catch (SQLException ex) {
            logger.error(266  + " " + ex.getMessage());
        }
        if (result.size() == 0) return new ArrayList<>();
        else return result;
    }

    public static ArrayList<TaskType> getTypeTaskList() {
        ArrayList<TaskType> result = new ArrayList<>();
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT * FROM typesOfTasks"
            );
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                String name = answer.getString("type");
                String description = answer.getString("description");
                result.add(new TaskType(id, name, description));
            }
        } catch (SQLException ex) {
            logger.error(242 + " " + ex.getMessage());
        }
        return result;
    }

    public static void addTask(Long timeOfReg, TaskType type, Event event,
                               Long deadline, Status status) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT INTO tasks (timeOfReg, typeId, eventId, spaceId, deadline, status) VALUES (?, ?, ?, ?, ?, ?)"
            );
            pStatement.setLong(1, timeOfReg);
            pStatement.setInt(2, type.getId());
            pStatement.setInt(3, event.getId());
            pStatement.setInt(4, event.getSpace().getId());
            pStatement.setLong(5, deadline);
            pStatement.setString(6, status.getName());
            pStatement.execute();
        } catch (SQLException ex) {
            logger.error(172  + " " + ex.getMessage());
        }
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

    public static ArrayList<Booking> getBookingList() {
        ArrayList<Booking> result = new ArrayList<>();
        String sql = "SELECT b.id, b.timeOfReg, b.timeOfStart, b.timeOfEnd, b.halfOfSpace, b.comment, " +
                "e.id, e.title, e.description, e.timeToStart, " +
                "s.id, s.space, s.description, s.area, s.capacity, s.hasSeveralParts, s.firstPartArea, s.secondPartArea, " +
                "te.id, te.type, te.isEntertainment " +
                "FROM booking as b, events as e, typesOfEvents as te, spaces as s " +
                "WHERE b.eventId = e.id AND b.spaceId = s.id AND e.spaceId = s.id " +
                "AND e.typeId = te.id";
        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                Long timeOfReg = answer.getLong("timeOfReg");
                Long timeOfStart = answer.getLong("timeOfStart");
                Long timeOfEnd = answer.getLong("timeOfEnd");
                Integer halfOfSpace = answer.getInt("halfOfSpace");
                String comment = answer.getString("comment");

                Event event = parseEvent(answer, null);
                Space space = event.getSpace();

                result.add(new Booking(id, timeOfReg, event, timeOfStart, timeOfEnd, space, halfOfSpace, comment));
            }
        } catch (SQLException ex) {
            logger.error(318 + " " + ex.getMessage());
        }
        return result;
    }

    // для Места
    public static ArrayList<Booking> getBookingList(Space space) {
        ArrayList<Booking> result = new ArrayList<>();
        String sql = "SELECT b.id, b.timeOfReg, b.timeOfStart, b.timeOfEnd, b.halfOfSpace, b.comment, " +
                "e.id, e.title, e.description, e.timeToStart, " +
                "te.id, te.type, te.isEntertainment " +
                "FROM booking as b, events as e, typesOfEvents as te " +
                "WHERE b.eventId = e.id AND b.spaceId = e.spaceId = ? AND e.typeId = te.id";
        try {
            PreparedStatement pStatement = connection.prepareStatement(sql);
            pStatement.setInt(1, space.getId());
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                Long timeOfReg = answer.getLong("timeOfReg");
                Long timeOfStart = answer.getLong("timeOfStart");
                Long timeOfEnd = answer.getLong("timeOfEnd");
                Integer halfOfSpace = answer.getInt("halfOfSpace");
                String comment = answer.getString("comment");

                Event event = parseEvent(answer, space);

                result.add(new Booking(id, timeOfReg, event, timeOfStart, timeOfEnd, space, halfOfSpace, comment));
            }
        } catch (SQLException ex) {
            logger.error(318 + " " + ex.getMessage());
        }
        return result;
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

                result.add(new Booking(id, timeOfReg, event, timeOfStart, timeOfEnd, space, halfOfSpace, comment));
            }
        } catch (SQLException ex) {
            logger.error(318 + " " + ex.getMessage());
        }
        return result;
    }
}