package com.moudevops.quanlm.cardeal.model;

import android.widget.ImageView;

import java.util.Map;

/**
 * Created by MyPC on 30/09/2017.
 */

public class ImageEntry implements Map.Entry<String, ImageView> {
    private String key;
    private ImageView value;

    public ImageEntry(String key, ImageView value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public ImageView getValue() {
        return value;
    }

    @Override
    public ImageView setValue(ImageView value) {
        this.value = value;
        return this.value;
    }
}
