package com.example.quanlm.cardeal.adapter;

import android.content.Context;
import android.view.View;

import com.example.quanlm.cardeal.model.CarType;

import java.util.List;

/**
 * Created by MyPC on 27/08/2017.
 */

public class SingleChoiceCarTypeAdapter extends CarTypeAdapter {
    CarType selectedCarType;
    public SingleChoiceCarTypeAdapter(Context mContext, List<CarType> lstCarType) {
        super(mContext, lstCarType);
    }

    @Override
    public void onBindViewHolder(final CarTypeViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear all previous selected
                // Check on this item
                clearCondition();
                selectedCarType = lstCarType.get(position);
                selectedCarType.setChecked(true);
                holder.carTypeChecked.setVisibility(View.VISIBLE);
            }
        });
    }

    public CarType getSelectedCarType() {
        return selectedCarType;
    }
}
