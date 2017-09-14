package com.example.quanlm.cardeal.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlm.cardeal.ActCarDetail;
import com.example.quanlm.cardeal.R;
import com.example.quanlm.cardeal.adapter.CarAdapter;
import com.example.quanlm.cardeal.configure.Constants;
import com.example.quanlm.cardeal.model.Brand;
import com.example.quanlm.cardeal.model.Car;
import com.example.quanlm.cardeal.model.Filter;
import com.example.quanlm.cardeal.util.Util;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchListFragment extends Fragment implements ConditionSearchDialogFragment.OnSearchConditionChangedListener, CarAdapter.OnCarSelectListener {

    private OnFragmentInteractionListener mListener;

    FloatingActionButton btnSearch;

    Filter mFilter;
    RecyclerView rcvCarList;
    CarAdapter adtCar;
    List<Car> lstCar;
    ConditionSearchDialogFragment conditionSearchDialogFragment;

    private FirebaseDatabase mDatabase;


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
        adtCar = new CarAdapter(getContext(), lstCar);
        adtCar.setmCarSelectListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvCarList.setLayoutManager(layoutManager);
        rcvCarList.setAdapter(adtCar);

        DatabaseReference dealTable = mDatabase.getReference(Constants.DEAL_TABLE);
        dealTable.addValueEventListener(new DealTableValueEventListener());
    }

    private boolean isMatchWithFilter(Car objDeal) {
        // Filter = null -> no filter at all
        if (mFilter != null) {
            // TODO: QuanLM implement all other filter condition
            List filteredBrand = Arrays.asList(mFilter.getBrandCode());
            if (Util.isEmptyList(filteredBrand) || filteredBrand.contains(objDeal.getBrand())
//                    && Arrays.asList(mFilter.getBrandCode()).contains(objDeal.getBrand())
//                    && Arrays.asList(mFilter.getCarTypeCode()).contains(objDeal.getCarType())
                    ) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class FilteredEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Car objDeal = dataSnapshot.getValue(Car.class);
            lstCar.add(objDeal);
            adtCar.notifyDataSetChanged();
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
            for (DataSnapshot dealer : dataSnapshot.getChildren()) {
                for (DataSnapshot deal: dealer.getChildren()) {
                    Car objDeal = deal.getValue(Car.class);

                    if (isMatchWithFilter(objDeal)) {
                        lstCar.add(objDeal);
                    }
                }
            }
            adtCar.updateData(lstCar);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
