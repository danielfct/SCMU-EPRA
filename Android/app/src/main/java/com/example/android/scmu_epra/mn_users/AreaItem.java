package com.example.android.scmu_epra.mn_users;

public class AreaItem {

    private int id;
    private String name;
    private boolean isAlarmOn;

    public AreaItem(int id, String name, boolean isAlarmOn) {
        this.id = id;
        this.name = name;
        this.isAlarmOn = isAlarmOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAlarmOn() {
        return isAlarmOn;
    }

    public void setAlarmOn(boolean alarmOn) {
        isAlarmOn = alarmOn;
    }

    @Override
    public String toString() {
        return getName();
    }
}
