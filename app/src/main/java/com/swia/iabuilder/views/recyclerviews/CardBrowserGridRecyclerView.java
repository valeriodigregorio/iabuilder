package com.swia.iabuilder.views.recyclerviews;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swia.iabuilder.views.adapters.DeckAdapter;
import com.swia.iabuilder.views.callbacks.CardEntryDiffCallback;
import com.swia.iabuilder.views.viewholders.CardViewHolder;
import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

import java.util.ArrayList;

public class CardBrowserGridRecyclerView extends RecyclerView {

    public CardBrowserGridRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public CardBrowserGridRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardBrowserGridRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setColumns(int columns) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), columns);
        setLayoutManager(manager);
    }

    public void setOnItemClickListener(CollectionViewHolder.OnItemClickListener<CardViewHolder.CardEntry> listener) {
        DeckAdapter adapter = (DeckAdapter) getAdapter();
        if (adapter != null) {
            adapter.setOnItemClickListener(listener);
        }
    }

    public void update() {
        DeckAdapter adapter = (DeckAdapter) getAdapter();
        if (adapter != null) {
            update(adapter.getCollection());
        }
    }

    public void update(ArrayList<CardViewHolder.CardEntry> entries) {
        DeckAdapter adapter = (DeckAdapter) getAdapter();
        if (adapter != null) {
            ArrayList<CardViewHolder.CardEntry> collection = adapter.getCollection();
            adapter.setCollection(entries);
            CardEntryDiffCallback callback = new CardEntryDiffCallback(collection, entries);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
            diffResult.dispatchUpdatesTo(adapter);
        }
    }
}
