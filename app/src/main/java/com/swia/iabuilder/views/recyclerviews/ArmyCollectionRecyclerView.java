package com.swia.iabuilder.views.recyclerviews;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swia.iabuilder.datastores.ArmyStore;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.models.Faction;
import com.swia.iabuilder.views.adapters.ArmyAdapter;
import com.swia.iabuilder.views.callbacks.SwipeToDeleteCallback;
import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArmyCollectionRecyclerView extends RecyclerView {

    private static final ArmyNameComparer COMPARER = new ArmyNameComparer();

    private final SwipeToDeleteCallback callback;
    private Faction faction;
    private ArmyAdapter adapter;

    public ArmyCollectionRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ArmyCollectionRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArmyCollectionRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setLayoutManager(new LinearLayoutManager(context));
        setHasFixedSize(true);

        callback = new SwipeToDeleteCallback();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(callback);
        itemTouchhelper.attachToRecyclerView(this);
    }

    public void setOnItemClickListener(CollectionViewHolder.OnItemClickListener<Army> listener) {
        adapter.setOnItemClickListener(listener);
    }

    public void setOnDeleteListener(SwipeToDeleteCallback.OnDeleteListener listener) {
        callback.setOnDeleteListener(listener);
    }

    public void initialize(Faction faction) {
        this.faction = faction;
        adapter = new ArmyAdapter();
        setAdapter(adapter);
        refresh();
    }

    public int add(Army army) {
        int position = adapter.add(army, COMPARER);
        adapter.notifyItemInserted(position);
        return position;
    }

    public void remove(int position) {
        adapter.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void refresh(int position) {
        adapter.notifyItemChanged(position);
    }

    public void refresh() {
        ArrayList<Army> collection = adapter.getCollection();
        ArrayList<Army> newCollection = new ArrayList<>();
        for (String uuid : ArmyStore.list()) {
            Army army = ArmyStore.load(uuid);
            if (army.getFaction() == faction) {
                newCollection.add(army);
            }
        }
        Collections.sort(newCollection, COMPARER);
        adapter.setCollection(newCollection);
        DiffCallback callback = new DiffCallback(collection, newCollection);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        diffResult.dispatchUpdatesTo(adapter);
    }

    private static class ArmyNameComparer implements Comparator<Army> {
        @Override
        public int compare(Army army1, Army army2) {
            return army1.getName().compareTo(army2.getName());
        }
    }
    
    private static class DiffCallback extends DiffUtil.Callback {

        private final ArrayList<Army> oldCollection;
        private final ArrayList<Army> newCollection;

        public DiffCallback(ArrayList<Army> oldCollection, ArrayList<Army> newCollection) {
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
}
