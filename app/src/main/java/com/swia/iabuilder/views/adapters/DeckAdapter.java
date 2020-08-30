package com.swia.iabuilder.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.swia.iabuilder.R;
import com.swia.iabuilder.views.viewholders.CardViewHolder;

public class DeckAdapter extends CollectionAdapter<CardViewHolder.CardEntry, CardViewHolder> {

    private final int ratio;

    public DeckAdapter(int ratio) {
        this.ratio = ratio;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_card, parent, false);
        return new CardViewHolder(view, ratio);
    }
}
