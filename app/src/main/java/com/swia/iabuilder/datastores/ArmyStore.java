package com.swia.iabuilder.datastores;

import android.content.Context;
import android.content.SharedPreferences;

import com.swia.iabuilder.models.Army;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class ArmyStore {

    private static final String ARMY_STORE = "army_store";

    private static SharedPreferences preferences = null;

    public static void initialize(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(ARMY_STORE, 0);
        }
    }

    public static void save(Army army) {
        SharedPreferences.Editor editor = preferences.edit();
        String json = army.toJson();
        editor.putString(army.getUuid(), json);
        editor.apply();
    }

    public static Army load(String uuid) {
        String json = preferences.getString(uuid, null);
        Army army = Army.fromJson(json);
        if (army == null) {
            throw new NullPointerException(uuid);
        }
        army.setUuid(uuid);
        return army;
    }

    public static void remove(String uuid) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(uuid);
        editor.apply();
    }

    public static String newUuid() {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (preferences.contains(uuid));
        return uuid;
    }

    public static ArrayList<String> list() {
        ArrayList<String> uuids = new ArrayList<>();
        Map<String, ?> entries = preferences.getAll();
        for (Map.Entry<String, ?> entry : entries.entrySet()) {
            uuids.add(entry.getKey());
        }
        return uuids;
    }
}