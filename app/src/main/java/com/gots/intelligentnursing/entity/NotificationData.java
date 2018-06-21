package com.gots.intelligentnursing.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Accumulei
 * @date 2018/6/17.
 */
public class NotificationData {

    private int imageId;
    private String date;
    private Date mDate;
    private String event;
    private String status;
    private int type;

    public NotificationData(String event, int type) {
        this.event = event;
        this.type = type;
    }

    public NotificationData(int imageId, String date, String event, String status) {
        this.imageId = imageId;
        this.date = date;
        this.event = event;
        this.status = status;
    }

    public NotificationData() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {

        return status;
    }

    public int getType() {
        return type;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getState() {
        return status;
    }

    public void setState(String state) {
        this.status = state;
    }
}
