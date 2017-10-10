package com.moudevops.quanlm.cardeal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.moudevops.quanlm.cardeal.configure.Constants;
import com.moudevops.quanlm.cardeal.model.Car;
import com.moudevops.quanlm.cardeal.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Set;

public class ActCarDetail extends AppCompatActivity {
    SliderLayout slider;
    TextView txtCarName;
    TextView txtCarDescription;
    TextView btnShare;
    TextView txtPrice;
    TextView txtRating;
    TextView txtArea;
    TextView txtDealerName;
    TextView txtDealerPhoneNumber;
    TextView btnBuy;
    TextView btnLike;

    Car selectedCar;

    FirebaseDatabase mDatabase;
    FirebaseStorage mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_car_detail);
        Bundle params = getIntent().getBundleExtra(Constants.CAR_DETAIL_PARAMS);
        selectedCar = (Car) params.getSerializable("selected_car");
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
        initControls();
        initEvents();
    }

    private void initControls() {
        slider = (SliderLayout) findViewById(R.id.slider);
        txtCarName = (TextView) findViewById(R.id.txtCarName);
        txtCarDescription = (TextView) findViewById(R.id.txtCarDescription);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtRating = (TextView) findViewById(R.id.txtRating);
        txtArea = (TextView) findViewById(R.id.txtArea);
        txtDealerName = (TextView) findViewById(R.id.txtDealerName);
        txtDealerPhoneNumber = (TextView) findViewById(R.id.txtDealerPhoneNumber);
        btnShare = (TextView) findViewById(R.id.btnShare);
        btnBuy = (TextView) findViewById(R.id.btnBuy);
        btnLike = (TextView) findViewById(R.id.btnLike);

        txtCarName.setText(selectedCar.getName());
        txtCarDescription.setText(selectedCar.getDescription());
        txtPrice.setText(selectedCar.getPrice());
        txtDealerName.setText(selectedCar.getDealerDisplayName());
        txtDealerPhoneNumber.setText(selectedCar.getDealerPhoneNumber());

        if (selectedCar.getImages() != null && selectedCar.getImages().size() > 0) {
            for (int i = 0; i < selectedCar.getImages().size(); i++) {
                final DefaultSliderView slide = new DefaultSliderView(this);
                Task<Uri> downloadUrl = mStorage.getReferenceFromUrl(selectedCar.getImages().get(i)).getDownloadUrl();
                downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        slide.image(uri.toString());
                    }
                });

                slider.addSlider(slide);
            }
        }
//        Glide.with(this).load(selectedCar.getImages().get(0)).into(imgCarThumb);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        Set<String> favoriteCars = sharedPreferences.getStringSet(Constants.FAVORITE, new ArraySet<String>());
        if (favoriteCars.contains(Util.generateFavoriteString(selectedCar))) {
            btnLike.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_out, R.anim.right_in);
    }

    private void initEvents() {
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
                Set<String> favoriteCar = sharedPreferences.getStringSet(Constants.FAVORITE, new ArraySet<String>());
                if (favoriteCar.size() >= 5) {
                    Toast.makeText(ActCarDetail.this, "Only 5 deals allowed", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isFavorite()) {
                    favoriteCar.remove(Util.generateFavoriteString(selectedCar));
                    editor.putStringSet(Constants.FAVORITE, favoriteCar);
                    editor.commit();
                    v.setBackground(getResources().getDrawable(R.drawable.ic_favorite_border_black_48dp));
                } else {
                    favoriteCar.add(Util.generateFavoriteString(selectedCar));
                    editor.putStringSet(Constants.FAVORITE, favoriteCar);
                    editor.commit();
                    v.setBackground(getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
                }
            }
        });
    }

    private boolean isFavorite() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        Set<String> favoriteCar = sharedPreferences.getStringSet(Constants.FAVORITE, new ArraySet<String>());
        if (favoriteCar.contains(Util.generateFavoriteString(selectedCar))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (selectedCar.getImages() != null && selectedCar.getImages().size() > 1) {
            slider.startAutoCycle();
        } else {
            slider.stopAutoCycle();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        slider.stopAutoCycle();
    }
}
