package com.example.android.scmu_epra.mn_burglaryManag;

public class BurglaryManagementItem {

    private String area;
    private int duration;

    public BurglaryManagementItem(String area, int duration) {
        this.area = area;
        this.duration = duration;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
