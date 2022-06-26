package com.my.db.entity;

public class BookingStatus extends Entity{
    private StatusName name;
    private String description;

    public StatusName getName() {
        return name;
    }

    public void setName(StatusName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum StatusName{
        CREATED, CANCELED, CONFIRMED, DENIED, PAID, COMPLETED, CANCELLATION_REQUESTED;

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
