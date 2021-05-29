package org.hse.lab2.model;

import com.google.gson.annotations.SerializedName;

public class TimeZone {
    @SerializedName("current_time")
    private String currentTime;

    public String getCurrentTime() { return currentTime; }

    public void setCurrentTime(String time) {
        this.currentTime = time;
    }
}
