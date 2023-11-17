package com.vladislav.models;

public enum Type 
{
    PERFORMANCE("Предстваление"),
    CONCERT("Концерт"),
    REHEARSAL("Репетиция"),
    EXHIBITION("Выставка"),     
    LECTURE("Лекция");


    private String name;

    Type(String name) 
    {
        this.name = name;
    }
    
    String getName()
    {
        return name;
    }
}