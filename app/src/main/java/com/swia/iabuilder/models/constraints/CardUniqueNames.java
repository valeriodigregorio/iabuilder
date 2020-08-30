package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;

import java.util.HashSet;
import java.util.Set;

public class CardUniqueNames implements CardConstraint {

    private Set<String> names;

    public CardUniqueNames() {
        names = new HashSet<>();
    }

    @Override
    public void reset() {
        names = new HashSet<>();
    }

    @Override
    public void add(Card card) {
        if (isValid(card)) {
            names.add(card.getName());
        }
    }

    @Override
    public void remove(Card card) {
        if (isValid(card)) {
            names.remove(card.getName());
        }
    }

    @Override
    public boolean check(Card card) {
        return !isValid(card) || !names.contains(card.getName());
    }

    @Override
    public boolean isValid(Card card) {
        return ((DeploymentCard) card).isUnique();
    }
}
