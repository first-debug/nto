package com.vladislav.models;

import javafx.beans.property.*;

import java.util.ArrayList;

public class Space {
    public final static ArrayList<Space> objectsList = new ArrayList<>();

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final IntegerProperty area;
    private final IntegerProperty capacity;
    private final BooleanProperty hasSeveralParts;
    private final Integer[] partsArea;
    private final ArrayList<Booking> bookingList;

    public Space(Integer id, String name, String description, Integer area,
                 Integer capacity, Boolean hasSeveralParts, Integer[] partsArea) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.area = new SimpleIntegerProperty(area);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.hasSeveralParts = new SimpleBooleanProperty(hasSeveralParts);
        this.partsArea = partsArea;
        this.bookingList = DataBase.getBookingList(this);
        Space.objectsList.add(this);
    }


    public Integer getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public Integer getArea() {
        return area.get();
    }

    public IntegerProperty areaProperty() {
        return area;
    }

    public Integer getCapacity() {
        return capacity.get();
    }

    public IntegerProperty capacityProperty() {
        return capacity;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setArea(int area) {
        this.area.set(area);
    }

    public void setCapacity(int capacity) {
        this.capacity.set(capacity);
    }

    public ArrayList<Booking> getBookingList() {
        return bookingList;
    }

    public boolean hasSeveralParts() {
        return hasSeveralParts.get();
    }

    public BooleanProperty hasSeveralPartsProperty() {
        return hasSeveralParts;
    }

    public Integer[] getPartsArea() {
        return partsArea;
    }
}
