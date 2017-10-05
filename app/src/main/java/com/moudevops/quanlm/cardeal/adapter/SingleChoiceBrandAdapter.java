package com.moudevops.quanlm.cardeal.adapter;

import android.content.Context;
import android.view.View;

import com.moudevops.quanlm.cardeal.model.Brand;

import java.util.List;

/**
 * Created by MyPC on 27/08/2017.
 */

public class SingleChoiceBrandAdapter extends BrandAdapter {
    Brand selectedBrand;
    public SingleChoiceBrandAdapter(Context mContext, List<Brand> mListBrand) {
        super(mContext, mListBrand);
    }

    @Override
    public void onBindViewHolder(final BrandViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear all previous selected
                // Check on this item
                clearCondition();
                selectedBrand = mListBrand.get(position);
                selectedBrand.setSelected(true);
                holder.carChecked.setVisibility(View.VISIBLE);
            }
        });
    }

    public Brand getSelectedBrand() {
        return selectedBrand;
    }
}
