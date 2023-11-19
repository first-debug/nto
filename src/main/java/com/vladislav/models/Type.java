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

    static public Type getValueFromName(String name)
    {
        for (Type type : values())
        {
            if (type.name.equals(name))
            {
                return type;
            }
        }
        return null;
    }
}