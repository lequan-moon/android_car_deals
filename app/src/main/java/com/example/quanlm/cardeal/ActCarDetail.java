package com.example.quanlm.cardeal;

import android.content.SharedPreferences;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlm.cardeal.configure.Constants;
import com.example.quanlm.cardeal.model.Car;

import java.util.Set;

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
        Bundle params = getIntent().getBundleExtra(Constants.CAR_DETAIL_PARAMS);
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

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        Set<String> favoriteCars = sharedPreferences.getStringSet(Constants.FAVORITE, new ArraySet<String>());
        if (favoriteCars.contains(selectedCar.getCode())) {
            btnLike.setBackground(getDrawable(R.drawable.ic_favorite_black_48dp));
        }
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
                // Check is added to favorites
                // If true then remove it from favorites and change background of this button "add to favorites"
                // If not, add to favorites and change background of this button to "already added to favorites"
                SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isFavorite()) {
                    Set<String> favoriteCar = sharedPreferences.getStringSet(Constants.FAVORITE, new ArraySet<String>());
                    favoriteCar.remove(selectedCar.getCode());
                    editor.putStringSet(Constants.FAVORITE, favoriteCar);
                    editor.commit();
                    v.setBackground(getDrawable(R.drawable.ic_favorite_border_black_48dp));
                } else {
                    Set<String> favoriteCar = sharedPreferences.getStringSet(Constants.FAVORITE, new ArraySet<String>());
                    favoriteCar.add(selectedCar.getCode());
                    editor.putStringSet(Constants.FAVORITE, favoriteCar);
                    editor.commit();
                    v.setBackground(getDrawable(R.drawable.ic_favorite_black_48dp));
                }
            }
        });
    }

    private boolean isFavorite() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        Set<String> favoriteCar = sharedPreferences.getStringSet(Constants.FAVORITE, new ArraySet<String>());
        if (favoriteCar.contains(selectedCar.getCode())) {
            return true;
        } else {
            return false;
        }
    }
}
