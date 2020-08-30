package com.swia.iabuilder.views.recyclerviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CommandCard;
import com.swia.datasets.cards.DeployableCard;
import com.swia.datasets.cards.Trait;
import com.swia.iabuilder.R;
import com.swia.iabuilder.datastores.ConfigStore;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.settings.SettingsManager;
import com.swia.iabuilder.views.adapters.DeckAdapter;
import com.swia.iabuilder.views.viewholders.CardViewHolder;

import java.util.ArrayList;

public class ChooserRecyclerView extends CardGridRecyclerView implements AdapterView.OnItemSelectedListener {

    private String filter = null;
    private String order = null;

    private static boolean match(Card card, String filter) {
        if (filter == null) {
            return true;
        }
        switch (card.getCardType()) {
            case DEPLOYMENT:
            case COMPANION: {
                DeployableCard c = (DeployableCard) card;
                Trait trait = Trait.fromString(filter);
                return c.hasTrait(trait);
            }
            case COMMAND: {
                CommandCard c = (CommandCard) card;
                if (filter.equals("None")) {
                    return true;
                }
                for (String restriction: c.getRestrictions()) {
                    if (restriction.contains(filter)) {
                        return true;
                    }
                }
                return false;
            }
            case FORM:
            default:
                return true;
        }
    }

    public ChooserRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public ChooserRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooserRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        String key = context.getString(R.string.setting_card_browser_image_scaling);
        setAdapter(new DeckAdapter(SettingsManager.getIntSetting(key)));
    }

    @Override
    protected void onArmyLoaded(Army army) {
        onArmyModified(army);
        scrollToPosition(0);
    }

    @Override
    protected void onArmyModified(Army army) {
        boolean showDisabled = ConfigStore.getShowDisabled();
        ArrayList<CardViewHolder.CardEntry> newCards = new ArrayList<>();

        Card[] cards = getCardType().getAllCards(army.getCardSystem());
        for (Card card : cards) {
            if (!match(card, filter)) {
                continue;
            }
            if (army.isValid(card)) {
                boolean enabled = army.canAdd(card);
                boolean shortlisted = army.isShortlisted(card);
                if (showDisabled || enabled || (shortlisted && army.isAllowed(card))) {
                    newCards.add(new CardViewHolder.CardEntry(card, enabled, shortlisted));
                }
            }
        }

        if (order == null || order.equals("Cost")) {
            updateCollection(newCards, CardViewHolder.CardEntry::compareByCost);
        } else if (order.equals("Name")) {
            updateCollection(newCards, CardViewHolder.CardEntry::compareByName);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
        switch (parent.getId()) {
            case R.id.spnFilter: {
                String filter = parent.getSelectedItem().toString();
                if (!filter.equals(this.filter)) {
                    this.filter = filter;
                    refresh();
                }
                break;
            }
            case R.id.spnOrder: {
                String order = parent.getSelectedItem().toString();
                if (!order.equals(this.order)) {
                    this.order = order;
                    refresh();
                }
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
