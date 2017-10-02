package com.example.quanlm.cardeal.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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

import com.example.quanlm.cardeal.ActCarDetail;
import com.example.quanlm.cardeal.FetchImageService;
import com.example.quanlm.cardeal.R;
import com.example.quanlm.cardeal.adapter.CarAdapter;
import com.example.quanlm.cardeal.configure.Constants;
import com.example.quanlm.cardeal.model.Car;
import com.example.quanlm.cardeal.model.Filter;
import com.example.quanlm.cardeal.util.Util;
import com.google.firebase.database.ChildEventListener;
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
        View.OnScrollChangeListener{

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
    FetchImageService mFetchImageService;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(FetchImageService.UPDATE_IMAGE_ACTION)){
                Bundle args = intent.getBundleExtra(Constants.IMAGE_PACKS);
                int position = args.getInt(Constants.POSITION);
                String imageUri = args.getString(Constants.IMAGE_URI);
                List<Car> lstCar = adtCar.getListdata();
                Car car = lstCar.get(position);
                if (car.getImageUris() == null) {
                    car.setImageUris(new ArrayList<String>());
                }
                car.getImageUris().add(imageUri);
//                Log.d("fetchImage", "position: " + position);
//                Log.d("fetchImage", "onReceive: " + imageUri);
//                Log.d("fetchImage", "lstCar size: " + lstCar.size());
                adtCar.updateData(lstCar);
            }
        }
    };

    IntentFilter intentFilter;

    public SearchListFragment() {
        // Required empty public constructor
    }

    public static SearchListFragment newInstance(String param1, FetchImageService fetchImageService) {
        SearchListFragment fragment = new SearchListFragment();
        fragment.setmFetchImageService(fetchImageService);
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
        resgisterBroadcast();
        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

    private void resgisterBroadcast(){
        intentFilter = new IntentFilter(FetchImageService.UPDATE_IMAGE_ACTION);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
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
        adtCar.setmFetchImageService(mFetchImageService);

        rcvCarList.setLayoutManager(layoutManager);
        rcvCarList.setAdapter(adtCar);
        rcvCarList.setOnScrollChangeListener(this);

        dealTable = mDatabase.getReference(Constants.DEAL_TABLE);
        dealTable.orderByKey()
                .limitToFirst(Constants.ITEM_PER_PAGE)
                .addValueEventListener(new DealTableValueEventListener());
//                .addChildEventListener(new DealTableChildEventListener());
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
//                    && Arrays.asList(mFilter.getCarTypeCode()).contains(objDeal.getCarType())
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
                    .limitToFirst(Constants.ITEM_PER_PAGE)
                    .startAt(lastRecordKey)
//                    .addChildEventListener(new DealTableChildEventListener());
                    .addValueEventListener(new DealTableValueEventListener());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    public void setmFetchImageService(FetchImageService mFetchImageService) {
        this.mFetchImageService = mFetchImageService;
    }

    private class DealTableChildEventListener implements ChildEventListener{

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Car objDeal = dataSnapshot.getValue(Car.class);

            if (isMatchWithFilter(objDeal)) {
                lstCar.add(objDeal);
                lastRecordKey = dataSnapshot.getKey();
            }
            adtCar.updateData(lstCar);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private class DealTableValueEventListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            isPopulatingData = true;
            for (DataSnapshot deal : dataSnapshot.getChildren()) {
//                for (DataSnapshot deal: dealer.getChildren()) {
                    Car objDeal = deal.getValue(Car.class);

//                    mFetchImageService.fetchImageForAdapter(lstCar.size(), objDeal.getImages().get(0));
                    if (isMatchWithFilter(objDeal)) {
                        lstCar.add(objDeal);
                        lastRecordKey = deal.getKey();
                    }
//                }
            }
            adtCar.updateData(lstCar);
            Log.d("LISTDATA", "lstCar count: " + lstCar.size());
            isPopulatingData = false;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("DB", databaseError.getDetails());
        }
    }
}
