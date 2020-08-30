package com.swia.iabuilder.views.recyclerviews;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.R;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.settings.SettingsManager;
import com.swia.iabuilder.views.adapters.DeckAdapter;
import com.swia.iabuilder.views.viewholders.CardViewHolder;

import java.util.ArrayList;

public class ArmyRecyclerView extends CardGridRecyclerView {

    public ArmyRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ArmyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArmyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        String key = context.getString(R.string.setting_deck_image_scaling);
        setAdapter(new DeckAdapter(SettingsManager.getIntSetting(key)));
    }

    @Override
    protected void onArmyLoaded(Army army) {
        onArmyModified(army);
        scrollToPosition(0);
    }

    @Override
    protected void onArmyModified(Army army) {
        CardType cardType = getCardType();
        ArrayList<Card> cards = army.getCards(cardType);
        ArrayList<CardViewHolder.CardEntry> collection = new ArrayList<>();
        for (Card card : cards) {
            collection.add(new CardViewHolder.CardEntry(card, true, false));
        }
        updateCollection(collection, CardViewHolder.CardEntry::compareTo);
    }
}
