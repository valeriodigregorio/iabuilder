package com.swia.iabuilder.models;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.CommandCard;
import com.swia.datasets.cards.DeploymentCard;
import com.swia.iabuilder.models.constraints.CardCounter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public abstract class Deck {

    private final int sizeLimit;
    private final CardType cardType;
    private int pointsLimit;
    private int pointsLeft;
    private int currentSize;

    private ArrayList<Card> cards;
    private HashSet<Card> shortlist;
    private final CardCounter counter = new CardCounter();
    private final Army army;

    private static final int IACP_HEAVY_STOORMTROOPER_ELITE = 53;
    private static final int IACP_THE_MANDALORIA_RISING_PHOENIX = 216;

    private static int getCardCost(Card card) {
        switch (card.getCardType()) {
            case DEPLOYMENT:
                int cost = ((DeploymentCard) card).getDeploymentCost();
                // TODO: Introduce a better handling of Modular and Quested abilities
                if (card.getId() == IACP_HEAVY_STOORMTROOPER_ELITE ||
                        card.getId() == IACP_THE_MANDALORIA_RISING_PHOENIX) {
                    cost -= 1;
                }
                return cost;
            case COMMAND:
                return ((CommandCard) card).getCost();
            default:
                throw new IllegalArgumentException(card.getCardType().toString());
        }
    }

    public static Deck create(CardType cardType, Army army) {
        switch (cardType) {
            case DEPLOYMENT:
                return new DeploymentDeck(army);
            case COMMAND:
                return new CommandDeck(army);
            default:
                throw new IllegalArgumentException(cardType.toString());
        }
    }

    public Deck(CardType cardType, int pointsLimit, int sizeLimit, Army army) {
        this.cardType = cardType;
        this.pointsLimit = pointsLimit;
        this.sizeLimit = sizeLimit;
        this.army = army;
        clear();
    }

    public Deck(CardType cardType, int pointsLimit, Army army) {
        this(cardType, pointsLimit, Integer.MAX_VALUE, army);
    }

    protected Army getArmy() {
        return army;
    }

    public int getPointsLeft() {
        return pointsLeft;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public HashSet<Card> getShortlist() {
        return shortlist;
    }

    public void incrementPointsLimit(int n) {
        pointsLimit += n;
        pointsLeft += n;
    }

    public boolean canAdd(Card card) {
        int cost = getCardCost(card);
        if (card.getCardType() == CardType.DEPLOYMENT && cost > pointsLeft) {
            DeploymentCard fix = ((DeploymentCard) card).getCardFix();
            if (fix != null) {
                cost += fix.getDeploymentCost();
            }
        }
        return currentSize < sizeLimit && cost <= pointsLeft && isAllowed(card);
    }

    public boolean isValid(Card card) {
        return counter.isValid(card);
    }

    public boolean isAllowed(Card card) {
        return counter.isAllowed(card);
    }

    protected abstract void onAdd(Card card);

    protected abstract void onRemove(Card card);

    protected abstract void onClear();

    public void add(Card card) {
        if (card.getCardType() == cardType) {
            int cost = getCardCost(card);
            if (card.getCardType() == CardType.DEPLOYMENT && cost > pointsLeft) {
                DeploymentCard fix = ((DeploymentCard) card).getCardFix();
                if (fix != null) {
                    add(fix);
                }
            }
            cards.add(card);
            pointsLeft -= cost;
            currentSize++;
            Collections.sort(cards);
            counter.add(card);
            onAdd(card);
        }
    }

    public void remove(Card card) {
        if (cards.contains(card)) {
            cards.remove(card);
            pointsLeft += getCardCost(card);
            currentSize--;
            counter.remove(card);
            onRemove(card);
        }
    }

    public void toggleShortlist(Card card) {
        if (shortlist.contains(card)) {
            shortlist.remove(card);
        } else {
            shortlist.add(card);
        }
    }

    public boolean isShortlisted(Card card) {
        return shortlist.contains(card);
    }

    public void clear() {
        shortlist = new HashSet<>();
        counter.reset();
        cards = new ArrayList<>();
        pointsLeft = pointsLimit;
        currentSize = 0;
        onClear();
    }
}
