package com.example.quanlm.cardeal.fragment;

import android.os.Bundle;
import android.service.notification.Condition;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.quanlm.cardeal.R;
import com.example.quanlm.cardeal.adapter.BrandAdapter;
import com.example.quanlm.cardeal.adapter.CarTypeAdapter;
import com.example.quanlm.cardeal.model.Brand;
import com.example.quanlm.cardeal.model.CarType;
import com.example.quanlm.cardeal.model.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class ConditionSearchDialogFragment extends DialogFragment {
    RecyclerView rcvBrand;
    RecyclerView rcvCarType;
    OnSearchConditionChangedListener mListener;

    String selectedBrandCode;
    String selectedCarTypeCode;
    String selectedPriceStart;
    String selectedPriceEnd;
    String selectedManufacturedYearStart;
    String selectedManufacturedYearEnd;

    public ConditionSearchDialogFragment() {
    }

    public static ConditionSearchDialogFragment newInstance(){
        ConditionSearchDialogFragment fragment = new ConditionSearchDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_condition_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initControls();
        initEvents();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void initEvents() {
        getView().findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter filter = new Filter(selectedBrandCode,
                        selectedCarTypeCode,
                        selectedPriceStart,
                        selectedPriceEnd,
                        selectedManufacturedYearStart,
                        selectedManufacturedYearEnd);

                mListener.OnSearchConditionChanged(filter);
            }
        });
    }

    private void initControls() {
        rcvBrand = (RecyclerView) getView().findViewById(R.id.rcvBrand);
        List<Brand> lstBrand = new ArrayList<>();
        lstBrand.add(new Brand("Honda", "Honda"));
        lstBrand.add(new Brand("Toyota", "Toyota"));
        lstBrand.add(new Brand("Hyundai", "Hyundai"));
        lstBrand.add(new Brand("Suzuki", "Suzuki"));
        lstBrand.add(new Brand("Mercedes", "Mercedes-Benz"));
        lstBrand.add(new Brand("Audi", "Audi"));
        lstBrand.add(new Brand("BMW", "BMW"));
        BrandAdapter adtBrand = new BrandAdapter(getContext(), lstBrand);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rcvBrand.setLayoutManager(layoutManager);
        rcvBrand.setAdapter(adtBrand);

        rcvCarType = (RecyclerView) getView().findViewById(R.id.rcvCarType);
        List<CarType> lstCarType = new ArrayList<>();
        lstCarType.add(new CarType("type1", "Sedan"));
        lstCarType.add(new CarType("type1", "Hatchback"));
        lstCarType.add(new CarType("type1", "MPV"));
        lstCarType.add(new CarType("type1", "SUV"));
        lstCarType.add(new CarType("type1", "Crossover"));
        lstCarType.add(new CarType("type1", "Coupe"));
        CarTypeAdapter adtCarType = new CarTypeAdapter(getContext(), lstCarType);
        RecyclerView.LayoutManager layoutManagerCarType = new GridLayoutManager(getContext(), 2);
        rcvCarType.setLayoutManager(layoutManagerCarType);
        rcvCarType.setAdapter(adtCarType);
    }

    public OnSearchConditionChangedListener getmListener() {
        return mListener;
    }

    public void setmListener(OnSearchConditionChangedListener mListener) {
        this.mListener = mListener;
    }

    public interface OnSearchConditionChangedListener {
        void OnSearchConditionChanged(Filter filter);
    }
}
