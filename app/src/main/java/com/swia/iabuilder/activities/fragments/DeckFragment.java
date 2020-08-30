package com.swia.iabuilder.activities.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.R;
import com.swia.iabuilder.activities.ArmyActivity;
import com.swia.iabuilder.activities.ViewerActivity;
import com.swia.iabuilder.datastores.ArmyStore;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.utils.DoubleClickHelper;
import com.swia.iabuilder.views.dialogs.DeleteCardDialog;
import com.swia.iabuilder.views.recyclerviews.ArmyRecyclerView;
import com.swia.iabuilder.views.viewholders.CardViewHolder;
import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

import java.util.ArrayList;

public class DeckFragment extends Fragment implements CollectionViewHolder.OnItemClickListener<CardViewHolder.CardEntry> {

    private static final String ARG_ARMY = "army";
    private static final String ARG_CARD_TYPE = "card_type";
    private static final String ARG_COLUMNS_PORTRAIT = "columns_portrait";
    private static final String ARG_COLUMNS_LANDSCAPE = "columns_landscape";

    public static DeckFragment newInstance(String uuid, CardType cardType, int columnsPortrait, int columnsLandscape) {
        Bundle args = new Bundle();
        args.putString(ARG_ARMY, uuid);
        args.putInt(ARG_CARD_TYPE, cardType.ordinal());
        args.putInt(ARG_COLUMNS_PORTRAIT, columnsPortrait);
        args.putInt(ARG_COLUMNS_LANDSCAPE, columnsLandscape);
        DeckFragment fragment = new DeckFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String getArmyId() {
        return getArguments().getString(ARG_ARMY);
    }

    private Army getArmy() {
        return ArmyStore.load(getArmyId());
    }

    public CardType getCardType() {
        return CardType.values()[getArguments().getInt(ARG_CARD_TYPE)];
    }

    private int getColumns(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return getArguments().getInt(ARG_COLUMNS_LANDSCAPE);
        }
        return getArguments().getInt(ARG_COLUMNS_PORTRAIT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArmyRecyclerView recyclerView = view.findViewById(R.id.rclDeck);
        recyclerView.setOnItemClickListener(this);
        recyclerView.setOnUpdatedListener((ArmyActivity) getActivity());

        FragmentActivity activity = getActivity();
        if (activity != null) {
            int orientation = activity.getResources().getConfiguration().orientation;
            recyclerView.setColumns(getColumns(orientation));
        }

        recyclerView.initialize(getArmyId(), getCardType());
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            ArmyRecyclerView recyclerView = view.findViewById(R.id.rclDeck);
            if (recyclerView != null) {
                recyclerView.refresh();
            }
        }
    }

    @Override
    public void onItemClicked(CollectionViewHolder<CardViewHolder.CardEntry> viewHolder) {
        if (!DoubleClickHelper.testAndSet()) {
            return;
        }

        Card card = viewHolder.getItem().getCard();
        ViewerActivity.show(getContext(), card);
    }

    @Override
    public boolean onItemLongClicked(CollectionViewHolder<CardViewHolder.CardEntry> viewHolder) {
        if (!DoubleClickHelper.testAndSet()) {
            return false;
        }

        Army army = getArmy();
        Card card = viewHolder.getItem().getCard();

        ArrayList<Card> sideEffects = army.canSafelyRemove(card);
        if (sideEffects.size() > 0) {
            DeleteCardDialog dialog = new DeleteCardDialog(getContext());
            dialog.setListener(() -> remove(card, sideEffects)).show();
        } else {
            remove(card, sideEffects);
        }
        return true;
    }

    private void remove(Card card, ArrayList<Card> sideEffects) {
        View view = getView();
        if (view != null) {
            final ArmyRecyclerView recyclerView = view.findViewById(R.id.rclDeck);
            if (recyclerView != null) {
                recyclerView.remove(card);
                String message = getString(R.string.chooser_card_removed, card.getName());
                Snackbar snackbar = Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.undo, v -> {
                    recyclerView.add(card);
                    for (Card c : sideEffects) {
                        recyclerView.add(c);
                    }
                });
                snackbar.show();
            }
        }
    }
}