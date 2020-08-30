package com.swia.iabuilder.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.swia.iabuilder.R;
import com.swia.iabuilder.activities.ArmyActivity;
import com.swia.iabuilder.datastores.ArmyStore;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.models.Faction;
import com.swia.iabuilder.utils.DoubleClickHelper;
import com.swia.iabuilder.views.callbacks.SwipeToDeleteCallback;
import com.swia.iabuilder.views.dialogs.DeleteArmyDialog;
import com.swia.iabuilder.views.dialogs.RenameArmyDialog;
import com.swia.iabuilder.views.recyclerviews.ArmyCollectionRecyclerView;
import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

public class ArmyCollectionFragment extends Fragment implements CollectionViewHolder.OnItemClickListener<Army>, SwipeToDeleteCallback.OnDeleteListener {

    private static final String ARG_FACTION = "faction";
    private ArmyCollectionRecyclerView recyclerView;

    public static ArmyCollectionFragment newInstance(Faction faction) {
        Bundle args = new Bundle();
        args.putInt(ARG_FACTION, faction.ordinal());
        ArmyCollectionFragment fragment = new ArmyCollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Faction getFaction() {
        return Faction.values()[getArguments().getInt(ARG_FACTION)];
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_army_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rclArmies);
        recyclerView.initialize(getFaction());
        recyclerView.setOnItemClickListener(this);
        recyclerView.setOnDeleteListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            recyclerView = view.findViewById(R.id.rclArmies);
            recyclerView.refresh();
        }
    }

    @Override
    public void onItemClicked(CollectionViewHolder<Army> viewHolder) {
        if (!DoubleClickHelper.testAndSet()) {
            return;
        }
        Army army = viewHolder.getItem();
        ArmyActivity.show(getContext(), army.getUuid());
    }

    @Override
    public boolean onItemLongClicked(final CollectionViewHolder<Army> viewHolder) {
        if (!DoubleClickHelper.testAndSet()) {
            return false;
        }

        final Army army = ArmyStore.load(viewHolder.getItem().getUuid());
        RenameArmyDialog dialog = new RenameArmyDialog(getContext(), army.getName());
        dialog.setListener(name -> {
            army.setName(name);
            ArmyStore.save(army);
            recyclerView.remove(viewHolder.getLayoutPosition());
            recyclerView.add(army);
        }).show();
        return true;
    }

    @Override
    public void onDelete(@NonNull final RecyclerView.ViewHolder viewHolder) {
        DeleteArmyDialog dialog = new DeleteArmyDialog(getContext());
        dialog.setListener(new DeleteArmyDialog.OnDeleteListener() {
            @Override
            public void onDeleted() {
                CollectionViewHolder<?> holder = viewHolder instanceof CollectionViewHolder<?> ? (CollectionViewHolder<?>) viewHolder : null;
                if (holder != null && holder.getItem() instanceof Army) {
                    Army army = (Army) holder.getItem();
                    ArmyStore.remove(army.getUuid());
                    recyclerView.remove(holder.getLayoutPosition());
                }
            }

            @Override
            public void onOperationCanceled() {
                recyclerView.refresh(viewHolder.getLayoutPosition());
            }
        }).show();
    }
}
