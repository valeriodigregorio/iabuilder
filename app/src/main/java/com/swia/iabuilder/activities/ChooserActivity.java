package com.swia.iabuilder.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.R;
import com.swia.iabuilder.datastores.ArmyStore;
import com.swia.iabuilder.datastores.ConfigStore;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.settings.SettingsManager;
import com.swia.iabuilder.utils.DoubleClickHelper;
import com.swia.iabuilder.views.recyclerviews.CardGridRecyclerView;
import com.swia.iabuilder.views.recyclerviews.ChooserRecyclerView;
import com.swia.iabuilder.views.viewholders.CardViewHolder;
import com.swia.iabuilder.views.viewholders.CollectionViewHolder;

public class ChooserActivity extends AppCompatActivity implements CollectionViewHolder.OnItemClickListener<CardViewHolder.CardEntry>, CardGridRecyclerView.OnUpdatedListener {

    private static final String ARG_ARMY = "army";
    private static final String ARG_CARD_TYPE = "card_type";

    public static void show(Context context, String uuid, CardType cardType) {
        Intent intent = new Intent(context, ChooserActivity.class);
        intent.putExtra(ARG_ARMY, uuid);
        intent.putExtra(ARG_CARD_TYPE, cardType.ordinal());
        context.startActivity(intent);
    }

    private String getArmyId() {
        return getIntent().getStringExtra(ARG_ARMY);
    }

    private Army getArmy() {
        return ArmyStore.load(getArmyId());
    }

    private CardType getCardType() {
        Intent intent = getIntent();
        return CardType.values()[intent.getIntExtra(ARG_CARD_TYPE, -1)];
    }

    private void updateOptionsMenu(MenuItem item) {
        if (ConfigStore.getShowDisabled()) {
            item.setIcon(R.drawable.ic_hide);
            item.setTitle(R.string.action_hide);
        } else {
            item.setIcon(R.drawable.ic_show);
            item.setTitle(R.string.action_show);
        }

        ChooserRecyclerView recyclerView = findViewById(R.id.rclChooser);
        recyclerView.refresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        ChooserRecyclerView recyclerView = findViewById(R.id.rclChooser);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CardType cardType = getCardType();
        Spinner spinner = findViewById(R.id.spnFilter);
        ArrayAdapter<CharSequence> adapter = null;
        switch (cardType) {
            case DEPLOYMENT:
                adapter = ArrayAdapter.createFromResource(
                        this, R.array.traits, android.R.layout.simple_spinner_item);
                break;
            case COMMAND:
                adapter = ArrayAdapter.createFromResource(
                        this, R.array.restrictions, android.R.layout.simple_spinner_item);
                break;
            default:
        }

        if (adapter != null) {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setVisibility(View.VISIBLE);
            spinner.setOnItemSelectedListener(recyclerView);
        } else {
            spinner.setVisibility(View.GONE);
        }

        spinner = findViewById(R.id.spnOrder);
        switch (cardType) {
            case DEPLOYMENT:
            case COMMAND:
                adapter = ArrayAdapter.createFromResource(
                        this, R.array.orders, android.R.layout.simple_spinner_item);
                break;
            default:
        }

        if (adapter != null) {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setVisibility(View.VISIBLE);
            spinner.setOnItemSelectedListener(recyclerView);
        } else {
            spinner.setVisibility(View.GONE);
        }

        recyclerView.setOnItemClickListener(this);
        recyclerView.setOnUpdatedListener(this);

        int columns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = SettingsManager.getIntSetting(getString(R.string.setting_card_chooser_columns_landscape));
        } else {
            columns = SettingsManager.getIntSetting(getString(R.string.setting_card_chooser_columns_portrait));
        }

        recyclerView.initialize(getArmyId(), cardType);
        recyclerView.setColumns(columns);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chooser, menu);
        updateOptionsMenu(menu.getItem(0));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!DoubleClickHelper.testAndSet()) {
            return false;
        }

        if (item.getItemId() == R.id.action_show_invalid) {
            ConfigStore.setShowDisabled(!ConfigStore.getShowDisabled());
            updateOptionsMenu(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(CollectionViewHolder<CardViewHolder.CardEntry> viewHolder) {
        CardViewHolder.CardEntry item = viewHolder.getItem();
        if (item.isEnabled()) {
            Card card = item.getCard();
            final ChooserRecyclerView recyclerView = findViewById(R.id.rclChooser);
            if (recyclerView.add(card)) {
                String message = getString(R.string.chooser_card_added, card.getName());
                Snackbar snackbar = Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.undo, v -> recyclerView.remove(card));
                snackbar.show();
            }
        }
    }

    @Override
    public boolean onItemLongClicked(CollectionViewHolder<CardViewHolder.CardEntry> viewHolder) {
        Card card = viewHolder.getItem().getCard();
        Army army = getArmy();
        army.toggleShortlist(card);
        ArmyStore.save(army);
        final ChooserRecyclerView recyclerView = findViewById(R.id.rclChooser);
        recyclerView.refresh();
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public void onUpdated() {
        Army army = getArmy();
        CardType cardType = getCardType();

        int totalCards = army.getCards(cardType).size();
        String card_string = getString(totalCards == 1 ? R.string.card : R.string.cards);
        setTitle(getString(R.string.cards_selected, totalCards, card_string));

        int pointsLeft = army.getPointsLeft(cardType);
        Toolbar toolbar = findViewById(R.id.toolbar);
        String point_string = getString(pointsLeft == 1 ? R.string.point : R.string.points);
        toolbar.setSubtitle(getString(R.string.points_left, pointsLeft, point_string));
    }
}