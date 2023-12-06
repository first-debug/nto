package com.vladislav.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.ArrayList;
import java.util.Arrays;

public class Space {
    public final static ObservableList<Space> objectsList = FXCollections.observableArrayList();
    public final static ArrayList<Integer> objectsId = new ArrayList<>();

    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty description;
    private final IntegerProperty area;
    private final IntegerProperty capacity;
    private final BooleanProperty hasSeveralParts;
    private final Integer[] partsArea;
    private final IntegerProperty firstArea;
    private final IntegerProperty secondArea;
    private final FilteredList<Booking> bookingList;

    private Space(Integer id, String name, String description, Integer area,
                 Integer capacity, Boolean hasSeveralParts, Integer[] partsArea) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.area = new SimpleIntegerProperty(area);
        this.capacity = new SimpleIntegerProperty(capacity);
        this.hasSeveralParts = new SimpleBooleanProperty(hasSeveralParts);
        this.partsArea = partsArea;
        this.firstArea = new SimpleIntegerProperty(partsArea[0]);
        this.secondArea = new SimpleIntegerProperty(partsArea[0]);
        this.bookingList = new FilteredList<>(Booking.objectsList, p -> p.getSpace().equals(this));
        objectsList.add(this);
        objectsId.add(id);
    }

    public static Space getInstant(Integer id, String name, String description, Integer area,
                                   Integer capacity, Boolean hasSeveralParts, Integer[] partsArea) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new Space(id, name, description, area, capacity, hasSeveralParts, partsArea);
        } else {
            objectsList.forEach(f -> {
                if (f.getId().equals(id)) {
                    f.setName(name);
                    f.setDescription(description);
                    f.setArea(area);
                    f.setCapacity(capacity);
                    f.setHasSeveralParts(hasSeveralParts);
                    f.setFirstArea(partsArea[0]);
                    f.setFirstArea(partsArea[1]);
                    return;
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    public static void remove(Space space) {
        objectsList.remove(space);
        objectsId.remove(space.getId());
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

    public ObservableList<Booking> getBookingList() {
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

    public void setHasSeveralParts(boolean hasSeveralParts) {
        this.hasSeveralParts.set(hasSeveralParts);
    }

    public int getFirstArea() {
        return firstArea.get();
    }

    public IntegerProperty firstAreaProperty() {
        return firstArea;
    }

    public void setFirstArea(int firstArea) {
        this.firstArea.set(firstArea);
    }

    public int getSecondArea() {
        return secondArea.get();
    }

    public IntegerProperty secondAreaProperty() {
        return secondArea;
    }

    public void setSecondArea(int secondArea) {
        this.secondArea.set(secondArea);
    }

    @Override
    public String toString() {
        return "Space{" +
                "id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", area=" + area +
                ", capacity=" + capacity +
                ", hasSeveralParts=" + hasSeveralParts +
                ", partsArea=" + Arrays.toString(partsArea) +
                ", firstArea=" + firstArea +
                ", secondArea=" + secondArea +
                ", bookingList=" + bookingList +
                '}';
    }
}