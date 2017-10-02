package com.example.quanlm.cardeal.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.quanlm.cardeal.adapter.CarAdapter;
import com.example.quanlm.cardeal.configure.Constants;
import com.example.quanlm.cardeal.model.Car;

import java.util.List;

/**
 * Created by QuanLM on 10/2/2017.
 */

public class FetchImageBroadcastReceiver extends BroadcastReceiver {
    CarAdapter mAdapter;

    public FetchImageBroadcastReceiver() {
    }

    public FetchImageBroadcastReceiver(CarAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle args = intent.getBundleExtra(Constants.IMAGE_PACKS);
        int position = args.getInt(Constants.POSITION);
        String imageUri = args.getString(Constants.IMAGE_URI);
        List<Car> lstCar = mAdapter.getListdata();
        lstCar.get(position).getImageUris().add(imageUri);
        Log.d("fetchImage", "onReceive: " + imageUri);
        Log.d("fetchImage", "lstCar size: " + lstCar.size());
        mAdapter.updateData(lstCar);
    }
}
