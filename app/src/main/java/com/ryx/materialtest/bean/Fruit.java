package com.ryx.materialtest.bean;

/**
 * Created by Ryx on 2019/1/2.
 */
public class Fruit {
    private String name;
    private int imageId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
