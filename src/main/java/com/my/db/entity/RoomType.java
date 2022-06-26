package com.my.db.entity;

import java.util.Objects;

public class RoomType extends Entity{
    private TypeName name;
    private String description;

    public TypeName getName() {
        return name;
    }

    public void setName(TypeName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum TypeName {
        APARTMENT, STD, SUITE, STUDIO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RoomType roomType = (RoomType) o;
        return name == roomType.name && description.equals(roomType.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description);
    }
}
