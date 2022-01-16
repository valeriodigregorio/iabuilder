package com.swia.iabuilder.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.R;
import com.swia.iabuilder.activities.fragments.DeckFragment;
import com.swia.iabuilder.datastores.ArmyStore;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.parsers.ArmyMarshallerType;
import com.swia.iabuilder.parsers.BaseArmyMarshaller;
import com.swia.iabuilder.settings.SettingsManager;
import com.swia.iabuilder.utils.CardUtils;
import com.swia.iabuilder.utils.DoubleClickHelper;
import com.swia.iabuilder.utils.UriUtils;
import com.swia.iabuilder.views.dialogs.RenameArmyDialog;
import com.swia.iabuilder.views.dialogs.ResultsDialog;
import com.swia.iabuilder.views.pageradapters.CollectionPagerAdapter;
import com.swia.iabuilder.views.recyclerviews.CardGridRecyclerView;

import java.util.ArrayList;

public class ArmyActivity extends AppCompatActivity implements View.OnClickListener, CardGridRecyclerView.OnUpdatedListener {

    public static final String ARG_ARMY = "army";

    public static void show(Context context, String uuid) {
        Intent intent = new Intent(context, ArmyActivity.class);
        intent.putExtra(ARG_ARMY, uuid);
        context.startActivity(intent);
    }

    private String getArmyId() {
        return getIntent().getStringExtra(ARG_ARMY);
    }

    private Army getArmy() {
        return ArmyStore.load(getArmyId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_army);

        Army army = getArmy();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(army.getName());

        ImageView imageView = findViewById(R.id.imgFaction);
        imageView.setImageDrawable(getDrawable(army.getFaction().getDrawableId()));

        CollectionPagerAdapter pagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        for (CardType cardType : CardUtils.VISIBLE_CARD_TYPES) {
            int cp = SettingsManager.getIntSetting(getString(R.string.setting_deck_columns_portrait));
            int cl = SettingsManager.getIntSetting(getString(R.string.setting_deck_columns_landscape));
            pagerAdapter.add(DeckFragment.newInstance(army.getUuid(), cardType, cp, cl), cardType.toString());
        }
        ViewPager viewPager = findViewById(R.id.pgrDecks);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabs = findViewById(R.id.tblDecks);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fabAddCard);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_army, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private Intent getShareUrlIntent(Army army) {
        BaseArmyMarshaller<String, ?> marshaller = ArmyMarshallerType.TTA_EXTENDED.getMarshaller();
        String url = marshaller.serialize(army);
        return new ShareCompat.IntentBuilder(ArmyActivity.this)
                .getIntent()
                .putExtra(Intent.EXTRA_SUBJECT, army.getName())
                .putExtra(Intent.EXTRA_TEXT, url)
                .setType("text/plain");
    }

    private Intent getShareFileIntent(String text, Uri uri, String type) {
        return new ShareCompat.IntentBuilder(ArmyActivity.this)
                .getIntent()
                .putExtra(Intent.EXTRA_SUBJECT, text)
                .putExtra(Intent.EXTRA_TEXT, text)
                .putExtra(Intent.EXTRA_STREAM, uri)
                .setType(type)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!DoubleClickHelper.testAndSet()) {
            return false;
        }

        final Army army = getArmy();
        switch (item.getItemId()) {
            case R.id.action_rename: {
                RenameArmyDialog dialog = new RenameArmyDialog(this, army.getName());
                dialog.setListener(name -> {
                    army.setName(name);
                    ArmyStore.save(army);
                    setTitle(army.getName());
                }).show();
                return true;
            }
            case R.id.action_clone: {
                String uuid = ArmyStore.newUuid();
                army.setUuid(uuid);
                ArmyStore.save(army);
                ArmyActivity.show(this, uuid);
                return true;
            }
            case R.id.action_results: {
                ResultsDialog dialog = new ResultsDialog(this, army.getVictories(), army.getDefeats());
                dialog.setListener((victories, defeats) -> {
                    army.setVictories(victories);
                    army.setDefeats(defeats);
                    ArmyStore.save(army);
                }).show();
                return true;
            }
            case R.id.action_qrcode: {
                BaseArmyMarshaller<String, ?> marshaller = ArmyMarshallerType.TTA_EXTENDED.getMarshaller();
                String url = marshaller.serialize(army);
                QrCodeActivity.show(this, army, url);
                return true;
            }
            case R.id.action_share_link: {
                Intent intent = getShareUrlIntent(army);
                startActivity(Intent.createChooser(intent, getString(R.string.action_share_link)));
                return true;
            }
            case R.id.action_share_image: {
                Uri uri = null;
                ViewPager viewPager = findViewById(R.id.pgrDecks);
                PagerAdapter adapter = viewPager.getAdapter();
                if (adapter != null) {
                    ArrayList<Bitmap> bitmaps = new ArrayList<>();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        View child = viewPager.getChildAt(i);
                        if (child != null) {
                            View view = child.findViewById(R.id.rclDeck);
                            Bitmap bitmap = UriUtils.toBitmap(view);
                            if (bitmap != null) {
                                bitmaps.add(bitmap);
                            }
                        }
                    }
                    Bitmap bitmap = UriUtils.mergeBitmaps(bitmaps, LinearLayout.VERTICAL);
                    uri = UriUtils.toUri(this, bitmap, army.getName(), Bitmap.CompressFormat.PNG);
                }
                if (uri == null) {
                    Toast.makeText(this, getString(R.string.error_image_conversion), Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent intent = getShareFileIntent(army.getName(), uri, "image/png");
                startActivity(Intent.createChooser(intent, getString(R.string.action_share_image)));
                return true;
            }
            case R.id.action_share_file: {
                Uri uri = UriUtils.toUri(this, army);
                if (uri == null) {
                    Toast.makeText(this, getString(R.string.error_file_conversion), Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent intent = getShareFileIntent(army.getName(), uri, "application/octet-stream");
                startActivity(Intent.createChooser(intent, getString(R.string.action_share_file)));
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private DeckFragment getCurrentFragment() {
        ViewPager viewPager = findViewById(R.id.pgrDecks);
        CollectionPagerAdapter pagerAdapter = (CollectionPagerAdapter) viewPager.getAdapter();
        if (pagerAdapter == null) {
            return null;
        }
        return (DeckFragment) pagerAdapter.getItem(viewPager.getCurrentItem());
    }

    @Override
    public void onClick(View view) {
        if (!DoubleClickHelper.testAndSet()) {
            return;
        }
        DeckFragment fragment = getCurrentFragment();
        if (fragment != null) {
            CardType cardType = fragment.getCardType();
            ChooserActivity.show(this, getArmyId(), cardType);
        }
    }

    @Override
    public void onUpdated() {
        TabLayout tabs = findViewById(R.id.tblDecks);
        if (tabs != null) {
            for (CardType cardType : CardUtils.VISIBLE_CARD_TYPES) {
                TabLayout.Tab tab = tabs.getTabAt(cardType.ordinal());
                if (tab != null) {
                    Army army = getArmy();
                    int pointsLeft = army.getPointsLeft(cardType);
                    int totalCards = army.getCards(cardType).size();
                    String title = cardType.toString().toUpperCase() + ": " + totalCards + " \uD83C\uDCA0 / " + pointsLeft + " \u20A7";
                    tab.setText(title);
                }
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}