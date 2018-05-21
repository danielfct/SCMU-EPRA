package com.example.android.scmu_epra.mn_history;

class AlarmHistoryItem {

    enum AlarmHistoryType {
        AlarmTrigger, AlarmOnOff
    }

    private AlarmHistoryType type;
    private String message;
    private String date;

    AlarmHistoryItem(AlarmHistoryType type, String message, String date) {
        this.type = type;
        this.message = message;
        this.date = date;
    }

    public AlarmHistoryType getType() {
        return type;
    }

    public void setType(AlarmHistoryType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
