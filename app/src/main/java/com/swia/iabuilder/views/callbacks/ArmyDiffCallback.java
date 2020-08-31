package com.swia.iabuilder.views.callbacks;

import androidx.recyclerview.widget.DiffUtil;

import com.swia.iabuilder.models.Army;

import java.util.ArrayList;

public class ArmyDiffCallback extends DiffUtil.Callback {

    private final ArrayList<Army> oldCollection;
    private final ArrayList<Army> newCollection;

    public ArmyDiffCallback(ArrayList<Army> oldCollection, ArrayList<Army> newCollection) {
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
        Army army1 = oldCollection.get(oldItemPosition);
        Army army2 = newCollection.get(newItemPosition);
        return army1.getUuid().equals(army2.getUuid());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Army army1 = oldCollection.get(oldItemPosition);
        Army army2 = newCollection.get(newItemPosition);
        return army1.getName().equals(army2.getName()) &&
                army1.getDescription().equals(army2.getDescription()) &&
                army1.getVictories() == army2.getVictories() &&
                army1.getDefeats() == army2.getDefeats();
    }
}
