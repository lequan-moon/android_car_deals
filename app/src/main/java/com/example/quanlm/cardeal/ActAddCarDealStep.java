package com.example.quanlm.cardeal;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.quanlm.cardeal.model.Car;
import com.example.quanlm.cardeal.model.CarType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActAddCarDealStep extends AppCompatActivity implements CarThumbAdapter.OnCarThumbSelectListener{

    private static final int REQUEST_CODE_CHOOSE = 1;
    RecyclerView rcvBrand;
    RecyclerView rcvCarModel;
    TextView txtNewOld;
    Switch switchNewOld;
    RecyclerView rcvImages;
    TextView txtCarName;
    TextView txtCarDescription;
    TextView txtCarPrice;
    TextView txtDealerName;
    TextView txtDealerPhoneNumber;

    SingleChoiceBrandAdapter adtBrand;
    SingleChoiceCarTypeAdapter adtCarType;
    CarThumbAdapter adtCarThumb;
    List<Uri> lstCarThumb;

    View viewAddCarStep1;
    View viewAddCarStep2;

    private static final long ANIMATION_TIME = 1000;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_car_deal_step1);

        initControls();
        initEvents();
    }

    private void initControls() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

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
        txtCarPrice = (TextView) findViewById(R.id.txtCarPrice);
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
        lstCarThumb = new ArrayList<>();
        lstCarThumb.add(null); // First item will be the add image button

        adtCarThumb = new CarThumbAdapter(this, lstCarThumb, this);
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

                // Upload images to storage
                final List<String> lstUploadedImages = new ArrayList<String>();
                for (int i = 1; i < lstCarThumb.size(); i++) { // Loop begin from 1 because the 0 index is button add image
                    Uri image = lstCarThumb.get(i);
                    String randomImageId = UUID.randomUUID().toString();
                    uploadTask = mStorage.child(randomImageId + ".jpeg").putFile(image);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            lstUploadedImages.add(task.getResult().getDownloadUrl().toString());
                            Log.d("Firebase", "onComplete: " + mStorage.getActiveUploadTasks().size());

                            // When all images are uploaded then process post deal
                            if (mStorage.getActiveUploadTasks().size() == 0) {
                                // Get all input data and post to webservice
                                String brand = ((SingleChoiceBrandAdapter) rcvBrand.getAdapter()).getSelectedBrand().getBrandCode();
                                String carType = ((SingleChoiceCarTypeAdapter) rcvCarModel.getAdapter()).getSelectedCarType().getCarTypeCode();
                                int isNew = switchNewOld.isChecked() ? 1 : 0;

//                                String carName = txtCarName.getText().toString();
                                String carName = brand + " - " + carType;

//                                String carDescription = txtCarDescription.getText().toString();
                                String carDescription = brand + " - " + carType + " lorem ipsum blah bloh";

                                // TODO: QuanLM Auth with social account
                                //txtDealerName.getText().toString();
                                String dealerName = "QuanLM";

//                                String dealerPhoneNumber = txtDealerPhoneNumber.getText().toString();
                                String dealerPhoneNumber = "000-0000-0000";

//                                String carPrice = txtCarPrice.getText().toString();
                                String carPrice = "1.000.000 $";

                                // Post data into database
                                DatabaseReference deals = mDatabase.child(Constants.DEAL_TABLE);
                                String carCode = UUID.randomUUID().toString();
                                Car car = new Car(carCode, carName, carDescription, carPrice, brand, carType, dealerName, dealerPhoneNumber, lstUploadedImages);
                                deals.child(dealerName).child(carCode).setValue(car);

                                // When post deals is done
                                deals.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Toast.makeText(ActAddCarDealStep.this, "Posting done!", Toast.LENGTH_SHORT).show();
                                        Intent itHome = new Intent(ActAddCarDealStep.this, MainActivity.class);
                                        startActivity(itHome);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(ActAddCarDealStep.this, "Posting error!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
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

    @Override
    public void onAddCarThumb() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(5)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.example.quanlm.cardeal.fileprovider"))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            List<Uri> images = Matisse.obtainResult(data);

            for (int i = 0; i < images.size(); i++) {
                lstCarThumb.add(images.get(i));
            }
            adtCarThumb.notifyDataSetChanged();
        }
    }
}
