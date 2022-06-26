package com.my.db.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class Booking extends Entity{
    private long userId;
    private BookingStatus status;
    private String guestSurname;
    private String guestName;
    private String guestPhoneNumber;
    private double price;
    private Timestamp createTime;
    private Timestamp lastUpdateTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getGuestSurname() {
        return guestSurname;
    }

    public void setGuestSurname(String guestSurname) {
        this.guestSurname = guestSurname;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPhoneNumber() {
        return guestPhoneNumber;
    }

    public void setGuestPhoneNumber(String guestPhoneNumber) {
        this.guestPhoneNumber = guestPhoneNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Booking booking = (Booking) o;
        return userId == booking.userId && Double.compare(booking.price, price) == 0 && status.equals(booking.status) && guestSurname.equals(booking.guestSurname) && guestName.equals(booking.guestName) && guestPhoneNumber.equals(booking.guestPhoneNumber) && createTime.equals(booking.createTime) && lastUpdateTime.equals(booking.lastUpdateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, status, guestSurname, guestName, guestPhoneNumber, price, createTime, lastUpdateTime);
    }
}
