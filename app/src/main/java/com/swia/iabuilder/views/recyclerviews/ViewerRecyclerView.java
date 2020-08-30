package com.swia.iabuilder.views.recyclerviews;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.swia.datasets.cards.Card;
import com.swia.iabuilder.views.adapters.DeckAdapter;
import com.swia.iabuilder.views.viewholders.CardViewHolder;

public class ViewerRecyclerView extends RecyclerView {

    public ViewerRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ViewerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        setLayoutManager(manager);
        setAdapter(new DeckAdapter(1));
    }

    public void add(Card card) {
        if (card == null) {
            throw new NullPointerException();
        }
        DeckAdapter adapter = (DeckAdapter) getAdapter();
        if (adapter != null) {
            int position = adapter.add(new CardViewHolder.CardEntry(card, true, false));
            adapter.notifyItemInserted(position);
        }
    }
}
