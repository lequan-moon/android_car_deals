package com.moudevops.quanlm.cardeal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moudevops.quanlm.cardeal.R;
import com.moudevops.quanlm.cardeal.adapter.BrandAdapter;
import com.moudevops.quanlm.cardeal.adapter.CarTypeAdapter;
import com.moudevops.quanlm.cardeal.configure.Constants;
import com.moudevops.quanlm.cardeal.model.Brand;
import com.moudevops.quanlm.cardeal.model.CarType;
import com.moudevops.quanlm.cardeal.model.Filter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.apptik.widget.MultiSlider;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class ConditionSearchDialogFragment extends DialogFragment {
    RecyclerView rcvBrand;
    RecyclerView rcvCarType;
    MultiSlider sliderPrice;
    TextView txtPriceStart;
    TextView txtPriceEnd;

    BrandAdapter adtBrand;
    List<Brand> lstBrand;

    CarTypeAdapter adtCarType;
    List<CarType> lstCarType;

    MultiSlider sliderManufacturedYear;
    TextView txtManufacturedStart;
    TextView txtManufacturedEnd;

    OnSearchConditionChangedListener mListener;
    Filter mFilter;

    FirebaseDatabase mDatabase;

    public ConditionSearchDialogFragment() {
    }

    public static ConditionSearchDialogFragment newInstance(Filter filter) {
        ConditionSearchDialogFragment fragment = new ConditionSearchDialogFragment();
        fragment.mFilter = filter;
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
        
        mDatabase = FirebaseDatabase.getInstance();
        initControls();
        initEvents();

        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void initEvents() {
        getView().findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] selectedBrandCode;
                String[] selectedCarTypeCode;
                String selectedPriceStart = "";
                String selectedPriceEnd = "";
                String selectedManufacturedYearStart = "";
                String selectedManufacturedYearEnd = "";

                // Process selected brands
                List<Brand> listFilterBrand = ((BrandAdapter) rcvBrand.getAdapter()).getmListBrand();
                selectedBrandCode = new String[listFilterBrand.size()];
                for (int i = 0; i < listFilterBrand.size(); i++) {
                    Brand iBrand = listFilterBrand.get(i);
                    if (listFilterBrand.get(i).isSelected()) {
                        selectedBrandCode[i] = iBrand.getBrandCode();
                    }
                }

                // Process selected car type
                List<CarType> listFilterCarType = ((CarTypeAdapter) rcvCarType.getAdapter()).getLstCarType();
                selectedCarTypeCode = new String[listFilterCarType.size()];
                for (int i = 0; i < listFilterCarType.size(); i++) {
                    CarType iCarType = listFilterCarType.get(i);
                    if (iCarType.isChecked()) {
                        selectedCarTypeCode[i] = iCarType.getCarTypeCode();
                    }
                }

                // Process price
                selectedPriceStart = String.valueOf(sliderPrice.getThumb(0).getValue());
                selectedPriceEnd = String.valueOf(sliderPrice.getThumb(1).getValue());

                // Process manufactured date
                selectedManufacturedYearStart = String.valueOf(txtManufacturedStart.getText());
                selectedManufacturedYearEnd = String.valueOf(txtManufacturedEnd.getText());

                Filter filter = new Filter(selectedBrandCode,
                        selectedCarTypeCode,
                        selectedPriceStart,
                        selectedPriceEnd,
                        selectedManufacturedYearStart,
                        selectedManufacturedYearEnd);

                mListener.OnSearchConditionChanged(filter);
            }
        });

        getView().findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset checked brand list
                ((BrandAdapter) rcvBrand.getAdapter()).clearCondition();

                // Reset checked car type
                ((CarTypeAdapter) rcvCarType.getAdapter()).clearCondition();

                // Reset price slider
                sliderPrice.getThumb(0).setValue(Constants.FILTER_PRICE_MIN);
                sliderPrice.getThumb(1).setValue(Constants.FILTER_PRICE_MAX);

                // Reset manufactured year slider
                sliderManufacturedYear.getThumb(0).setValue(Constants.FILTER_YEAR_MIN);
                sliderManufacturedYear.getThumb(1).setValue(Constants.FILTER_YEAR_MAX);
            }
        });
    }

    private void initControls() {
        // Setup brand condition
        rcvBrand = (RecyclerView) getView().findViewById(R.id.rcvBrand);
        lstBrand = getListBrandCondition();
        adtBrand = new BrandAdapter(getContext(), lstBrand);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rcvBrand.setLayoutManager(layoutManager);
        rcvBrand.setAdapter(adtBrand);

        // Setup cartype condition
        rcvCarType = (RecyclerView) getView().findViewById(R.id.rcvCarType);
        lstCarType = getListCarTypeCondition();
        adtCarType = new CarTypeAdapter(getContext(), lstCarType);
        RecyclerView.LayoutManager layoutManagerCarType = new GridLayoutManager(getContext(), 3);
        rcvCarType.setLayoutManager(layoutManagerCarType);
        rcvCarType.setAdapter(adtCarType);

        // Setup price slider
        sliderPrice = (MultiSlider) getView().findViewById(R.id.sliderPrice);
        txtPriceStart = (TextView) getView().findViewById(R.id.txtPriceStart);
        txtPriceEnd = (TextView) getView().findViewById(R.id.txtPriceEnd);
        sliderPrice.setMin(Constants.FILTER_PRICE_MIN);
        sliderPrice.setMax(Constants.FILTER_PRICE_MAX);
        sliderPrice.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    txtPriceStart.setText(generatePriceString(value));
                } else {
                    txtPriceEnd.setText(generatePriceString(value));
                }
            }
        });
        sliderPrice.setStep(100);
        if (mFilter != null) {
            String priceStart = mFilter.getPriceStart();
            String priceEnd = mFilter.getPriceEnd();
            txtPriceStart.setText(priceStart);
            txtPriceEnd.setText(priceEnd);
            sliderPrice.getThumb(0).setValue(Integer.valueOf(priceStart));
            sliderPrice.getThumb(1).setValue(Integer.valueOf(priceEnd));
        } else {
            sliderPrice.getThumb(0).setValue(Constants.FILTER_PRICE_MIN);
            sliderPrice.getThumb(1).setValue(Constants.FILTER_PRICE_MAX);
        }

        // Setup manufactured date slider
        // TODO: QuanLM Fixbug and show this field
        // Temporary set GONE in view for this field
        sliderManufacturedYear = (MultiSlider) getView().findViewById(R.id.sliderManufacturedYear);
        txtManufacturedStart = (TextView) getView().findViewById(R.id.txtManufacturedStart);
        txtManufacturedEnd = (TextView) getView().findViewById(R.id.txtManufacturedEnd);
        sliderManufacturedYear.setMin(Constants.FILTER_YEAR_MIN);
        sliderManufacturedYear.setMax(Constants.FILTER_YEAR_MAX); // TODO: QuanLM replace with current year
        sliderManufacturedYear.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    txtManufacturedStart.setText(String.valueOf(value));
                } else {
                    txtManufacturedEnd.setText(String.valueOf(value));
                }
            }
        });
        sliderManufacturedYear.setStep(1);
        if (mFilter != null) {
            String manufacturedYearStart = mFilter.getManufacturedYearStart();
            String manufacturedYearEnd = mFilter.getManufacturedYearEnd();
            txtManufacturedStart.setText(manufacturedYearStart);
            txtManufacturedEnd.setText(manufacturedYearEnd);
            sliderManufacturedYear.getThumb(0).setValue(Integer.valueOf(manufacturedYearStart));
            sliderManufacturedYear.getThumb(1).setValue(Integer.valueOf(manufacturedYearEnd));
        } else {
            sliderManufacturedYear.getThumb(0).setValue(Constants.FILTER_YEAR_MIN);
            sliderManufacturedYear.getThumb(1).setValue(Constants.FILTER_YEAR_MAX);
        }

    }

    private String generatePriceString(int price) {
        if (price == 0) {
            return "0";
        }
        if (price == Constants.FILTER_PRICE_MAX) {
            return "no limit";
        } else {
            return String.valueOf(price) + " " + getString(R.string.price_unit);
        }
    }

    private List<Brand> getListBrandCondition() {
        lstBrand = new ArrayList<>();
        DatabaseReference brandTable = mDatabase.getReference(Constants.BRAND_TABLE);
        brandTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot brand :
                        dataSnapshot.getChildren()) {
                    Brand objBrand = brand.getValue(Brand.class);
                    lstBrand.add(objBrand);
                }
                adtBrand.notifyDataSetChanged();

                if (mFilter != null) {
                    String[] lstCheckedbrand = mFilter.getBrandCode();
                    for (int idxCheckedBrand = 0; idxCheckedBrand < lstCheckedbrand.length; idxCheckedBrand++) {
                        for (int idxBrand = 0; idxBrand < lstBrand.size(); idxBrand++) {
                            Brand brand = lstBrand.get(idxBrand);
                            String checkedBrand = lstCheckedbrand[idxCheckedBrand];
                            if (brand.getBrandCode().equals(checkedBrand)) {
                                brand.setSelected(true);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return lstBrand;
    }

    private List<CarType> getListCarTypeCondition() {
        lstCarType = new ArrayList<>();
        DatabaseReference brandTable = mDatabase.getReference(Constants.CAR_TYPE_TABLE);
        brandTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot carType :
                        dataSnapshot.getChildren()) {
                    CarType objCarType = carType.getValue(CarType.class);
                    lstCarType.add(objCarType);
                }
                adtCarType.notifyDataSetChanged();

                if (mFilter != null) {
                    String[] lstCheckedCarType = mFilter.getCarTypeCode();
                    for (int idxCheckedCarType = 0; idxCheckedCarType < lstCheckedCarType.length; idxCheckedCarType++) {
                        for (int idxCarType = 0; idxCarType < lstCarType.size(); idxCarType++) {
                            CarType carType = lstCarType.get(idxCarType);
                            String checkedCarType = lstCheckedCarType[idxCheckedCarType];
                            if (carType.getCarTypeCode().equals(checkedCarType)) {
                                carType.setChecked(true);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return lstCarType;
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
