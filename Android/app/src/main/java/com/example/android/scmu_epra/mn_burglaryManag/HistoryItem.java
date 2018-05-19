package com.example.android.scmu_epra.mn_burglaryManag;

public class HistoryItem {

    private String name;
    private String time;

    HistoryItem(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

}
