package com.swia.iabuilder.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.R;
import com.swia.iabuilder.activities.fragments.ArmyCollectionFragment;
import com.swia.iabuilder.datastores.ArmyStore;
import com.swia.iabuilder.datastores.ConfigStore;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.models.Faction;
import com.swia.iabuilder.parsers.ttadmiral.TabletopAdmiralArmyMarshaller;
import com.swia.iabuilder.parsers.vassal.VassalParser;
import com.swia.iabuilder.settings.SettingsActivity;
import com.swia.iabuilder.settings.SettingsManager;
import com.swia.iabuilder.utils.DoubleClickHelper;
import com.swia.iabuilder.utils.LogcatFileDump;
import com.swia.iabuilder.utils.UriUtils;
import com.swia.iabuilder.views.dialogs.CreateArmyDialog;
import com.swia.iabuilder.views.dialogs.ReleaseNotesDialog;
import com.swia.iabuilder.views.pageradapters.CollectionPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = SettingsManager.class.getName();

    private static final int REQUEST_PICK_FILE = 1;

    private static final String API_PROFILE = "https://my-json-server.typicode.com/valeriodigregorio/iabuilder-rest/profile";
    private static final String API_DOWNLOAD = "https://drive.google.com/uc?export=download&confirm=Hawt&id=1Vq7h0-rLu1Z7jcZTVRADhkZNUtTyGB9p";

    private void initialize(Context context) {
        CardType.initialize(context);
        ArmyStore.initialize(context);
        ConfigStore.initialize(context);
        SettingsManager.initialize(context);
    }

    private void askForPermissions() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String permission : info.requestedPermissions) {
                    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    private int getCurrentVersionCode() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            return 0;
        }
    }

    private void checkUpdates(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_PROFILE, null,
                response -> {
                    try {
                        JSONObject version = response.getJSONObject("version");
                        int code = version.getInt("code");
                        int current = getCurrentVersionCode();
                        if (code > current) {
                            String name = version.getString("name");
                            String message = getString(R.string.main_update_available, name);
                            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction(R.string.download, v -> {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(API_DOWNLOAD));
                                startActivity(intent);
                            });
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    }
                }, error -> Log.e(TAG, error.toString()));
        queue.add(request);
    }

    private void importArmyFromUrl(String url) {
        final Army army = new TabletopAdmiralArmyMarshaller().deserialize(url, "");
        String name = null;
        if (army != null) {
            name = army.getDefaultName(2);
        }
        if (name != null) {
            army.setName(name);
            ArmyStore.save(army);
            setCurrentFragment(army.getFaction());
            ArmyActivity.show(this, army.getUuid());
        } else {
            Toast.makeText(this, getString(R.string.error_invalid_link), Toast.LENGTH_SHORT).show();
        }
    }

    private void importArmyFromFile(Uri uri) {
        try {
            InputStream stream = getContentResolver().openInputStream(uri);
            Army army = VassalParser.load(stream);
            if (uri.getPath() == null || stream == null || army == null) {
                Toast.makeText(this, getString(R.string.error_invalid_file), Toast.LENGTH_SHORT).show();
                return;
            }
            stream.close();
            army.setName(UriUtils.getFilename(this, uri));
            ArmyStore.save(army);
            setCurrentFragment(army.getFaction());
            ArmyActivity.show(this, army.getUuid());
        } catch (IOException | NullPointerException e) {
            Toast.makeText(this, getString(R.string.error_invalid_file), Toast.LENGTH_SHORT).show();
        }
    }

    private Faction getCurrentFaction() {
        ViewPager viewPager = findViewById(R.id.pgrFactions);
        int item = viewPager.getCurrentItem();
        return Faction.values()[item];
    }

    private void setCurrentFragment(Faction faction) {
        ViewPager viewPager = findViewById(R.id.pgrFactions);
        if (viewPager != null) {
            viewPager.setCurrentItem(faction.ordinal());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File crashFile = new File(getExternalFilesDir(null), "crash_dump.log");
        Thread.setDefaultUncaughtExceptionHandler(new LogcatFileDump(crashFile));

        initialize(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollectionPagerAdapter pagerAdapter = new CollectionPagerAdapter(this.getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.pgrFactions);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        for (Faction faction : Faction.values()) {
            ArmyCollectionFragment fragment = ArmyCollectionFragment.newInstance(faction);
            pagerAdapter.add(fragment, faction.getName());
        }
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tblFactions);
        tabLayout.setupWithViewPager(viewPager);
        for (Faction faction : Faction.values()) {
            TabLayout.Tab tab = tabLayout.getTabAt(faction.ordinal());
            if (tab != null) {
                tab.setIcon(faction.getDrawableId());
            }
        }

        FloatingActionButton fab = findViewById(R.id.fabNewArmy);
        fab.setOnClickListener(this);

        askForPermissions();
        showReleaseNotes();
        onIntent(getIntent());
    }

    private void showReleaseNotes() {
        int current = getCurrentVersionCode();
        int previous = ConfigStore.getCurrentRelease();
        previous = previous == 0 ? current - 1 : previous;
        if (previous != current) {
            ReleaseNotesDialog dialog = new ReleaseNotesDialog(this, previous, current);
            dialog.show();
            ConfigStore.setCurrentRelease(current);
        }
    }

    protected void onIntent(Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_VIEW)) {
            importArmyFromUrl(intent.getDataString());
            intent.setAction(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatingActionButton fab = findViewById(R.id.fabNewArmy);
        checkUpdates(fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!DoubleClickHelper.testAndSet()) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.action_import_link: {
                String url = null;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData clip = clipboard.getPrimaryClip();
                    if (clip != null && clip.getItemCount() > 0) {
                        url = clip.getItemAt(0).coerceToText(this).toString();
                    }
                }
                importArmyFromUrl(url);
                return true;
            }
            case R.id.action_import_file: {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, REQUEST_PICK_FILE);
                return true;
            }
            case R.id.action_settings: {
                SettingsActivity.show(this);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_FILE) {
            importArmyFromFile(data.getData());
        }
    }

    @Override
    public void onClick(View view) {
        if (!DoubleClickHelper.testAndSet()) {
            return;
        }

        CreateArmyDialog dialog = new CreateArmyDialog(this);
        dialog.setListener((CardSystem cardSystem, String name) -> {
            Army army = new Army(cardSystem, getCurrentFaction(), name, 0, 0);
            ArmyStore.save(army);
            ArmyActivity.show(this, army.getUuid());
        }).show();
    }
}
