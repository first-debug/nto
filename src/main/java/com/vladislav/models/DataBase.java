package com.vladislav.models;

import com.vladislav.App;

import java.sql.*;
import java.util.ArrayList;

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
            App.logger.error(20  + " " + ex.getMessage());
        }
    }

    public void closeConnection() {
        try {
			if (connection != null) {
                connection.close();
            }
		} catch (SQLException ex) {
            App.logger.error(30  + " " + ex.getMessage());
		}
    }

    public static Event getEvent(Integer id) {
        Event result = null;
        try {
            PreparedStatement pStatement = connection.prepareStatement("SELECT * FROM events WHERE id=?");
            pStatement.setInt(1, id);
            ResultSet answer = pStatement.executeQuery();
            String title = answer.getString("title");
            String description = answer.getString("description");
            Space space = getSpace(answer.getInt("spaceId"));
            Long timeToStart = answer.getLong("timeToStart");
            String typeName = answer.getString("type");
            EventType eventType = getEventType(typeName);
            result = new Event((int) (Math.random() * 1000), title, description, space, timeToStart, eventType);
        } catch (SQLException ex) {
            App.logger.error(48  + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Event> getEduEvents() {
        ArrayList<Event> result = null;
        try
        {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT title, description, spaceId, timeToStart, type FROM events WHERE isEntertainment=?");
            pStatement.setBoolean(1, false);
            ResultSet answer = pStatement.executeQuery();
            result = getEventsList(answer);
        } catch (SQLException ex) {
            App.logger.error(63  + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Event> getEntEvents() {
        ArrayList<Event> result = null;
        try
        {
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT title, description, spaceId, timeToStart, type FROM events WHERE isEntertainment=?");
            pStatement.setBoolean(1, true);
            ResultSet answer = pStatement.executeQuery();
            result = getEventsList(answer);
        } catch (SQLException ex) {
            App.logger.error(78  + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Event> getEventsList(ResultSet answer) {
        if (answer == null) {
            try {
                Statement statement = connection.createStatement();
                String request = "SELECT title, description, spaceId, timeToStart, type FROM events";
                answer = statement.executeQuery(request);
            } catch (SQLException ex) {
                App.logger.error(90  + " " + ex.getMessage());
            }
        }

        ArrayList<Event> result = new ArrayList<>();
        try {
            while (answer != null && answer.next()) {
                String title = answer.getString("title");
                String description = answer.getString("description");
                Space space = getSpace(answer.getInt("spaceId"));
                Long timeToStart = answer.getLong("timeToStart");
                String typeName = answer.getString("type");
                EventType eventType = getEventType(typeName);

                result.add(new Event((int) (Math.random() * 1000), title, description, space, timeToStart, eventType));
            }
        } catch (SQLException ex) {
            App.logger.error(107 + " " + ex.getMessage());
        }
        return result;
    }

    public static void addEvent(String title, String description, Integer spaceId, long timeToStart, EventType type) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT INTO events (title, description, spaceID, timeToStart, type, isEntertainment) VALUES (?, ?, ?, ?, ?, ?)"
            );
            pStatement.setString(1, title);
            pStatement.setString(2, description);
            pStatement.setInt(3, spaceId);
            pStatement.setLong(4, timeToStart);
            pStatement.setString(5, type.getName());
            pStatement.setBoolean(6, type.getIsEntertainment());
            pStatement.execute();
        } catch (SQLException ex) {
            App.logger.error(124  + " " + ex.getMessage());
        }
    }

    private static EventType getEventType(String typeName) {
        EventType result = null;
        try {
            PreparedStatement pStatement = connection.prepareStatement("SELECT * FROM typesOfEvents WHERE type=?");
            pStatement.setString(1, typeName);
            ResultSet answer = pStatement.executeQuery();
            String type = answer.getString("type");
            boolean isEntertainment = answer.getBoolean("isEntertainment");
            result = new EventType(type, isEntertainment);
        } catch (SQLException ex) {
            App.logger.error(138  + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<EventType> getTypesList() {
        ArrayList<EventType> result = new ArrayList<>();
        try
        {
            Statement statement = connection.createStatement();
            ResultSet answer = statement.executeQuery(
                    "SELECT * FROM typesOfEvents"
            );
            while (answer.next())
            {
                String name = answer.getString("type");
                boolean isEntertainment = answer.getBoolean("isEntertainment");
                result.add(new EventType(name, isEntertainment));
            }
        } catch (SQLException ex) {
            App.logger.error(158  + " " + ex.getMessage());
        }
        return result;
    }

    public static void addEventType(EventType eventType) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT INTO typesOfEvents (type, isEntertainment) VALUES (?, ?)"
            );
            pStatement.setString(1, eventType.getName());
            pStatement.setBoolean(2, eventType.getIsEntertainment());
            pStatement.execute();
        } catch (SQLException ex) {
            App.logger.error(172  + " " + ex.getMessage());
        }
    }

    public static void removeEventType(EventType eventType) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "DELETE FROM typesOfEvents WHERE type=?"
            );
            pStatement.setString(1, eventType.getName());
            pStatement.execute();
        } catch (SQLException ex) {
            App.logger.error(184  + " " + ex.getMessage());
        }
    }

    public static Space getSpace(String name) {
        Space result = null;
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM spaces WHERE name=" + name);
            ResultSet answer = statement.getResultSet();
            Integer id = answer.getInt("id");
            String description = answer.getString("description");
            Integer area = answer.getInt("area");
            Integer capacity = answer.getInt("capacity");
            result = new Space(id, name, description, area, capacity);
        } catch (SQLException ex) {
            App.logger.error(200  + " " + ex.getMessage());
        }
        return result;
    }

    public static Space getSpace(Integer id) {
        Space result = null;
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM spaces WHERE id=" + id);
            ResultSet answer = statement.getResultSet();
            String name = answer.getString("name");
            String description = answer.getString("description");
            Integer area = answer.getInt("area");
            Integer capacity = answer.getInt("capacity");
            result = new Space(id, name, description, area, capacity);
        } catch (SQLException ex) {
            App.logger.error(200  + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Space> getSpacesList() {
        ArrayList<Space> result = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM spaces");
            ResultSet answer = statement.getResultSet();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                String name = answer.getString("name");
                String description = answer.getString("description");
                Integer area = answer.getInt("area");
                Integer capacity = answer.getInt("capacity");
                result.add(new Space(id, name, description, area, capacity));
            }
        } catch (SQLException ex) {
            App.logger.error(200  + " " + ex.getMessage());
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
            App.logger.error(214  + " " + ex.getMessage());
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
            App.logger.error(228 + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<TaskType> getTaskTypeList() {
        ArrayList<TaskType> result = new ArrayList<>();
        try {
            PreparedStatement pStatement = connection.prepareStatement("SELECT * FROM typesOfTasks");
            ResultSet answer = pStatement.executeQuery();
            while (answer.next()) {
                Integer id = answer.getInt("id");
                String name = answer.getString("type");
                String description = answer.getString("description");
                result.add(new TaskType(id, name, description));
            }
        } catch (SQLException ex) {
            App.logger.error(242 + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Task> getTasksList(boolean isExecuted) {
        ArrayList<Task> result = new ArrayList<>();
        try
        {
            Statement st = connection.createStatement();
            ResultSet answer = st.executeQuery(
                    "SELECT * FROM tasks"
            );
            while (answer.next())
            {
                String statusName = answer.getString("status");
                Status status = null;
                switch (statusName) {
                    case "Создана": status = Status.CREATED; break;
                    case "К выполнению": status = Status.EXECUTED; break;
                    case "Выполнена": status = Status.COMPLETED; break;
                }
                assert status != null;
                if (isExecuted && !status.equals(Status.EXECUTED)) {
                   continue;
                }
                Integer id = answer.getInt("id");
                Long timeOfReg = answer.getLong("timeOfReg");
                Integer eventId = answer.getInt("eventId");
                Event event = getEvent(eventId);
                Integer typeId = answer.getInt("typeId");
                TaskType type = getTaskType(typeId);
                Long deadline = answer.getLong("deadline");
                result.add(new Task(id, timeOfReg, event, type, deadline, status));
            }
        } catch (SQLException ex) {
            App.logger.error(266  + " " + ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Task> getTasksList(boolean isExecuted, String name) {
        ArrayList<Task> result = new ArrayList<>();
        try
        {
            Statement st = connection.createStatement();
            ResultSet answer = st.executeQuery(
                    "SELECT * FROM tasks"
            );
            while (answer.next())
            {
                String statusName = answer.getString("status");
                Status status = null;
                switch (statusName) {
                    case "Создана": status = Status.CREATED; break;
                    case "К выполнению": status = Status.EXECUTED; break;
                    case "Выполнена": status = Status.COMPLETED; break;
                }
                assert status != null;
                if (isExecuted && !status.equals(Status.EXECUTED)) {
                    continue;
                }
                Integer typeId = answer.getInt("typeId");
                TaskType type = getTaskType(typeId);
                if (type.getName().equals(name)) {
                    Integer id = answer.getInt("id");
                    Long timeOfReg = answer.getLong("timeOfReg");
                    Integer eventId = answer.getInt("eventId");
                    Event event = getEvent(eventId);
                    Long deadline = answer.getLong("deadline");
                    result.add(new Task(id, timeOfReg, event, type, deadline, status));
                }
            }
        } catch (SQLException ex) {
            App.logger.error(266  + " " + ex.getMessage());
        }
        if (result.size() == 0) return new ArrayList<>();
        else return result;
    }

    public static void addTask(Task task) {
        try {
            PreparedStatement pStatement = connection.prepareStatement(
                    "INSERT INTO tasks (timeOfReg, typeId, eventId, spaceId, deadline, status) VALUES (?, ?, ?, ?, ?, ?)"
            );
            pStatement.setLong(1, task.getTimeOfReg());
            pStatement.setInt(2, task.getId());
            pStatement.setInt(3, task.getEvent().getId());
            pStatement.setInt(4, task.getSpace().getId());
            pStatement.setLong(5, task.getDeadline());
            pStatement.setString(6, task.getStatusName());
            pStatement.execute();
        } catch (SQLException ex) {
            App.logger.error(172  + " " + ex.getMessage());
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
            App.logger.error(287 + " " + ex.getMessage());
        }
        return result;
    }
}

