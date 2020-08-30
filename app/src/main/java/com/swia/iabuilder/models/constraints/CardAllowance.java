package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;

public abstract class CardAllowance implements CardConstraint {
    private final int cardId;
    private final int baseAllowance;
    private final Affiliation affiliation;

    private int allowance;

    protected CardAllowance(int baseAllowance, int cardId, Affiliation affiliation) {
        this.baseAllowance = baseAllowance;
        this.cardId = cardId;
        this.affiliation = affiliation;
        allowance = 0;
    }

    public abstract boolean applicable(Card card);

    public int getAllowance() {
        return allowance;
    }

    @Override
    public void reset() {
        allowance = 0;
    }

    @Override
    public void add(Card card) {
        if (card.getId() == cardId) {
            allowance += baseAllowance;
        } else if (applicable(card)) {
            allowance--;
        }
    }

    @Override
    public void remove(Card card) {
        if (card.getId() == cardId) {
            allowance -= baseAllowance;
        } else if (applicable(card)) {
            allowance++;
        }
    }

    @Override
    public boolean isValid(Card card) {
        DeploymentCard c = (DeploymentCard) card;
        return c.getAffiliation() == this.affiliation ||
                c.getAffiliation() == Affiliation.NEUTRAL ||
                applicable(card);
    }

    @Override
    public boolean check(Card card) {
        DeploymentCard c = (DeploymentCard) card;
        return c.getAffiliation() == this.affiliation ||
                c.getAffiliation() == Affiliation.NEUTRAL ||
                (applicable(card) && allowance > 0);
    }
}
