package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;

import java.util.ArrayList;
import java.util.List;

public abstract class CrossFactionConstraint implements CardConstraint {
    private final List<Card> cards;
    private final int size;

    protected CrossFactionConstraint(int size) {
        this.cards = new ArrayList<>();
        this.size = size;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public void reset() {
        cards.clear();
    }

    @Override
    public void add(Card card) {
        if (cards.size() < size) {
            cards.add(card);
        }
    }

    @Override
    public void remove(Card card) {
        cards.remove(card);
    }

    @Override
    public boolean isAllowed(Card card) {
        return isValid(card) && cards.size() < size;
    }

    public abstract int getCardId();

    public abstract int getPriority();
}
