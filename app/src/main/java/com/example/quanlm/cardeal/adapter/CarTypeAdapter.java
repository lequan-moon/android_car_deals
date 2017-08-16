package com.example.quanlm.cardeal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quanlm.cardeal.R;
import com.example.quanlm.cardeal.model.CarType;

import java.util.List;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class CarTypeAdapter extends RecyclerView.Adapter<CarTypeAdapter.CarTypeViewHolder>{
    Context mContext;
    List<CarType> lstCarType;

    public CarTypeAdapter(Context mContext, List<CarType> lstCarType) {
        this.mContext = mContext;
        this.lstCarType = lstCarType;
    }

    @Override
    public CarTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.cartype_item_view, parent, false);
        CarTypeViewHolder carTypeViewHolder = new CarTypeViewHolder(itemView);
        return carTypeViewHolder;
    }

    @Override
    public void onBindViewHolder(CarTypeViewHolder holder, int position) {
        CarType carType = lstCarType.get(position);
        holder.txtCarType.setText(carType.getCarTypeName());
    }

    @Override
    public int getItemCount() {
        return lstCarType.size();
    }

    public class CarTypeViewHolder extends RecyclerView.ViewHolder {
        TextView txtCarType;

        public CarTypeViewHolder(View itemView) {
            super(itemView);
            txtCarType = (TextView) itemView.findViewById(R.id.txtCarType);
        }
    }
}
