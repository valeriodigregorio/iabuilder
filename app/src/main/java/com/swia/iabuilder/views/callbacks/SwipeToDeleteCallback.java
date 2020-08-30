package com.swia.iabuilder.views.callbacks;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private OnDeleteListener listener = null;

    public SwipeToDeleteCallback() {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null) {
            listener.onDelete(viewHolder);
        }
    }

    public interface OnDeleteListener {
        void onDelete(@NonNull RecyclerView.ViewHolder viewHolder);
    }
}
