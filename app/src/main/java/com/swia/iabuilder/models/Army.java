package com.swia.iabuilder.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.DeploymentCard;
import com.swia.iabuilder.datastores.ArmyStore;
import com.swia.iabuilder.models.comparators.DeploymentCardSafeComparator;
import com.swia.iabuilder.utils.CardUtils;
import com.swia.iabuilder.utils.JsonUtils;
import com.swia.iabuilder.utils.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Army {

    private static final int BALANCE_OF_THE_FORCE = 10;

    private String uuid;
    private CardSystem cardSystem;
    private Faction faction;
    private String name;
    private int victories;
    private int defeats;

    private final Deck[] decks = new Deck[CardUtils.VISIBLE_CARD_TYPES.length];

    public Army(CardSystem cardSystem, Faction faction, String name, int victories, int defeats) {
        this(null, cardSystem, faction, name, victories, defeats);
    }

    private Army(String uuid, CardSystem cardSystem, Faction faction, String name, int victories, int defeats) {
        for (CardType cardType : CardUtils.VISIBLE_CARD_TYPES) {
            decks[cardType.ordinal()] = Deck.create(cardType);
        }
        if (uuid == null) {
            uuid = ArmyStore.newUuid();
        }
        setUuid(uuid);
        setCardSystem(cardSystem);
        setFaction(faction);
        setName(name);
        setVictories(victories);
        setDefeats(defeats);
    }

    public static Army fromJson(String json) {
        if (json == null) {
            return null;
        }
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(Army.class, new ArmyDeserializer()).create();
        return gson.fromJson(json, Army.class);
    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.registerTypeAdapter(Army.class, new ArmySerializer()).create();
        return gson.toJson(this);
    }

    public Deck getDeck(CardType cardType) {
        return decks[cardType.ordinal()];
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public CardSystem getCardSystem() {
        return cardSystem;
    }

    public void setCardSystem(CardSystem cardSystem) {
        this.cardSystem = cardSystem;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
        ((DeploymentDeck) getDeck(CardType.DEPLOYMENT)).setFaction(faction);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getDefeats() {
        return defeats;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }

    public String getDescription() {
        ArrayList<Card> cards = getDeck(CardType.DEPLOYMENT).getCards();
        int n = cards.size();
        StringBuilder sb = new StringBuilder();
        if (n > 0) {
            sb.append(cards.get(0).getName());
            for (int i = 1; i < n; i++) {
                sb.append(", ").append(cards.get(i).getName());
            }
        }
        return sb.toString();
    }

    public boolean isValid(Card card) {
        return getDeck(card.getCardType()).isValid(card, this);
    }

    public boolean isAllowed(Card card) {
        return getDeck(card.getCardType()).isAllowed(card);
    }

    public boolean isAllowedUpgrade(DeploymentCard card) {
        return ((DeploymentDeck) getDeck(CardType.DEPLOYMENT)).isAllowedUpgrade(card);
    }

    public boolean canAdd(Card card) {
        if (!getDeck(card.getCardType()).canAdd(card)) {
            return false;
        }
        if (card instanceof DeploymentCard) {
            DeploymentCard deploymentCard = (DeploymentCard) card;
            return !deploymentCard.isUpgrade() || isAllowedUpgrade(deploymentCard);
        }
        return true;
    }

    public boolean add(Card card) {
        if (card != null && isValid(card) && canAdd(card)) {
            getDeck(card.getCardType()).add(card);
            if (card instanceof DeploymentCard && card.getId() == BALANCE_OF_THE_FORCE) {
                getDeck(CardType.COMMAND).incrementPointsLimit(3);
            }
            return true;
        }
        return false;
    }

    public ArrayList<Card> canSafelyRemove(Card card) {
        ArrayList<Card> sideEffects = new ArrayList<>();
        if (card instanceof DeploymentCard) {
            ArrayList<Card> cards = getCards(card.getCardType());
            Collections.sort(cards, DeploymentCardSafeComparator.Instance);
            Army testArmy = new Army(getCardSystem(), getFaction(), getName(), getVictories(), getDefeats());
            boolean skipped = false;
            for (Card c : cards) {
                if (!skipped && c == card) {
                    skipped = true;
                    continue;
                }
                if (!testArmy.isValid(c) || !testArmy.canAdd(c)) {
                    sideEffects.add(c);
                } else {
                    testArmy.add(c);
                }
            }
        }
        return sideEffects;
    }

    public void remove(Card card) {
        Deck deck = getDeck(card.getCardType());
        deck.remove(card);
        if (deck instanceof DeploymentDeck) {
            if (card.getId() == BALANCE_OF_THE_FORCE) {
                getDeck(CardType.COMMAND).incrementPointsLimit(-3);
            }
            ArrayList<Card> cards = getCards(card.getCardType());
            Collections.sort(cards, DeploymentCardSafeComparator.Instance);
            deck.removeAll();
            for (Card c : cards) {
                if (isValid(c) && canAdd(c)) {
                    deck.add(c);
                }
            }
        }
    }

    public void toggleShortlist(Card card) {
        Deck deck = getDeck(card.getCardType());
        deck.toggleShortlist(card);
    }

    public boolean isShortlisted(Card card) {
        Deck deck = getDeck(card.getCardType());
        return deck.isShortlisted(card);
    }

    public int getPointsLeft(CardType cardType) {
        return getDeck(cardType).getPointsLeft();
    }

    public ArrayList<Card> getCards(CardType cardType) {
        return getDeck(cardType).getCards();
    }

    public String getDefaultName() {
        return getDefaultName(0);
    }

    public String getDefaultName(int limit) {
        ArrayList<Card> cards = getDeck(CardType.DEPLOYMENT).getCards();
        Collections.sort(cards);
        StringBuilder army_name = new StringBuilder();
        String last_name = null;
        int i = 1;
        int n = 0;
        for (Card card : cards) {
            String name = ((DeploymentCard) card).getShortname();
            if (last_name == null) {
                last_name = name;
            }
            if (!last_name.equals(name)) {
                if (limit > 0 && i == limit) {
                    break;
                }
                army_name.append(army_name.length() > 0 ? " + " : "")
                    .append(n > 1 ? n + "x" : "")
                    .append(last_name);
                last_name = name;
                i++;
                n = 1;
            } else {
                n++;
            }
        }
        if (last_name != null) {
            army_name.append(army_name.length() > 0 ? " + " : "")
                    .append(n > 1 ? n + "x" : "")
                    .append(last_name);
        }
        return (army_name.length() > 0 ? army_name.toString() : null);
    }

    private static class ArmyDeserializer implements JsonDeserializer<Army> {
        private static final String TAG = ArmyDeserializer.class.getSimpleName();

        @Override
        public Army deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject == null) {
                throw new JsonParseException("json");
            }

            String uuid = JsonUtils.getAsString(jsonObject, "uuid");
            String name = JsonUtils.getAsString(jsonObject, "name");
            CardSystem cardSystem = CardSystem.values()[JsonUtils.getAsInt(jsonObject, "card_system")];
            Faction faction = Faction.values()[JsonUtils.getAsInt(jsonObject, "faction")];
            int victories = JsonUtils.getAsInt(jsonObject, "victories", 0);
            int defeats = JsonUtils.getAsInt(jsonObject, "defeats", 0);

            Army army = new Army(uuid, cardSystem, faction, name, victories, defeats);

            JsonArray jsonArray = JsonUtils.getAsJsonArray(jsonObject, "deployment_deck", true);
            List<Card> cards = JsonUtils.toCardList(cardSystem, CardType.DEPLOYMENT, jsonArray);
            Collections.sort(cards, DeploymentCardSafeComparator.Instance);
            for (Card card : cards) {
                if (!army.add(card)) {
                    Log.w(TAG, "Error processing ID " + card.getId() + " in army: " + json.toString());
                }
            }

            jsonArray = JsonUtils.getAsJsonArray(jsonObject, "command_deck", true);
            cards = JsonUtils.toCardList(cardSystem, CardType.COMMAND, jsonArray);
            for (Card card : cards) {
                if (!army.add(card)) {
                    Log.w(TAG, "Error processing ID " + card.getId() + " in army: " + json.toString());
                }
            }

            jsonArray = JsonUtils.getAsJsonArray(jsonObject, "deployment_shortlist", false);
            cards = JsonUtils.toCardList(cardSystem, CardType.DEPLOYMENT, jsonArray);
            for (Card card : cards) {
                army.toggleShortlist(card);
            }

            jsonArray = JsonUtils.getAsJsonArray(jsonObject, "command_shortlist", false);
            cards = JsonUtils.toCardList(cardSystem, CardType.COMMAND, jsonArray);
            for (Card card : cards) {
                army.toggleShortlist(card);
            }

            return army;
        }
    }

    private static class ArmySerializer implements JsonSerializer<Army> {
        @Override
        public JsonElement serialize(Army src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();

            json.addProperty("uuid", src.getUuid());
            json.addProperty("name", src.getName());
            json.addProperty("card_system", src.getCardSystem().ordinal());
            json.addProperty("faction", src.getFaction().ordinal());
            json.addProperty("victories", src.getVictories());
            json.addProperty("defeats", src.getDefeats());

            List<Card> cards = src.getDeck(CardType.DEPLOYMENT).getCards();
            Collections.sort(cards, DeploymentCardSafeComparator.Instance);
            JsonArray jsonArray = JsonUtils.toJsonArray(cards);
            json.add("deployment_deck", jsonArray);

            jsonArray = JsonUtils.toJsonArray(src.getDeck(CardType.COMMAND).getCards());
            json.add("command_deck", jsonArray);

            jsonArray = JsonUtils.toJsonArray(src.getDeck(CardType.DEPLOYMENT).getShortlist());
            json.add("deployment_shortlist", jsonArray);

            jsonArray = JsonUtils.toJsonArray(src.getDeck(CardType.COMMAND).getShortlist());
            json.add("command_shortlist", jsonArray);

            return json;
        }
    }
}
