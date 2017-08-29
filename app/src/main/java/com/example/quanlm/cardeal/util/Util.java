package com.example.quanlm.cardeal.util;

import com.example.quanlm.cardeal.model.Car;

/**
 * Created by MyPC on 29/08/2017.
 */

public class Util {
    public static String generateFavoriteString(Car car) {
        return car.getDealerName() + "@" + car.getCode();
    }

    public static String getFavoriteCarDealer(String favoriteString) {
        return favoriteString.substring(0, favoriteString.indexOf("@"));
    }

    public static String getFavoriteCarCode(String favoriteString) {
        return favoriteString.substring(favoriteString.indexOf("@") + 1, favoriteString.length());
    }
}
