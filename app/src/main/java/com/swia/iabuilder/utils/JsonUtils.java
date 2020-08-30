package com.swia.iabuilder.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JsonUtils {

    public static JsonArray toJsonArray(Collection<Card> cards) {
        JsonArray jsonArray = new JsonArray();
        for (Card card : cards) {
            jsonArray.add(card.getId());
        }
        return jsonArray;
    }

    public static List<Card> toCardList(CardSystem cardSystem, CardType cardType, JsonArray jsonArray) {
        ArrayList<Card> cards = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement e = jsonArray.get(i);
                if (e == null) {
                    throw new JsonParseException(Integer.toString(i));
                }

                int id = e.getAsInt();
                Card card = cardType.getCard(cardSystem, id);
                if (card != null) {
                    cards.add(card);
                }
            }
        }
        return cards;
    }

    public static String getAsString(JsonObject jsonObject, String name) {
        return getAsString(jsonObject, name, null);
    }

    public static String getAsString(JsonObject jsonObject, String name, String defValue) {
        JsonElement e = jsonObject.get(name);
        if (e == null) {
            if (defValue == null) {
                throw new JsonParseException(name);
            }
            return defValue;
        }
        return e.getAsString();
    }

    public static int getAsInt(JsonObject jsonObject, String name) {
        return getAsInt(jsonObject, name, null);
    }

    public static int getAsInt(JsonObject jsonObject, String name, Integer defValue) {
        JsonElement e = jsonObject.get(name);
        if (e == null) {
            if (defValue == null) {
                throw new JsonParseException(name);
            }
            return defValue;
        }
        return e.getAsInt();
    }

    public static JsonArray getAsJsonArray(JsonObject jsonObject, String name, boolean mustThrow) {
        JsonElement e = jsonObject.get(name);
        if (e == null) {
            if (mustThrow) {
                throw new JsonParseException(name);
            } else {
                return null;
            }
        }

        JsonArray jsonArray = e.getAsJsonArray();
        if (jsonArray == null) {
            if (mustThrow) {
                throw new JsonParseException(name);
            } else {
                return null;
            }
        }

        return jsonArray;
    }

}
