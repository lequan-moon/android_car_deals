package com.example.quanlm.cardeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlm.cardeal.model.Car;

import static com.example.quanlm.cardeal.fragment.SearchListFragment.CAR_DETAIL_PARAMS;

public class ActCarDetail extends AppCompatActivity {
    ImageView imgCarThumb;
    TextView txtCarName;
    TextView btnBack;
    TextView btnShare;
    TextView txtPrice;
    TextView txtRating;
    TextView txtArea;
    TextView txtDealerName;
    TextView txtDealerPhoneNumber;
    TextView btnBuy;
    TextView btnLike;

    Car selectedCar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_car_detail);
        Bundle params = getIntent().getBundleExtra(CAR_DETAIL_PARAMS);
        selectedCar = (Car) params.getSerializable("selected_car");

        initControls();
        initEvents();
    }

    private void initControls() {
        imgCarThumb = (ImageView) findViewById(R.id.imgCarThumb);
        txtCarName = (TextView) findViewById(R.id.txtCarName);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtRating = (TextView) findViewById(R.id.txtRating);
        txtArea = (TextView) findViewById(R.id.txtArea);
        txtDealerName = (TextView) findViewById(R.id.txtDealerName);
        txtDealerPhoneNumber = (TextView) findViewById(R.id.txtDealerPhoneNumber);
        btnBack = (TextView) findViewById(R.id.btnBack);
        btnShare = (TextView) findViewById(R.id.btnShare);
        btnBuy = (TextView) findViewById(R.id.btnBuy);
        btnLike = (TextView) findViewById(R.id.btnLike);

        txtCarName.setText(selectedCar.getName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_out, R.anim.right_in);
    }

    private void initEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
