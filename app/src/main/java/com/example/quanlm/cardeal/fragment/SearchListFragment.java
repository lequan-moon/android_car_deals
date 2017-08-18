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
import com.example.quanlm.cardeal.model.Car;
import com.example.quanlm.cardeal.model.Filter;

import java.util.ArrayList;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    FloatingActionButton btnSearch;

    Filter mFilter;
    RecyclerView rcvCarList;
    private int lastFirstVisiblePosition = 0;
    public static final String CAR_DETAIL_PARAMS = "car_detail_params";
    ConditionSearchDialogFragment conditionSearchDialogFragment;


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
    // TODO: Rename and change types and number of parameters
    public static SearchListFragment newInstance(String param1, String param2) {
        SearchListFragment fragment = new SearchListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        initControls();
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

    private void initControls() {
        btnSearch = (FloatingActionButton) getView().findViewById(R.id.btnSearch);
        rcvCarList = (RecyclerView) getView().findViewById(R.id.rcvCarList);
        List<Car> lstCar = new ArrayList<>();
        lstCar.add(new Car("", "Xe 1", "Description car 1", "100000000"));
        lstCar.add(new Car("", "Xe 2", "Description car 2", "100000000"));
        lstCar.add(new Car("", "Xe 3", "Description car 3", "100000000"));
        lstCar.add(new Car("", "Xe 4", "Description car 4", "100000000"));
        lstCar.add(new Car("", "Xe 5", "Description car 5", "100000000"));
        lstCar.add(new Car("", "Xe 6", "Description car 6", "100000000"));
        lstCar.add(new Car("", "Xe 7", "Description car 7", "100000000"));
        CarAdapter adtCar = new CarAdapter(getContext(), lstCar);
        adtCar.setmCarSelectListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvCarList.setLayoutManager(layoutManager);
        rcvCarList.setAdapter(adtCar);
    }

    @Override
    public void onPause() {
        super.onPause();
        lastFirstVisiblePosition = ((LinearLayoutManager) rcvCarList.getLayoutManager()).findFirstVisibleItemPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LinearLayoutManager) rcvCarList.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
        lastFirstVisiblePosition = 0;
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

        // TODO: Reload list with received filter condition
    }

    @Override
    public void onCarSelect(String carCode) {
        Intent itCarDetail = new Intent(getContext(), ActCarDetail.class);
        Bundle params = new Bundle();
        params.putString("carCode", carCode);
        itCarDetail.putExtra(CAR_DETAIL_PARAMS, params);
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
