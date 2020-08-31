package com.swia.iabuilder.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.CommandCard;
import com.swia.datasets.cards.DeployableCard;
import com.swia.datasets.cards.DeploymentCard;
import com.swia.datasets.cards.Trait;
import com.swia.iabuilder.R;
import com.swia.iabuilder.settings.SettingsManager;
import com.swia.iabuilder.views.adapters.DeckAdapter;
import com.swia.iabuilder.views.recyclerviews.CardBrowserGridRecyclerView;
import com.swia.iabuilder.views.viewholders.CardViewHolder;
import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BrowserActivity extends AppCompatActivity implements CollectionViewHolder.OnItemClickListener<CardViewHolder.CardEntry>, AdapterView.OnItemSelectedListener, TextWatcher {

    public static void show(Context context) {
        Intent intent = new Intent(context, BrowserActivity.class);
        context.startActivity(intent);
    }

    private void createSpinner(int id, int resource, boolean enabled) {
        Spinner spinner = findViewById(id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, resource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setEnabled(enabled);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
    }

    private String getTextFilter() {
        EditText filter = findViewById(R.id.edtFilter);
        return filter.getText().toString();
    }

    private CardSystem getCardSystem() {
        Spinner spinner = findViewById(R.id.spnCardSystem);
        String value = spinner.getSelectedItem().toString();
        return CardSystem.valueOf(value.toUpperCase());
    }

    private CardType getCardType() {
        Spinner spinner = findViewById(R.id.spnCardType);
        String value = spinner.getSelectedItem().toString();
        return CardType.valueOf(value.toUpperCase());
    }

    private Affiliation getAffiliation() {
        Spinner spinner = findViewById(R.id.spnAffiliation);
        try {
            String value = spinner.getSelectedItem().toString();
            return Affiliation.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    private Comparator<CardViewHolder.CardEntry> getComparator() {
        Spinner spinner = findViewById(R.id.spnOrder);
        if (spinner.isEnabled()) {
            Object item = spinner.getSelectedItem();
            if (item != null) {
                String value = item.toString();
                switch (value) {
                    case "Cost":
                        return CardViewHolder.CardEntry::compareByCost;
                    case "Name":
                        return CardViewHolder.CardEntry::compareByName;
                    default:
                }
            }
        }
        return CardViewHolder.CardEntry::compareTo;
    }

    private String getFilter() {
        Spinner spinner = findViewById(R.id.spnFilter);
        try {
            return spinner.getSelectedItem().toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    private void onUpdateSpinners() {
        CardType cardType = getCardType();
        switch (cardType) {
            case DEPLOYMENT:
                createSpinner(R.id.spnAffiliation, R.array.affiliations, true);
                createSpinner(R.id.spnOrder, R.array.orders, true);
                createSpinner(R.id.spnFilter, R.array.traits, true);
                break;
            case COMMAND:
                createSpinner(R.id.spnAffiliation, R.array.affiliations, false);
                createSpinner(R.id.spnOrder, R.array.orders, true);
                createSpinner(R.id.spnFilter, R.array.restrictions, true);
                break;
            case COMPANION:
                createSpinner(R.id.spnAffiliation, R.array.affiliations, false);
                createSpinner(R.id.spnOrder, R.array.orders, false);
                createSpinner(R.id.spnFilter, R.array.traits, true);
                break;
            case FORM:
            default:
                createSpinner(R.id.spnAffiliation, R.array.affiliations, false);
                createSpinner(R.id.spnOrder, R.array.orders, false);
                createSpinner(R.id.spnFilter, R.array.disabled, false);
                break;
        }
    }

    private void onClearBrowser() {
        CardBrowserGridRecyclerView recyclerView = findViewById(R.id.rclBrowser);
        ArrayList<CardViewHolder.CardEntry> collection = new ArrayList<>();
        recyclerView.update(collection);
    }

    private void onUpdateBrowser() {
        CardSystem cardSystem = getCardSystem();
        CardType cardType = getCardType();
        ArrayList<CardViewHolder.CardEntry> collection = new ArrayList<>();
        String filter = getFilter();
        if (filter != null) {
            for (Card card : cardType.getAllCards(cardSystem)) {
                StringBuilder text = new StringBuilder(card.getName());
                if (card instanceof DeploymentCard) {
                    DeploymentCard c = (DeploymentCard) card;
                    text.append(c.getDescription());
                    Affiliation affiliation = getAffiliation();
                    if (affiliation != null && c.getAffiliation() != affiliation) {
                        continue;
                    }
                }
                if (card instanceof DeployableCard) {
                    Trait trait = Trait.fromString(filter);
                    DeployableCard c = (DeployableCard) card;
                    if (!c.hasTrait(trait)) {
                        continue;
                    }
                } else if (card instanceof CommandCard) {
                    CommandCard c = (CommandCard) card;
                    String[] restrictions = c.getRestrictions();
                    if (!filter.equals("All") && (!filter.equals("None") || restrictions.length != 0)) {
                        boolean found = false;
                        for (String restriction : restrictions) {
                            if (restriction.contains(filter)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            continue;
                        }
                    }
                }
                String textFilter = getTextFilter();
                if (textFilter.length() == 0 || text.toString().toLowerCase().contains(textFilter.toLowerCase())) {
                    collection.add(new CardViewHolder.CardEntry(card, true, false));
                }
            }
        }
        Collections.sort(collection, getComparator());
        CardBrowserGridRecyclerView recyclerView = findViewById(R.id.rclBrowser);
        recyclerView.update(collection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        createSpinner(R.id.spnCardSystem, R.array.card_systems, true);
        createSpinner(R.id.spnCardType, R.array.card_types, true);

        EditText filter = findViewById(R.id.edtFilter);
        filter.addTextChangedListener(this);

        int scaling = SettingsManager.getIntSetting(getString(R.string.setting_card_browser_image_scaling));
        DeckAdapter adapter = new DeckAdapter(scaling);
        CardBrowserGridRecyclerView recyclerView = findViewById(R.id.rclBrowser);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        int columns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = SettingsManager.getIntSetting(getString(R.string.setting_card_browser_columns_landscape));
        } else {
            columns = SettingsManager.getIntSetting(getString(R.string.setting_card_browser_columns_portrait));
        }
        recyclerView.setColumns(columns);
    }

    @Override
    public void onItemClicked(CollectionViewHolder<CardViewHolder.CardEntry> item) {
        ViewerActivity.show(this, item.getItem().getCard());
    }

    @Override
    public boolean onItemLongClicked(CollectionViewHolder<CardViewHolder.CardEntry> item) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView)parent.getChildAt(0)).setTextColor(view.isEnabled() ? Color.WHITE : Color.GRAY);
        switch (parent.getId()) {
            case R.id.spnCardType:
                onClearBrowser();
                onUpdateSpinners();
            case R.id.spnCardSystem:
            case R.id.spnAffiliation:
            case R.id.spnFilter:
            case R.id.spnOrder:
                onUpdateBrowser();
                break;
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        onUpdateBrowser();
    }
}