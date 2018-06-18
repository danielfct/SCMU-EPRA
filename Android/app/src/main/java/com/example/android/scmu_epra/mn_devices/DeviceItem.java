package com.example.android.scmu_epra.mn_devices;

public class DeviceItem {

    public enum DeviceType {
        Sensor, Actuator, Simulator
    }

    private String name;
    private DeviceType type;
    private boolean isOn;
    private int areaId;

    public DeviceItem(String name, DeviceType type, boolean isOn, int areaId) {
        this.name = name;
        this.type = type;
        this.isOn = isOn;
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
}
