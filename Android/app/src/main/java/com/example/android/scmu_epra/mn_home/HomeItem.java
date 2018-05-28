package com.example.android.scmu_epra.mn_home;

public class HomeItem {

    private int id;
    private String name;
    private int alarmeLigado;
    private String sensor;

    public HomeItem(int id, String name, int alarmeLigado, String sensor) {
        this.id = id;
        this.name = name;
        this.alarmeLigado = alarmeLigado;
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

    public int getAlarmeLigado() {
        return alarmeLigado;
    }

    public void setAlarmeLigado(int alarmeLigado) {
        this.alarmeLigado = alarmeLigado;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }
}
