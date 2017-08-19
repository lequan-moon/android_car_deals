package com.example.quanlm.cardeal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlm.cardeal.R;
import com.example.quanlm.cardeal.model.Brand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {
    Context mContext;
    List<Brand> mListBrand;
    List<BrandViewHolder> mListViewHolder;

    public BrandAdapter(Context mContext, List<Brand> mListBrand) {
        this.mContext = mContext;
        this.mListBrand = mListBrand;
    }

    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View brandItemView = LayoutInflater.from(mContext).inflate(R.layout.brand_item, parent, false);
        BrandViewHolder viewHolder = new BrandViewHolder(brandItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BrandViewHolder holder, int position) {
        if (mListViewHolder == null) {
            mListViewHolder = new ArrayList<>();
        }
        if (!mListViewHolder.contains(holder)) {
            mListViewHolder.add(holder);
        }

        final Brand brand = mListBrand.get(position);
        holder.txtBrandName.setText(brand.getBrandName());
        if (brand.isSelected()) {
            holder.carChecked.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brand.isSelected()) {
                    brand.setSelected(false);
                    holder.carChecked.setVisibility(View.INVISIBLE);
                } else {
                    brand.setSelected(true);
                    holder.carChecked.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListBrand.size();
    }

    public List<Brand> getmListBrand() {
        return mListBrand;
    }

    public class BrandViewHolder extends RecyclerView.ViewHolder {
        TextView txtBrandName;
        ImageView carChecked;
        View itemView;

        public BrandViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtBrandName = (TextView) itemView.findViewById(R.id.txtBrandName);
            carChecked = (ImageView) itemView.findViewById(R.id.carChecked);
        }
    }

    public void clearCondition() {
        // Reset checked mark
        if (mListViewHolder != null) {
            for (int i = 0; i < mListViewHolder.size(); i++) {
                mListViewHolder.get(i).carChecked.setVisibility(View.INVISIBLE);
            }
        }

        // Reset Brand model's checked state to false
        if (mListBrand != null) {
            for (int i = 0; i < mListBrand.size(); i++) {
                mListBrand.get(i).setSelected(false);
            }
        }
    }
}
