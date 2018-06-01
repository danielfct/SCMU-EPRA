package com.example.android.scmu_epra.connection;

public class Row {

    private int id;
    private String evento;
    private String data;

    public Row(int id, String evento, String data) {
        this.id = id;
        this.evento = evento;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getEvento() {
        return evento;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Row{" +
                "id=" + id +
                ", evento='" + evento + '\'' +
                ", data=" + data +
                '}';
    }
}
