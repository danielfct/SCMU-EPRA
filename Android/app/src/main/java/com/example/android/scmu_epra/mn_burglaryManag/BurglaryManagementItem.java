package com.example.android.scmu_epra.mn_burglaryManag;

public class BurglaryManagementItem {

    private String name;
    private String time;

    BurglaryManagementItem(String name, String time) {
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
