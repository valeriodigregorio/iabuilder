package com.swia.iabuilder.views.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class CollectionViewHolder<T> extends RecyclerView.ViewHolder {

    private T item = null;

    public CollectionViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public T getItem() {
        return item;
    }

    public void bind(T item) {
        this.item = item;
        onBind(item);
    }

    public abstract void onBind(T item);

    public interface OnItemClickListener<T> {
        void onItemClicked(CollectionViewHolder<T> item);

        boolean onItemLongClicked(CollectionViewHolder<T> item);
    }
}
