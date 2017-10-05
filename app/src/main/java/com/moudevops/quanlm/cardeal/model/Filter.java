package com.moudevops.quanlm.cardeal.model;

import static com.moudevops.quanlm.cardeal.configure.Constants.PRICE_UNIT;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class Filter {
    String[] brandCode;
    String[] carTypeCode;
    String priceStart;
    String priceEnd;
    String manufacturedYearStart;
    String manufacturedYearEnd;

    public Filter(String[] brandCode, String[] carTypeCode, String priceStart, String priceEnd, String manufacturedYearStart, String manufacturedYearEnd) {
        this.brandCode = brandCode;
        this.carTypeCode = carTypeCode;
        this.priceStart = priceStart;
        this.priceEnd = priceEnd;
        this.manufacturedYearStart = manufacturedYearStart;
        this.manufacturedYearEnd = manufacturedYearEnd;
    }

    public double getPriceStartValue() {
        return Double.valueOf(getPriceStart()) * PRICE_UNIT;
    }

    public double getPriceEndValue() {
        return Double.valueOf(getPriceEnd()) * PRICE_UNIT;
    }

    public String[] getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String[] brandCode) {
        this.brandCode = brandCode;
    }

    public String[] getCarTypeCode() {
        return carTypeCode;
    }

    public void setCarTypeCode(String[] carTypeCode) {
        this.carTypeCode = carTypeCode;
    }

    public String getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(String priceStart) {
        this.priceStart = priceStart;
    }

    public String getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(String priceEnd) {
        this.priceEnd = priceEnd;
    }

    public String getManufacturedYearStart() {
        return manufacturedYearStart;
    }

    public void setManufacturedYearStart(String manufacturedYearStart) {
        this.manufacturedYearStart = manufacturedYearStart;
    }

    public String getManufacturedYearEnd() {
        return manufacturedYearEnd;
    }

    public void setManufacturedYearEnd(String manufacturedYearEnd) {
        this.manufacturedYearEnd = manufacturedYearEnd;
    }
}
