package com.example.quanlm.cardeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.quanlm.cardeal.fragment.SearchListFragment.CAR_DETAIL_PARAMS;

public class ActCarDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_car_detail);
        Bundle params = getIntent().getBundleExtra(CAR_DETAIL_PARAMS);
        String carCode = params.getString("car_code");
    }
}
