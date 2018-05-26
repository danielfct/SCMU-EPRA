package com.example.android.scmu_epra.mn_home;

public class HomeItem {

    private int id;
    private String name;
    private int estadoAtual;
    private int areaId;

    public HomeItem(int id, String name, int estadoAtual, int areaId) {
        this.id = id;
        this.name = name;
        this.estadoAtual = estadoAtual;
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(int estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }
}
