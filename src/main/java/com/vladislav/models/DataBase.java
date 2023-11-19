package com.vladislav.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import java.sql.Statement;
import java.text.SimpleDateFormat;


public class DataBase {
    private static Connection connection = null;

    public DataBase()
    {
        
        try 
        {
            connection = DriverManager.getConnection("jdbc:sqlite:db.db");
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getMessage());
        } 
    }

    public void closeConnection()
    {
        try {
			if (connection != null) {
                connection.close();
            }
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
    }

    static public ArrayList<Event> getEduEvents()
    {
        ArrayList<Event> result = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.YYYY");
        try 
        {
            Statement st = connection.createStatement();
            String request = "SELECT title, description, timeToStart, type FROM events "
            + "WHERE type='Лекция' OR type='Выставка' OR type='Репетиция'";
            ResultSet answer = st.executeQuery(request);
            while (answer.next())
            {
                String title = answer.getString("title");
                String description = answer.getString("description");
                String timeToStart = format.format(new Date(answer.getLong("timeToStart")));
                String type = answer.getString("type");

                result.add(new Event(title, description, timeToStart, type));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    static public ArrayList<Event> getEntEvents()
    {
        ArrayList<Event> result = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.YYYY");
        try 
        {
            Statement st = connection.createStatement();
            String request = "SELECT title, description, timeToStart, type FROM events"
            + " WHERE type='Представления' OR type='Концерт'";
            ResultSet answer = st.executeQuery(request);
            while (answer.next())
            {
                String title = answer.getString("title");
                String description = answer.getString("description");
                String timeToStart = format.format(new Date(answer.getLong("timeToStart")));
                String type = answer.getString("type");

                result.add(new Event(title, description, timeToStart, type));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }
}
