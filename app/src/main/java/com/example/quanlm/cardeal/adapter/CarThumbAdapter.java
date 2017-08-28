package com.example.quanlm.cardeal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.quanlm.cardeal.R;

import java.util.List;

/**
 * Created by MyPC on 27/08/2017.
 */

public class CarThumbAdapter extends RecyclerView.Adapter<CarThumbAdapter.CarThumbnail> {
    Context mContext;
    List<Integer> lstImages;

    public CarThumbAdapter(Context mContext, List<Integer> lstImages) {
        this.mContext = mContext;
        this.lstImages = lstImages;
    }

    @Override
    public CarThumbnail onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.car_thumbnail_item, parent, false);
        CarThumbnail carThumbnail = new CarThumbnail(itemView);
        return carThumbnail;
    }

    @Override
    public void onBindViewHolder(CarThumbnail holder, int position) {
        if (position == 0) {
            holder.imgCarThumb.setImageDrawable(mContext.getDrawable(R.drawable.ic_add_black_48dp));
        } else {
            holder.imgCarThumb.setImageDrawable(mContext.getDrawable(lstImages.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return lstImages.size();
    }

    public class CarThumbnail extends RecyclerView.ViewHolder {
        ImageView imgCarThumb;

        public CarThumbnail(View itemView) {
            super(itemView);
            imgCarThumb = (ImageView) itemView.findViewById(R.id.imgCarThumb);
            imgCarThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = CarThumbnail.this.getAdapterPosition();
                    if (position == 0) {
                        // Add button
                        Log.d("CarThumbnail", "onClick: Click on add button");
                    } else {
                        // Click on image
                        Log.d("CarThumbnail", "onClick: Click on image");
                    }
                }
            });
        }
    }
}
