package com.example.android.scmu_epra.mn_devices;

class DevicesItem {

    enum DevicesType {
        Sensor, Actuator, Simulator
    }

    private DevicesType type;
    private String name;
    private boolean isOn;

    DevicesItem(String name, DevicesType type, boolean isOn) {
        this.type = type;
        this.name = name;
        this.isOn = isOn;
    }

    public DevicesType getType() {
        return type;
    }

    public void setType(DevicesType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String message) {
        this.name = message;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
