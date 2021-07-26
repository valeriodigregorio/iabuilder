package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CommandCard;
import com.swia.datasets.cards.DeploymentCard;

import java.util.HashMap;
import java.util.Map;

public class CardCounter implements CardConstraint {

    private final Map<Integer, Integer> counters = new HashMap<>();

    public CardCounter() {
    }

    @Override
    public void reset() {
        counters.clear();
    }

    @Override
    public void add(Card card) {
        int id = card.getId();
        if (counters.containsKey(id)) {
            int value = counters.get(id);
            counters.put(card.getId(), value + 1);
        } else {
            counters.put(card.getId(), 1);
        }
    }

    @Override
    public void remove(Card card) {
        int id = card.getId();
        if (counters.containsKey(id)) {
            int value = counters.get(id);
            if (value > 0) {
                counters.put(id, value - 1);
            } else {
                counters.remove(id);
            }
        } else {
            throw new IllegalArgumentException(card.toString());
        }
    }

    @Override
    public boolean isAllowed(Card card) {
        if (!isValid(card)) {
            return false;
        }
        Integer n = counters.get(card.getId());
        if (n == null) {
            n = 0;
        }
        return n < card.getLimit();
    }

    @Override
    public boolean isValid(Card card) {
        return card instanceof DeploymentCard || card instanceof CommandCard;
    }
}
