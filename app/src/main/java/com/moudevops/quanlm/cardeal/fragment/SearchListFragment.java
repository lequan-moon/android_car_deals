package com.moudevops.quanlm.cardeal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moudevops.quanlm.cardeal.ActCarDetail;
import com.moudevops.quanlm.cardeal.R;
import com.moudevops.quanlm.cardeal.adapter.CarAdapter;
import com.moudevops.quanlm.cardeal.configure.Constants;
import com.moudevops.quanlm.cardeal.model.Car;
import com.moudevops.quanlm.cardeal.model.Filter;
import com.moudevops.quanlm.cardeal.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchListFragment extends Fragment implements
        ConditionSearchDialogFragment.OnSearchConditionChangedListener,
        CarAdapter.OnCarSelectListener,
        View.OnScrollChangeListener {

    FloatingActionButton btnSearch;

    Filter mFilter;
    RecyclerView rcvCarList;
    CarAdapter adtCar;
    List<Car> lstCar;
    ConditionSearchDialogFragment conditionSearchDialogFragment;

    private FirebaseDatabase mDatabase;
    DatabaseReference dealTable;
    String lastRecordKey;
    private boolean isPopulatingData;


    public SearchListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchListFragment.
     */
    public static SearchListFragment newInstance(String param1, String param2) {
        SearchListFragment fragment = new SearchListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControls(view);
        initEvents();
    }

    private void initEvents() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getChildFragmentManager();
                if (conditionSearchDialogFragment == null) {
                    conditionSearchDialogFragment = ConditionSearchDialogFragment.newInstance(mFilter);
                }
                conditionSearchDialogFragment.setmListener(SearchListFragment.this);
                conditionSearchDialogFragment.mFilter = mFilter;
                conditionSearchDialogFragment.show(fm, "ConditionSearch");

            }
        });
    }

    private void initControls(View view) {

        mDatabase = FirebaseDatabase.getInstance();

        btnSearch = (FloatingActionButton) view.findViewById(R.id.btnSearch);

        rcvCarList = (RecyclerView) view.findViewById(R.id.rcvCarList);
        lstCar = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        adtCar = new CarAdapter(getContext(), lstCar);
        adtCar.setmCarSelectListener(this);

        rcvCarList.setLayoutManager(layoutManager);
        rcvCarList.setAdapter(adtCar);
        rcvCarList.setOnScrollChangeListener(this);

        dealTable = mDatabase.getReference(Constants.DEAL_TABLE);
        dealTable.orderByKey()
                // Get Constants.ITEM_PER_PAGE + 1 item
                // to keep the last record as key to load more later
                .limitToFirst(Constants.ITEM_PER_PAGE + 1)
                .addValueEventListener(new DealTableValueEventListener());
    }

    private boolean isMatchWithFilter(Car objDeal) {
        // Filter = null -> no filter at all
        if (mFilter != null) {
            List filteredBrand = Arrays.asList(mFilter.getBrandCode());
            List filteredCarType = Arrays.asList(mFilter.getCarTypeCode());
            double priceStart = mFilter.getPriceStartValue();
            double priceEnd = mFilter.getPriceEndValue();
            double carPrice;
            try {
                carPrice = Double.valueOf(objDeal.getPrice());
            } catch (NumberFormatException ex) {
                // If exception then not defined price
                // Ex: "contact for detail", "thoa thuan", ...
                carPrice = 0;
            }

            if ((Util.isEmptyList(filteredBrand) || filteredBrand.contains(objDeal.getBrand()))
                    && (Util.isEmptyList(filteredCarType) || filteredCarType.contains(objDeal.getCarType()))
                    && (priceStart <= carPrice && carPrice <= priceEnd)
                    ) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void OnSearchConditionChanged(Filter filter) {
        conditionSearchDialogFragment.dismiss();
        mFilter = filter;

        lstCar.clear();
        DatabaseReference dealTable = mDatabase.getReference(Constants.DEAL_TABLE);
        dealTable.addValueEventListener(new DealTableValueEventListener());
    }

    @Override
    public void onCarSelect(Car car) {
        Intent itCarDetail = new Intent(getContext(), ActCarDetail.class);
        Bundle params = new Bundle();
        params.putSerializable("selected_car", car);
        itCarDetail.putExtra(Constants.CAR_DETAIL_PARAMS, params);
        startActivity(itCarDetail);
        getActivity().overridePendingTransition(R.anim.right_out, R.anim.left_in);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) rcvCarList.getLayoutManager();
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
            // End of list
            // Load more item
            if (isPopulatingData) {
                return;
            }
            dealTable.orderByKey()
                    .limitToFirst(Constants.ITEM_PER_PAGE + 1)
                    .startAt(lastRecordKey)
                    .addValueEventListener(new DealTableValueEventListener());
            isPopulatingData = true;
        }
    }

    public void setPopulatingState(boolean state) {
        isPopulatingData = state;
    }

    private class DealTableValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            int dealCnt = 0;
            for (DataSnapshot deal : dataSnapshot.getChildren()) {
                Car objDeal = deal.getValue(Car.class);
                if (isMatchWithFilter(objDeal)) {
                    if (dealCnt < dataSnapshot.getChildrenCount()) {
                        lstCar.add(objDeal);
                    }
                    lastRecordKey = deal.getKey();
                }
            }
            dealCnt++;
            adtCar.updateData(lstCar);
            Log.d("LISTDATA", "lstCar count: " + lstCar.size());
            setPopulatingState(false);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("DB", databaseError.getDetails());
        }
    }
}
