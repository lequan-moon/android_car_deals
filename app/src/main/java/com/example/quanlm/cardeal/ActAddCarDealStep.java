package com.example.quanlm.cardeal;

import android.animation.Animator;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.quanlm.cardeal.adapter.BrandAdapter;
import com.example.quanlm.cardeal.adapter.CarTypeAdapter;
import com.example.quanlm.cardeal.model.Brand;
import com.example.quanlm.cardeal.model.CarType;

import java.util.ArrayList;
import java.util.List;

public class ActAddCarDealStep extends AppCompatActivity {

    RecyclerView rcvBrand;
    RecyclerView rcvCarModel;
    TextView txtNewOld;
    Switch switchNewOld;
    RecyclerView rcvImages;

    View viewAddCarStep1;
    View viewAddCarStep2;
    FrameLayout.LayoutParams paramsStep1;
    FrameLayout.LayoutParams paramsStep2;

    private static final long ANIMATION_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_car_deal_step1);

        initControls();
        initEvents();
    }

    private void initControls() {
        rcvBrand = (RecyclerView) findViewById(R.id.rcvBrand);
        List<Brand> lstBrand = getListBrandCondition();
        BrandAdapter adtBrand = new BrandAdapter(this, lstBrand);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        rcvBrand.setLayoutManager(layoutManager);
        rcvBrand.setAdapter(adtBrand);

        // TODO: QuanLM process show/hide model when choose Brand
        // TODO: QuanLM Temporary set cartype, need to set to car model
        findViewById(R.id.layoutCarModel);

        rcvCarModel = (RecyclerView) findViewById(R.id.rcvCarModel);
        List<CarType> lstCarType = getListCarTypeCondition();
        CarTypeAdapter adtCarType = new CarTypeAdapter(this, lstCarType);
        RecyclerView.LayoutManager layoutManagerCarType = new GridLayoutManager(this, 3);
        rcvCarModel.setLayoutManager(layoutManagerCarType);
        rcvCarModel.setAdapter(adtCarType);

        switchNewOld = (Switch) findViewById(R.id.switchNewOld);


        viewAddCarStep1 = findViewById(R.id.viewAddCarDealStep1);
        viewAddCarStep2 = findViewById(R.id.viewAddCarDealStep2);

        rcvImages = (RecyclerView) findViewById(R.id.rcvImages);

    }

    private void initEvents() {
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.btnDone).setVisibility(View.VISIBLE);
                findViewById(R.id.btnBack).setVisibility(View.VISIBLE);
                findViewById(R.id.btnNext).setVisibility(View.GONE);
                paramsStep1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                CountDownTimer countDownTimer = new CountDownTimer(1000, 10) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        final int width = viewAddCarStep1.getWidth();
                        paramsStep1.leftMargin -= width/56;
                        if(paramsStep1.leftMargin < -viewAddCarStep1.getWidth()){
                            paramsStep1.leftMargin = -viewAddCarStep1.getWidth();
                        }
                        viewAddCarStep1.setLayoutParams(paramsStep1);
                    }

                    @Override
                    public void onFinish() {
                    }
                };
                countDownTimer.start();

//                viewAddCarStep1.animate().setListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
////                        viewAddCarStep2.setVisibility(View.VISIBLE);
//                        viewAddCarStep2.animate()
//                                .setListener(null)
//                                .translationX(0)
//                                .setDuration(ANIMATION_TIME);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        viewAddCarStep1.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                }).translationX(-viewAddCarStep1.getWidth()).setDuration(ANIMATION_TIME);
            }
        });

        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.btnDone).setVisibility(View.GONE);
                findViewById(R.id.btnBack).setVisibility(View.GONE);
                findViewById(R.id.btnNext).setVisibility(View.VISIBLE);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                viewAddCarStep1.setLayoutParams(params);

//                viewAddCarStep2.animate().setListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        viewAddCarStep1.setVisibility(View.VISIBLE);
//                        viewAddCarStep1.animate()
//                                .setListener(null)
//                                .translationX(0)
//                                .setDuration(ANIMATION_TIME);
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
////                        viewAddCarStep2.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                }).translationX(viewAddCarStep2.getWidth()).setDuration(ANIMATION_TIME);
            }
        });
    }

    private List<Brand> getListBrandCondition() {
        List<Brand> lstBrand = new ArrayList<>();
        lstBrand.add(new Brand("Honda", "Honda"));
        lstBrand.add(new Brand("Toyota", "Toyota"));
        lstBrand.add(new Brand("Hyundai", "Hyundai"));
        lstBrand.add(new Brand("Suzuki", "Suzuki"));
        lstBrand.add(new Brand("Mercedes", "Mercedes-Benz"));
        lstBrand.add(new Brand("Audi", "Audi"));
        lstBrand.add(new Brand("BMW", "BMW"));

        return lstBrand;
    }

    private List<CarType> getListCarTypeCondition() {
        List<CarType> lstCarType = new ArrayList<>();
        lstCarType.add(new CarType("type1", "Sedan"));
        lstCarType.add(new CarType("type2", "Hatchback"));
        lstCarType.add(new CarType("type3", "MPV"));
        lstCarType.add(new CarType("type4", "SUV"));
        lstCarType.add(new CarType("type5", "Crossover"));
        lstCarType.add(new CarType("type6", "Coupe"));

        return lstCarType;
    }
}
