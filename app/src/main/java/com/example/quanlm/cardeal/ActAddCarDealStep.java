package com.example.quanlm.cardeal;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlm.cardeal.adapter.CarThumbAdapter;
import com.example.quanlm.cardeal.adapter.SingleChoiceBrandAdapter;
import com.example.quanlm.cardeal.adapter.SingleChoiceCarTypeAdapter;
import com.example.quanlm.cardeal.configure.Constants;
import com.example.quanlm.cardeal.model.Brand;
import com.example.quanlm.cardeal.model.CarType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActAddCarDealStep extends AppCompatActivity {

    RecyclerView rcvBrand;
    RecyclerView rcvCarModel;
    TextView txtNewOld;
    Switch switchNewOld;
    RecyclerView rcvImages;
    TextView txtCarName;
    TextView txtCarDescription;
    TextView txtDealerName;
    TextView txtDealerPhoneNumber;

    SingleChoiceBrandAdapter adtBrand;
    SingleChoiceCarTypeAdapter adtCarType;

    View viewAddCarStep1;
    View viewAddCarStep2;

    private static final long ANIMATION_TIME = 1000;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_car_deal_step1);

        initControls();
        initEvents();
    }

    private void initControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        rcvBrand = (RecyclerView) findViewById(R.id.rcvBrand);
        List<Brand> lstBrand = getListBrandCondition();
        adtBrand = new SingleChoiceBrandAdapter(this, lstBrand);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        rcvBrand.setLayoutManager(layoutManager);
        rcvBrand.setAdapter(adtBrand);


        // TODO: QuanLM process show/hide model when choose Brand
        // TODO: QuanLM Temporary set cartype, need to set to car model
        findViewById(R.id.layoutCarModel);

        rcvCarModel = (RecyclerView) findViewById(R.id.rcvCarModel);
        List<CarType> lstCarType = getListCarTypeCondition();
        adtCarType = new SingleChoiceCarTypeAdapter(this, lstCarType);
        RecyclerView.LayoutManager layoutManagerCarType = new GridLayoutManager(this, 3);
        rcvCarModel.setLayoutManager(layoutManagerCarType);
        rcvCarModel.setAdapter(adtCarType);

        txtCarName = (TextView) findViewById(R.id.txtCarName);
        txtCarDescription = (TextView) findViewById(R.id.txtCarDescription);
        txtDealerName = (TextView) findViewById(R.id.txtDealerName);
        txtDealerPhoneNumber = (TextView) findViewById(R.id.txtDealerPhoneNumber);
        txtNewOld = (TextView) findViewById(R.id.txtNewOld);
        switchNewOld = (Switch) findViewById(R.id.switchNewOld);
        switchNewOld.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtNewOld.setText("Xe mới");
                } else {
                    txtNewOld.setText("Xe cũ");
                }
            }
        });


        viewAddCarStep1 = findViewById(R.id.viewAddCarDealStep1);
        viewAddCarStep2 = findViewById(R.id.viewAddCarDealStep2);

        rcvImages = (RecyclerView) findViewById(R.id.rcvImages);
        List<Integer> lstCarThumb = new ArrayList<>();
        lstCarThumb.add(0);
        lstCarThumb.add(R.drawable.car_sample_1);
        lstCarThumb.add(R.drawable.car_sample_2);
        lstCarThumb.add(R.drawable.car_sample_3);
        lstCarThumb.add(R.drawable.car_sample_4);
        lstCarThumb.add(R.drawable.car_sample_5);
        lstCarThumb.add(R.drawable.car_sample_6);
        lstCarThumb.add(R.drawable.car_sample_7);
        lstCarThumb.add(R.drawable.car_sample_8);
        lstCarThumb.add(R.drawable.car_sample_9);
        CarThumbAdapter adtCarThumb = new CarThumbAdapter(this, lstCarThumb);
        RecyclerView.LayoutManager carThumbLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvImages.setLayoutManager(carThumbLayoutManager);
        rcvImages.setAdapter(adtCarThumb);

//        viewAddCarStep1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                paramsStep2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                paramsStep2.leftMargin = viewAddCarStep1.getWidth();
//                viewAddCarStep2.setLayoutParams(paramsStep2);
//            }
//        });

    }

    private void initEvents() {
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate
                if (!isStep1Valid()) {
                    Toast.makeText(ActAddCarDealStep.this, "Hay chon brand/car type", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show/hide related buttons
                findViewById(R.id.btnDone).setVisibility(View.VISIBLE);
                findViewById(R.id.btnBack).setVisibility(View.VISIBLE);
                findViewById(R.id.btnNext).setVisibility(View.GONE);

                // Animation
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                viewAddCarStep2.setLayoutParams(params);

                viewAddCarStep1.animate().setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        viewAddCarStep2.setVisibility(View.VISIBLE);
                        viewAddCarStep2.setAlpha(0);
                        viewAddCarStep2.animate()
                                .setListener(null)
                                .translationX(0)
                                .alpha(1)
                                .setDuration(ANIMATION_TIME);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewAddCarStep1.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).translationX(-viewAddCarStep1.getWidth()).setDuration(ANIMATION_TIME);

//                paramsStep1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                CountDownTimer countDownTimer = new CountDownTimer(1000, 10) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//                        final int width = viewAddCarStep1.getWidth();
//                        paramsStep1.leftMargin -= width/50;
//                        if(paramsStep1.leftMargin < -viewAddCarStep1.getWidth()){
//                            paramsStep1.leftMargin = -viewAddCarStep1.getWidth();
//                        }
//                        viewAddCarStep1.setLayoutParams(paramsStep1);
//
//                        paramsStep2.leftMargin -= width/50;
//                        if(paramsStep2.leftMargin < 0){
//                            paramsStep2.leftMargin = 0;
//                        }
//                        viewAddCarStep2.setLayoutParams(paramsStep2);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                    }
//                };
//                countDownTimer.start();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.btnDone).setVisibility(View.GONE);
                findViewById(R.id.btnBack).setVisibility(View.GONE);
                findViewById(R.id.btnNext).setVisibility(View.VISIBLE);

//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//                viewAddCarStep1.setLayoutParams(params);

                viewAddCarStep2.animate().setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        viewAddCarStep1.setVisibility(View.VISIBLE);
                        viewAddCarStep1.setAlpha(0);
                        viewAddCarStep1.animate()
                                .setListener(null)
                                .translationX(0)
                                .alpha(1)
                                .setDuration(ANIMATION_TIME);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewAddCarStep2.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).translationX(viewAddCarStep2.getWidth()).setDuration(ANIMATION_TIME);
            }
        });

        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStep2Valid()) {
                    Toast.makeText(ActAddCarDealStep.this, "Hay nhap day du thong tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get all input data and post to webservice
                String brand = ((SingleChoiceBrandAdapter) rcvBrand.getAdapter()).getSelectedBrand().getBrandCode();
                String carType = ((SingleChoiceCarTypeAdapter) rcvCarModel.getAdapter()).getSelectedCarType().getCarTypeCode();
                int isNew = switchNewOld.isChecked() ? 1 : 0;
                String carName = txtCarName.getText().toString();
                String carDescription = txtCarDescription.getText().toString();
                String dealerName = txtDealerName.getText().toString();
                String dealerPhoneNumber = txtDealerPhoneNumber.getText().toString();
                Toast.makeText(ActAddCarDealStep.this, "brand: " + brand +
                        " \ncarType:" + carType +
                        " \nisNew:" + isNew +
                        " \ncarName:" + carName +
                        " \ncarDescription:" + carDescription +
                        " \ndealerName:" + dealerName +
                        " \ndealerPhoneNumber:" + dealerPhoneNumber +
                        "", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isStep2Valid() {
        if (!"".equals(txtCarName.getText())) {
            return true;
        }
        if (!"".equals(txtCarDescription.getText())) {
            return true;
        }
        if (!"".equals(txtDealerName.getText())) {
            return true;
        }
        if (!"".equals(txtDealerPhoneNumber.getText())) {
            return true;
        }
        return false;
    }

    private boolean isStep1Valid() {
        if (((SingleChoiceBrandAdapter) rcvBrand.getAdapter()).getSelectedBrand() == null) {
            return false;
        }

        if (((SingleChoiceCarTypeAdapter) rcvCarModel.getAdapter()).getSelectedCarType() == null) {
            return false;
        }
        return true;
    }

    private List<Brand> getListBrandCondition() {
        final List<Brand> lstBrand = new ArrayList<>();
        DatabaseReference brandTable = mDatabase.child(Constants.BRAND_TABLE);
        brandTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot brand :
                        dataSnapshot.getChildren()) {
                    Brand objBrand = brand.getValue(Brand.class);
                    lstBrand.add(objBrand);
                }
                adtBrand.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return lstBrand;
    }

    private List<CarType> getListCarTypeCondition() {
        final List<CarType> lstCarType = new ArrayList<>();
        DatabaseReference brandTable = mDatabase.child(Constants.CAR_TYPE_TABLE);
        brandTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot carType :
                        dataSnapshot.getChildren()) {
                    CarType objCarType = carType.getValue(CarType.class);
                    lstCarType.add(objCarType);
                }
                adtCarType.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return lstCarType;
    }

}
