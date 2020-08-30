package com.swia.iabuilder.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.swia.iabuilder.R;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.models.Faction;
import com.swia.iabuilder.utils.QrCodeGenerator;

public class QrCodeActivity extends AppCompatActivity {

    public static final String ARG_NAME = "name";
    public static final String ARG_URL = "url";
    private static final String ARG_FACTION = "faction";

    private static final int QR_CODE_SIZE = 1024;

    public static void show(Context context, Army army, String url) {
        Intent intent = new Intent(context, QrCodeActivity.class);
        intent.putExtra(ARG_NAME, army.getName());
        intent.putExtra(ARG_FACTION, army.getFaction().ordinal());
        intent.putExtra(ARG_URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Bundle extras = getIntent().getExtras();
        String name = extras.getString(ARG_NAME);
        Faction faction = Faction.values()[extras.getInt(ARG_FACTION)];

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(name);

        ImageView imageView = findViewById(R.id.imgFaction);
        imageView.setImageDrawable(getDrawable(faction.getDrawableId()));

        String url = extras.getString(ARG_URL);
        if (url != null && !url.isEmpty()) {
            Bitmap bitmap = QrCodeGenerator.toBitmap(url, QR_CODE_SIZE, QR_CODE_SIZE);
            if (bitmap != null) {
                imageView = findViewById(R.id.qr_code);
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
