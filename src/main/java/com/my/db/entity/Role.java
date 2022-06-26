package com.my.db.entity;

public class Role extends Entity{
    private RoleName name;

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
