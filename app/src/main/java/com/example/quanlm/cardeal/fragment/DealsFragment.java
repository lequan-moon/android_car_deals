package com.example.quanlm.cardeal.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quanlm.cardeal.ActCarDetail;
import com.example.quanlm.cardeal.R;
import com.example.quanlm.cardeal.adapter.CarAdapter;
import com.example.quanlm.cardeal.configure.Constants;
import com.example.quanlm.cardeal.model.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DealsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DealsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DealsFragment extends Fragment implements CarAdapter.OnCarSelectListener{

    RecyclerView rcvMyDeals;

    private OnFragmentInteractionListener mListener;

    public DealsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DealsFragment.
     */
    public static DealsFragment newInstance(String param1, String param2) {
        DealsFragment fragment = new DealsFragment();
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
        return inflater.inflate(R.layout.fragment_deals, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initControls(view);
        initEvents(view);
    }

    private void initControls(View view) {
        rcvMyDeals = (RecyclerView) view.findViewById(R.id.rcvMyDeals);

        // TODO: QuanLM get my deals from API or something
        List<Car> lstCar = new ArrayList<>();
        lstCar.add(new Car("1", "Xe 1", "Description car 1", "100000000"));
        lstCar.add(new Car("2", "Xe 2", "Description car 2", "100000000"));
        CarAdapter adtCar = new CarAdapter(getContext(), lstCar);
        adtCar.setmCarSelectListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvMyDeals.setLayoutManager(layoutManager);
        rcvMyDeals.setAdapter(adtCar);
    }

    private void initEvents(View view) {
        view.findViewById(R.id.btnAddDeal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: QuanLM add deal action
            }
        });
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
