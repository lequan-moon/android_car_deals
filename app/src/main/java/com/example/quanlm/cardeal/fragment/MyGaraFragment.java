package com.example.quanlm.cardeal.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlm.cardeal.ActCarDetail;
import com.example.quanlm.cardeal.R;
import com.example.quanlm.cardeal.adapter.CarAdapter;
import com.example.quanlm.cardeal.configure.Constants;
import com.example.quanlm.cardeal.model.Car;
import com.example.quanlm.cardeal.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyGaraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyGaraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGaraFragment extends Fragment implements CarAdapter.OnCarSelectListener {

    RecyclerView rcvCarList;
    FirebaseDatabase mDatabase;
    List<Car> lstCar;
    CarAdapter adtCar;

    private OnFragmentInteractionListener mListener;

    public MyGaraFragment() {
        // Required empty public constructor
    }

    public static MyGaraFragment newInstance(String param1, String param2) {
        MyGaraFragment fragment = new MyGaraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_gara, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        rcvCarList = (RecyclerView) getView().findViewById(R.id.rcvCarList);
        lstCar = new ArrayList<>();
        lstCar = getListFavoriteCar();
        adtCar = new CarAdapter(getContext(), lstCar);
        adtCar.setmCarSelectListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvCarList.setLayoutManager(layoutManager);
        rcvCarList.setAdapter(adtCar);
    }

    private List<Car> getListFavoriteCar() {
        final List<Car> lstCar = new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        Set<String> favoriteCars = sharedPreferences.getStringSet(Constants.FAVORITE, new ArraySet<String>());

        for (String favoriteCar :
                favoriteCars) {
            String dealer = Util.getFavoriteCarDealer(favoriteCar);
            String carCode = Util.getFavoriteCarCode(favoriteCar);
            Log.d("MyGara", "dealer: " + dealer);
            Log.d("MyGara", "carCode: " + carCode);
            DatabaseReference carRef = mDatabase.getReference(Constants.DEAL_TABLE).child(dealer).child(carCode);
            carRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Car favoriteCar = dataSnapshot.getValue(Car.class);
                    lstCar.add(favoriteCar);
                    adtCar.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return lstCar;
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
}
