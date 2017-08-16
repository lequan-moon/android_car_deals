package com.example.quanlm.cardeal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quanlm.cardeal.R;
import com.example.quanlm.cardeal.model.Brand;

import java.util.List;

/**
 * Created by QuanLM on 8/16/2017.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {
    Context mContext;
    List<Brand> mListBrand;

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
    public void onBindViewHolder(BrandViewHolder holder, int position) {
        Brand brand = mListBrand.get(position);
        holder.txtBrandName.setText(brand.getBrandName());
    }

    @Override
    public int getItemCount() {
        return mListBrand.size();
    }

    public class BrandViewHolder extends RecyclerView.ViewHolder{
        TextView txtBrandName;

        public BrandViewHolder(View itemView) {
            super(itemView);
            txtBrandName = (TextView) itemView.findViewById(R.id.txtBrandName);
        }
    }

}
