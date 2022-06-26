package com.my.db.entity;

import java.util.Objects;

public class RoomInfo extends Entity{
    private RoomType type;
    private int capacity;
    private double price;
    private String description;
    private String imageUrl;

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RoomInfo roomInfo = (RoomInfo) o;
        return capacity == roomInfo.capacity && Double.compare(roomInfo.price, price) == 0 && type.equals(roomInfo.type) && description.equals(roomInfo.description) && imageUrl.equals(roomInfo.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, capacity, price, description, imageUrl);
    }
}
