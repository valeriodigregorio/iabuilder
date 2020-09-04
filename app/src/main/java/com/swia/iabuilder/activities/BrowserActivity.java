package com.swia.iabuilder.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.Trait;
import com.swia.iabuilder.R;
import com.swia.iabuilder.datastores.CardDatastore;
import com.swia.iabuilder.settings.SettingsManager;
import com.swia.iabuilder.views.adapters.DeckAdapter;
import com.swia.iabuilder.views.recyclerviews.CardBrowserGridRecyclerView;
import com.swia.iabuilder.views.viewholders.CardViewHolder;
import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

import java.util.ArrayList;

public class BrowserActivity extends AppCompatActivity implements CollectionViewHolder.OnItemClickListener<CardViewHolder.CardEntry>, AdapterView.OnItemSelectedListener, TextWatcher {

    public static void show(Context context) {
        Intent intent = new Intent(context, BrowserActivity.class);
        context.startActivity(intent);
    }

    private void createSpinner(int id, int resource, boolean enabled) {
        Spinner spinner = findViewById(id);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, resource, R.layout.spinner_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setEnabled(enabled);
        spinner.setVisibility(enabled ? View.VISIBLE : View.GONE);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
    }

    private String getSpinnerValue(int spinnerId) {
        Spinner spinner = findViewById(spinnerId);
        try {
            return spinner.isEnabled() ? spinner.getSelectedItem().toString() : null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private CardSystem getCardSystem() {
        String value = getSpinnerValue(R.id.spnCardSystem);
        return CardSystem.fromString(value);
    }

    private CardType getCardType() {
        String value = getSpinnerValue(R.id.spnCardType);
        return CardType.fromString(value);
    }
    private String getBox() {
        return getSpinnerValue(R.id.spnBox);
    }

    private Affiliation getAffiliation() {
        String value = getSpinnerValue(R.id.spnAffiliation);
        return Affiliation.fromString(value);
    }

    private String getSortingOrder() {
        return getSpinnerValue(R.id.spnOrder);
    }

    private Trait getTrait() {
        String value = getSpinnerValue(R.id.spnTrait);
        return Trait.fromString(value);
    }

    private String getRestriction() {
        return getSpinnerValue(R.id.spnRestriction);
    }

    private String getFigureType() {
        return getSpinnerValue(R.id.spnFigureType);
    }

    private String getFigureSize() {
        return getSpinnerValue(R.id.spnFigureSize);
    }

    private String getTextFilter() {
        EditText filter = findViewById(R.id.edtFilter);
        return filter.getText().toString();
    }

    private void onUpdateSpinners() {
        createSpinner(R.id.spnAffiliation, R.array.affiliations, false);
        createSpinner(R.id.spnFigureType, R.array.figure_types, false);
        createSpinner(R.id.spnFigureSize, R.array.figure_sizes, false);
        createSpinner(R.id.spnOrder, R.array.orders, false);
        createSpinner(R.id.spnTrait, R.array.disabled, false);
        createSpinner(R.id.spnRestriction, R.array.disabled, false);

        CardType cardType = getCardType();
        if (cardType == null) {
            return;
        }

        switch (cardType) {
            case DEPLOYMENT:
                createSpinner(R.id.spnAffiliation, R.array.affiliations, true);
                createSpinner(R.id.spnFigureType, R.array.figure_types, true);
                createSpinner(R.id.spnFigureSize, R.array.figure_sizes, true);
                createSpinner(R.id.spnOrder, R.array.orders, true);
            case COMPANION:
                createSpinner(R.id.spnTrait, R.array.traits, true);
                break;
            case COMMAND:
                createSpinner(R.id.spnOrder, R.array.orders, true);
                createSpinner(R.id.spnRestriction, R.array.restrictions, true);
                break;
            case FORM:
            default:
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
        if (cardSystem == null) {
            return;
        }

        CardType cardType = getCardType();
        if (cardType == null) {
            return;
        }

        Card[] cards = cardType.getAllCards(cardSystem);
        ArrayList<CardViewHolder.CardEntry> collection = new CardDatastore(cards)
                .whereBoxIs(getBox())
                .whereAffiliationIs(getAffiliation())
                .whereTraitIs(getTrait())
                .whereRestrictionIs(getRestriction())
                .whereFigureTypeIs(getFigureType())
                .whereFigureSizeIs(getFigureSize())
                .whereTextContains(getTextFilter())
                .orderBy(getSortingOrder())
                .getCollection();

        CardBrowserGridRecyclerView recyclerView = findViewById(R.id.rclBrowser);
        recyclerView.update(collection);
        recyclerView.scrollToPosition(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        createSpinner(R.id.spnCardSystem, R.array.card_systems, true);
        createSpinner(R.id.spnCardType, R.array.card_types, true);
        createSpinner(R.id.spnBox, R.array.boxes, true);

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
        switch (parent.getId()) {
            case R.id.spnCardType:
                onClearBrowser();
                onUpdateSpinners();
            case R.id.spnCardSystem:
            case R.id.spnBox:
            case R.id.spnAffiliation:
            case R.id.spnTrait:
            case R.id.spnRestriction:
            case R.id.spnFigureType:
            case R.id.spnFigureSize:
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