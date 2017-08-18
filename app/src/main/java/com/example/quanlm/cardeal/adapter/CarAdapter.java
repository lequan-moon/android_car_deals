package com.example.quanlm.cardeal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlm.cardeal.R;
import com.example.quanlm.cardeal.model.Car;

import java.util.List;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    Context mContext;
    List<Car> lstCar;
    OnCarSelectListener mCarSelectListener;

    public CarAdapter(Context mContext, List<Car> lstCar) {
        this.mContext = mContext;
        this.lstCar = lstCar;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.car_item, parent, false);
        CarViewHolder carViewHolder = new CarViewHolder(itemView);
        return carViewHolder;
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        final Car car = lstCar.get(position);
        // TODO: Get and append car's thumbnail
        holder.txtCarName.setText(car.getName());
        holder.txtDescription.setText(car.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCarSelectListener != null){
                    mCarSelectListener.onCarSelect(car.getCode());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstCar.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCarThumb;
        TextView txtCarName;
        TextView txtDescription;
        View itemView;

        public CarViewHolder(View itemView) {
            super(itemView);
            imgCarThumb = (ImageView) itemView.findViewById(R.id.imgCarThumb);
            txtCarName = (TextView) itemView.findViewById(R.id.txtCarName);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            this.itemView = itemView;
        }
    }

    public OnCarSelectListener getmCarSelectListener() {
        return mCarSelectListener;
    }

    public void setmCarSelectListener(OnCarSelectListener mCarSelectListener) {
        this.mCarSelectListener = mCarSelectListener;
    }

    public interface OnCarSelectListener {
        void onCarSelect(String carCode);
    }
}
