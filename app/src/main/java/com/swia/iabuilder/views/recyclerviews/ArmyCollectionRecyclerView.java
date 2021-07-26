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
import com.swia.iabuilder.views.callbacks.ArmyDiffCallback;
import com.swia.iabuilder.views.callbacks.SwipeToDeleteCallback;
import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArmyCollectionRecyclerView extends RecyclerView {

    private static final ArmyNameComparator COMPARER = new ArmyNameComparator();

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
        ArmyDiffCallback callback = new ArmyDiffCallback(collection, newCollection);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        diffResult.dispatchUpdatesTo(adapter);
    }

    private static class ArmyNameComparator implements Comparator<Army> {
        @Override
        public int compare(Army army1, Army army2) {
            return army1.getName().compareTo(army2.getName());
        }
    }

}
