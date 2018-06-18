package com.example.android.scmu_epra.mn_users;

public class AreaItem {

    private int id;
    private String name;
    private boolean isAlarmOn;
    private String sensor;

    public AreaItem(int id, String name, boolean isAlarmOn, String sensor) {
        this.id = id;
        this.name = name;
        this.isAlarmOn = isAlarmOn;
        this.sensor = sensor;
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

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return getName();
    }
}
