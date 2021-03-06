package com.moudevops.quanlm.cardeal.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.moudevops.quanlm.cardeal.R;
import com.moudevops.quanlm.cardeal.model.Car;
import com.moudevops.quanlm.cardeal.model.ImageEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    Context mContext;
    List<Car> lstCar;
    OnCarSelectListener mCarSelectListener;
    FirebaseStorage mStorage;

    public CarAdapter(Context mContext, List<Car> lstCar) {
        this.mContext = mContext;
        this.lstCar = lstCar;
        mStorage = FirebaseStorage.getInstance();
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.car_item, parent, false);
        CarViewHolder carViewHolder = new CarViewHolder(itemView);
        return carViewHolder;
    }

    @Override
    public void onBindViewHolder(final CarViewHolder holder, int position) {
        final Car car = lstCar.get(position);
        holder.txtCarName.setText(car.getDealName());
//        holder.txtDescription.setText(car.getDescription());
        holder.carItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCarSelectListener != null) {
                    mCarSelectListener.onCarSelect(car);
                }
            }
        });
        if (car.getImages() != null && car.getImages().size() > 0) {
            String topImageUri = car.getImages().get(0);
             Glide.with(mContext)
                     .load(topImageUri)
                     .fitCenter()
                     .into(holder.imgCarThumb);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.no_image_car)
                    .fitCenter()
                    .into(holder.imgCarThumb);
        }
    }

    @Override
    public int getItemCount() {
        return lstCar.size();
    }

    public void updateData(List<Car> lstCar) {
        this.lstCar = lstCar;
        notifyDataSetChanged();
    }

    public void notifyAddData(Car newDeal) {
        this.lstCar.add(newDeal);
        notifyItemInserted(getItemCount());
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCarThumb;
        TextView txtCarName;
        TextView txtDescription;
        View carItemView;
        SliderLayout slider;

        public CarViewHolder(View itemView) {
            super(itemView);
            imgCarThumb = (ImageView) itemView.findViewById(R.id.imgCarThumb);
            txtCarName = (TextView) itemView.findViewById(R.id.txtCarName);
            slider = (SliderLayout) itemView.findViewById(R.id.slider);
            this.carItemView = itemView;
        }
    }

    public OnCarSelectListener getmCarSelectListener() {
        return mCarSelectListener;
    }

    public void setmCarSelectListener(OnCarSelectListener mCarSelectListener) {
        this.mCarSelectListener = mCarSelectListener;
    }

    public interface OnCarSelectListener {
        void onCarSelect(Car car);
    }
}
