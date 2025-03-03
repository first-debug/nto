package com.vladislav.domain;

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
    private final StringProperty type;
    private int[][] seats;

    private Space(Integer id, String name, String description, Integer area,
                  Integer capacity, Boolean hasSeveralParts, Integer[] partsArea, String type, String seats) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        if (type.equals("event")) {
            this.area = new SimpleIntegerProperty(area);
            this.capacity = new SimpleIntegerProperty(capacity);
            this.hasSeveralParts = new SimpleBooleanProperty(hasSeveralParts);
            this.partsArea = partsArea;
            this.firstArea = new SimpleIntegerProperty(partsArea[0]);
            this.secondArea = new SimpleIntegerProperty(partsArea[1]);
            this.bookingList = new FilteredList<>(Booking.objectsList, p -> p.getSpace().equals(this));
            this.seats = parseSeats(seats);
        } else {
            this.area = new SimpleIntegerProperty(area == null ? 0 : area);
            this.capacity = new SimpleIntegerProperty(capacity == null ? 0 : capacity);
            this.hasSeveralParts = new SimpleBooleanProperty(false);
            this.partsArea = new Integer[0];
            this.firstArea = new SimpleIntegerProperty(0);
            this.secondArea = new SimpleIntegerProperty(0);
            this.bookingList = new FilteredList<>(Booking.objectsList, p -> p.getSpace().equals(this));
            this.seats = null;
        }
        this.type = new SimpleStringProperty(type);
        objectsList.add(this);
        objectsId.add(id);
    }

    public static Space getInstance(Integer id, String name, String description, Integer area,
                                    Integer capacity, Boolean hasSeveralParts, Integer[] partsArea,
                                    String type, String seats) {
        int objectIndex = objectsId.indexOf(id);
        if (objectIndex == -1) {
            return new Space(id, name, description, area, capacity, hasSeveralParts, partsArea, type, seats);
        } else {
            objectsList.forEach(f -> {
                if (f.getId().equals(id)) {
                    f.setName(name);
                    f.setDescription(description);
                    f.setArea(area == null ? 0 : area);
                    f.setCapacity(capacity == null ? 0 : capacity);
                    f.setHasSeveralParts(hasSeveralParts != null && hasSeveralParts);
                    f.setFirstArea(area == null ? 0 : partsArea[0]);
                    f.setSecondArea(area == null ? 0 : partsArea[1]);
                    f.setType(type);
                    f.setSeats(parseSeats(seats));
                }
            });
            return objectsList.get(objectIndex);
        }
    }

    private static int[][] parseSeats(String seats) {
        String[] rows = seats.split(" ");
        char[] nums = seats.toCharArray();
        int maxRowLen = 0;
        for (String row : rows)
            if (row.length()> maxRowLen)
                maxRowLen = row.length();
        int[][] result = new int[rows.length][maxRowLen];
        for (int i = 0, j = 0, k = 0; i < nums.length; i++) {
            int num = (nums[i] - 48);
            if (num == -16) {
                j++;
                k = 0;
            } else {
                result[j][k++] = num;
            }
        }
        return result;
    }

    public static void remove(Space space) {
        objectsList.remove(space);
        objectsId.remove(space.getId());
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public Integer getArea() {
        return area.get();
    }

    public void setArea(int area) {
        this.area.set(area);
    }

    public IntegerProperty areaProperty() {
        return area;
    }

    public Integer getCapacity() {
        return capacity.get();
    }

    public void setCapacity(int capacity) {
        this.capacity.set(capacity);
    }

    public IntegerProperty capacityProperty() {
        return capacity;
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

    public void setFirstArea(int firstArea) {
        this.firstArea.set(firstArea);
    }

    public IntegerProperty firstAreaProperty() {
        return firstArea;
    }

    public int getSecondArea() {
        return secondArea.get();
    }

    public void setSecondArea(int secondArea) {
        this.secondArea.set(secondArea);
    }

    public IntegerProperty secondAreaProperty() {
        return secondArea;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int[][] getSeats() {
        return seats;
    }

    public void setSeats(int[][] seats) {
        this.seats = seats;
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
                ", seats=" + Arrays.deepToString(seats) +
                '}';
    }
}