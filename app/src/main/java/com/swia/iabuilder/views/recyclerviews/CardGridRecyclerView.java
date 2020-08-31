package com.swia.iabuilder.views.recyclerviews;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.datastores.ArmyStore;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.views.adapters.DeckAdapter;
import com.swia.iabuilder.views.callbacks.CardEntryDiffCallback;
import com.swia.iabuilder.views.viewholders.CardViewHolder;
import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class CardGridRecyclerView extends RecyclerView {

    private String uuid = null;
    private CardType cardType = null;

    private OnUpdatedListener listener;

    public CardGridRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public CardGridRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardGridRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected Army getArmy() {
        return ArmyStore.load(uuid);
    }

    protected CardType getCardType() {
        return cardType;
    }

    protected abstract void onArmyLoaded(Army army);

    protected abstract void onArmyModified(Army army);

    public void setColumns(int columns) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), columns);
        setLayoutManager(manager);
    }

    public void initialize(String uuid, CardType cardType) {
        this.uuid = uuid;
        this.cardType = cardType;
        onArmyLoaded(getArmy());
    }

    public void setOnItemClickListener(CollectionViewHolder.OnItemClickListener<CardViewHolder.CardEntry> listener) {
        DeckAdapter adapter = (DeckAdapter) getAdapter();
        if (adapter != null) {
            adapter.setOnItemClickListener(listener);
        }
    }

    protected void updateCollection(ArrayList<CardViewHolder.CardEntry> entries, Comparator<CardViewHolder.CardEntry> comparator) {
        DeckAdapter adapter = (DeckAdapter) getAdapter();
        if (adapter != null) {
            Collections.sort(entries, comparator);
            ArrayList<CardViewHolder.CardEntry> collection = adapter.getCollection();
            adapter.setCollection(entries);
            CardEntryDiffCallback callback = new CardEntryDiffCallback(collection, entries);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
            diffResult.dispatchUpdatesTo(adapter);
            if (listener != null) {
                listener.onUpdated();
            }
        }
    }

    public boolean add(Card card) {
        Army army = getArmy();
        if (army.add(card)) {
            ArmyStore.save(army);
            onArmyModified(army);
            return true;
        }
        return false;
    }

    public void remove(Card card) {
        Army army = getArmy();
        army.remove(card);
        ArmyStore.save(army);
        onArmyModified(army);
    }

    public void refresh() {
        onArmyModified(getArmy());
    }

    public void setOnUpdatedListener(OnUpdatedListener listener) {
        this.listener = listener;
    }

    public interface OnUpdatedListener {
        void onUpdated();
    }

}
