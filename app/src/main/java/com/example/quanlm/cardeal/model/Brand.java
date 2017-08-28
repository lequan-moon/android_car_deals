package com.example.quanlm.cardeal.model;

import java.util.List;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class Brand {
    String brandCode;
    String brandName;
    boolean isSelected = false;

    public Brand() {
    }

    public Brand(String brandCode, String brandName) {
        this.brandCode = brandCode;
        this.brandName = brandName;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
