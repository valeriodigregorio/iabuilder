package com.swia.iabuilder.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.swia.iabuilder.R;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.views.viewholders.ArmyViewHolder;

public class ArmyAdapter extends CollectionAdapter<Army, ArmyViewHolder> {

    @NonNull
    @Override
    public ArmyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_army, parent, false);
        return new ArmyViewHolder(view);
    }
}
