package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;

public interface CardConstraint {
    void reset();

    void add(Card card);

    void remove(Card card);

    boolean isAllowed(Card card);

    boolean isValid(Card card);
}
