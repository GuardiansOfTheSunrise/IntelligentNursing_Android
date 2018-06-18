package com.gots.intelligentnursing.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Accumulei
 * @date 2018/6/17.
 */
public class MyNotification {

    private int imageId;
    private Date mDate;
    private String event;
    private String status;

    public MyNotification(int imageId, String event, String status) {
        this.imageId = imageId;
        this.event = event;
        this.status = status;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Date getDate() {
        return mDate;
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
