package com.swia.iabuilder.views.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class CollectionAdapter<T, VH extends CollectionViewHolder<T>> extends RecyclerView.Adapter<VH> implements View.OnClickListener, View.OnLongClickListener {

    protected ArrayList<T> collection;
    private CollectionViewHolder.OnItemClickListener<T> listener;
    private int count;

    public CollectionAdapter() {
        count = 0;
        collection = new ArrayList<>();
        listener = null;
    }

    public void setOnItemClickListener(CollectionViewHolder.OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    public void clear() {
        count = 0;
        collection = new ArrayList<>();
    }

    public int add(T item) {
        int position = count;
        collection.add(item);
        count++;
        return position;
    }

    public int add(T item, Comparator<T> comparator) {
        collection.add(item);
        count++;
        Collections.sort(collection, comparator);
        return collection.indexOf(item);
    }

    public void remove(int position) {
        collection.remove(position);
        count--;
    }

    public int remove(T item) {
        int position = getPosition(item);
        remove(position);
        return position;
    }

    public int getPosition(T item) {
        return collection.indexOf(item);
    }

    public ArrayList<T> getCollection() {
        return collection;
    }

    public void setCollection(ArrayList<T> items) {
        clear();
        for (T item : items) {
            add(item);
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T item = collection.get(position);
        holder.bind(item);
        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClicked((VH) view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        boolean consumed = false;
        if (listener != null) {
            consumed = listener.onItemLongClicked((VH) view.getTag());
        }
        return consumed;
    }
}
