package com.example.quanlm.cardeal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    public void onBindViewHolder(final CarTypeViewHolder holder, int position) {
        final CarType carType = lstCarType.get(position);
        holder.txtCarType.setText(carType.getCarTypeName());
        if (carType.isChecked()) {
            holder.carTypeChecked.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carType.isChecked()) {
                    carType.setChecked(false);
                    holder.carTypeChecked.setVisibility(View.INVISIBLE);
                } else {
                    carType.setChecked(true);
                    holder.carTypeChecked.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstCarType.size();
    }

    public List<CarType> getLstCarType() {
        return lstCarType;
    }

    public class CarTypeViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView txtCarType;
        ImageView carTypeChecked;

        public CarTypeViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtCarType = (TextView) itemView.findViewById(R.id.txtCarType);
            carTypeChecked = (ImageView) itemView.findViewById(R.id.carTypeChecked);
        }
    }
}
