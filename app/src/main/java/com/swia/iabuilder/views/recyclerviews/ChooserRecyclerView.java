package com.swia.iabuilder.views.recyclerviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.Trait;
import com.swia.iabuilder.R;
import com.swia.iabuilder.datastores.CardDatastore;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.settings.SettingsManager;
import com.swia.iabuilder.views.adapters.DeckAdapter;
import com.swia.iabuilder.views.viewholders.CardViewHolder;

import java.util.ArrayList;
import java.util.Objects;

public class ChooserRecyclerView extends CardGridRecyclerView implements AdapterView.OnItemSelectedListener {

    private Trait trait = null;
    private String restriction = null;
    private String order = null;

    public ChooserRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ChooserRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooserRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        String key = context.getString(R.string.setting_card_chooser_image_scaling);
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
        Card[] cards = cardType.getAllCards(army.getCardSystem());
        ArrayList<CardViewHolder.CardEntry> entries = new CardDatastore(cards)
                .whereTraitIs(trait)
                .whereRestrictionIs(restriction)
                .whereValidIn(army)
                .orderBy(order)
                .getCollection();
        updateCollection(entries);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = parent.getSelectedItem().toString();
        switch (parent.getId()) {
            case R.id.spnTrait: {
                Trait t = Trait.fromString(value);
                if (trait != t) {
                    trait = t;
                    refresh();
                    scrollToPosition(0);
                }
                break;
            }
            case R.id.spnRestriction: {
                if (!Objects.equals(restriction, value)) {
                    restriction = value;
                    refresh();
                    scrollToPosition(0);
                }
                break;
            }
            case R.id.spnOrder: {
                if (!Objects.equals(order, value)) {
                    order = value;
                    refresh();
                    scrollToPosition(0);
                }
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
