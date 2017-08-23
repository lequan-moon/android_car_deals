package com.example.quanlm.cardeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.quanlm.cardeal.adapter.BrandAdapter;
import com.example.quanlm.cardeal.adapter.CarTypeAdapter;
import com.example.quanlm.cardeal.model.Brand;
import com.example.quanlm.cardeal.model.CarType;

import java.util.ArrayList;
import java.util.List;

public class ActAddCarDealStep1 extends AppCompatActivity {

    RecyclerView rcvBrand;
    RecyclerView rcvCarModel;
    TextView txtNewOld;
    Switch switchNewOld;
    RecyclerView rcvImages;


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

        rcvImages = (RecyclerView) findViewById(R.id.rcvImages);
    }

    private void initEvents() {
        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: QuanLM move to step 2 to add car deal. Should be on the same Activity
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
