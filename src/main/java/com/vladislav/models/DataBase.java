package com.vladislav.models;

import java.lang.invoke.SerializedLambda;
import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private static Connection connection = null;

    public DataBase() {
        try
        {
            // костыль для запуска с помощью .bat
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/com/vladislav/db.db"); // для работы костыля "jdbc:sqlite:db.db"
            connection.setAutoCommit(true);
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getMessage());
        }
    }

    public void closeConnection() {
        try {
			if (connection != null) {
                connection.close();
            }
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
    }

    public static ArrayList<Event> getListEvents(ResultSet answer) {
        if (answer == null) {
            try {
                Statement st = connection.createStatement();
                String request = "SELECT title, description, timeToStart, type FROM events";
                answer = st.executeQuery(request);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        ArrayList<Event> result = new ArrayList<>();
        try {
            while (answer != null && answer.next()) {
                String title = answer.getString("title");
                String description = answer.getString("description");
                Long timeToStart = answer.getLong("timeToStart");
                String type = answer.getString("type");

                result.add(new Event(title, description, timeToStart, type));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Event> getEduEvents() {
        ArrayList<Event> result = null;
        try 
        {
            Statement st = connection.createStatement();
            String request =
                    "SELECT title, description, timeToStart, type FROM events " +
                            "WHERE type='Лекция' OR type='Выставка' OR type='Репетиция'";
            ResultSet answer = st.executeQuery(request);
            result = getListEvents(answer);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Event> getEntEvents() {
        ArrayList<Event> result = null;
        try 
        {
            Statement st = connection.createStatement();
            String request =
                    "SELECT title, description, timeToStart, type FROM events"
                            + " WHERE type='Представления' OR type='Концерт'";
            ResultSet answer = st.executeQuery(request);
            result = getListEvents(answer);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static ArrayList<Type> getTypes() {
        ArrayList<Type> result = new ArrayList<>();
        try
        {
            Statement st = connection.createStatement();
            ResultSet answer = st.executeQuery(
                    "SELECT * FROM typesOfEvents"
            );
            while (answer.next())
            {
                String name = answer.getString("type");
                String isEntertainment = answer.getString("isEntertainment");
                result.add(new Type(name, isEntertainment));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static void addEvent(String title, String description, long timeToStart, String type) {
        try {
            PreparedStatement add = connection.prepareStatement(
                    "INSERT INTO events (title, description, timeToStart, type) VALUES (?, ?, ?, ?)"
            );
            add.setString(1, title);
            add.setString(2, description);
            add.setLong(3, timeToStart);
            add.setString(4, type);
            add.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Deprecated
    public static void removeEvent(Event event) {
        try {
            PreparedStatement remove = connection.prepareStatement(
                    "INSERT  INTO typesOfEvents (title, description, timeToStart, type) VALUES (?, ?, ?, ?)"
            );
            remove.setString(1, event.getTitle());
            remove.setString(2, event.getDescription());
            remove.setLong(3, event.getTimeToStart());
            remove.setString(4, event.getType());
            remove.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void addType(Type type) {
        try {
            PreparedStatement remove = connection.prepareStatement(
                    "INSERT  INTO typesOfEvents (type, isEntertainment) VALUES (?, ?)"
            );
            remove.setString(1, type.getName());
            remove.setString(2, type.getIsEntertainment());
            remove.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void removeType(Type type) {
        try {
            PreparedStatement remove = connection.prepareStatement(
                    "DELETE FROM typesOfEvents WHERE type=?"
            );
            remove.setString(1, type.getName());
            remove.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
