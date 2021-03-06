package com.moudevops.quanlm.cardeal.model;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class CarType {
    String carTypeCode;
    String carTypeName;
    boolean isChecked = false;
    String thumbnail;

    public CarType() {
    }

    public CarType(String carTypeCode, String carTypeName) {

        this.carTypeCode = carTypeCode;
        this.carTypeName = carTypeName;
    }

    public String getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String carTypeCode) {
        this.carTypeCode = carTypeCode;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
