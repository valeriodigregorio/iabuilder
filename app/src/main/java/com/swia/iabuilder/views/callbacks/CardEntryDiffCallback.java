package com.swia.iabuilder.views.callbacks;

import androidx.recyclerview.widget.DiffUtil;

import com.swia.iabuilder.views.viewholders.CardViewHolder;

import java.util.ArrayList;

public class CardEntryDiffCallback extends DiffUtil.Callback {

    private final ArrayList<CardViewHolder.CardEntry> oldCollection;
    private final ArrayList<CardViewHolder.CardEntry> newCollection;

    public CardEntryDiffCallback(ArrayList<CardViewHolder.CardEntry> oldCollection, ArrayList<CardViewHolder.CardEntry> newCollection) {
        this.oldCollection = oldCollection;
        this.newCollection = newCollection;
    }

    @Override
    public int getOldListSize() {
        return oldCollection.size();
    }

    @Override
    public int getNewListSize() {
        return newCollection.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        CardViewHolder.CardEntry entry1 = oldCollection.get(oldItemPosition);
        CardViewHolder.CardEntry entry2 = newCollection.get(newItemPosition);
        return entry1.getCard().getCardType() == entry2.getCard().getCardType() &&
                entry1.getCard().getId() == entry2.getCard().getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        CardViewHolder.CardEntry entry1 = oldCollection.get(oldItemPosition);
        CardViewHolder.CardEntry entry2 = newCollection.get(newItemPosition);
        return entry1.equals(entry2);
    }
}
