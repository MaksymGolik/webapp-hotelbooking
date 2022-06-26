package com.my.db.entity;

import java.util.Locale;

public class Role {
    private int id;
    private RoleName name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public enum RoleName{
        GUEST, ADMIN, UNKNOWN;

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
