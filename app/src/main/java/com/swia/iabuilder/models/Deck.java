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

    private static final int IACP_HEAVY_STOORMTROOPER_ELITE = 53;

    private static int getCardCost(Card card) {
        switch (card.getCardType()) {
            case DEPLOYMENT:
                int cost = ((DeploymentCard) card).getDeploymentCost();
                // TODO: Introduce a better handling of Modular ability once the card gets approval
                if (card.getId() == IACP_HEAVY_STOORMTROOPER_ELITE) {
                    cost -= 1;
                }
                return cost;
            case COMMAND:
                return ((CommandCard) card).getCost();
            default:
                throw new IllegalArgumentException(card.getCardType().toString());
        }
    }

    public static Deck create(CardType cardType) {
        switch (cardType) {
            case DEPLOYMENT:
                return new DeploymentDeck();
            case COMMAND:
                return new CommandDeck();
            default:
                throw new IllegalArgumentException(cardType.toString());
        }
    }

    public Deck(CardType cardType, int pointsLimit, int sizeLimit) {
        this.cardType = cardType;
        this.pointsLimit = pointsLimit;
        this.sizeLimit = sizeLimit;
        clear();
    }

    public Deck(CardType cardType, int pointsLimit) {
        this(cardType, pointsLimit, Integer.MAX_VALUE);
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

    public abstract boolean isValid(Card card, Army army);

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

    public boolean isAllowed(Card card) {
        return counter.check(card);
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

    public void removeAll() {
        counter.reset();
        cards = new ArrayList<>();
        pointsLeft = pointsLimit;
        currentSize = 0;
        onClear();
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
        removeAll();
    }
}
