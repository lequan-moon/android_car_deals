package com.example.quanlm.cardeal.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class Car implements Serializable {
    String code;
    String name;
    String description;
    String price;
    String brand;
    String carType;
    String dealerName;
    String dealerPhoneNumber;
    String slogan;
    List<String> images;

    String dealerDisplayName;

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Car() {
    }

    public Car(String code, String name, String description, String price) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Car(String code,
               String name,
               String description,
               String price,
               String brand,
               String carType,
               String dealerName,
               String dealerPhoneNumber,
               List<String> images,
               String dealerDisplayName) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
        this.carType = carType;
        this.dealerName = dealerName;
        this.dealerPhoneNumber = dealerPhoneNumber;
        this.images = images;
        this.dealerDisplayName = dealerDisplayName;
    }

    public String getDealerDisplayName() {
        return dealerDisplayName;
    }

    public void setDealerDisplayName(String dealerDisplayName) {
        this.dealerDisplayName = dealerDisplayName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerPhoneNumber() {
        return dealerPhoneNumber;
    }

    public void setDealerPhoneNumber(String dealerPhoneNumber) {
        this.dealerPhoneNumber = dealerPhoneNumber;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
