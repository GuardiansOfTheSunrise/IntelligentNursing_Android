package com.gots.intelligentnursing.entity;

/**
 * @author Accumulei
 * @date 2018/6/4.
 */
public class MineItem {

    private int imageId;
    private String item;

    public MineItem(int imageId, String item) {
        this.imageId = imageId;
        this.item = item;
    }

    public MineItem() {

    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}


