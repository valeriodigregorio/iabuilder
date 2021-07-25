package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;

public class SaskaTeft extends CrossFactionConstraint {
    public static final int CARD_ID = 108;
    public static final int ALLOWANCE = 1;
    public static final int PRIORITY = 1;

    public SaskaTeft() {
        super(ALLOWANCE);
    }

    @Override
    public int getCardId() {
        return CARD_ID;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public boolean isValid(Card card) {
        DeploymentCard c = (DeploymentCard) card;
        return c.getAffiliation() == Affiliation.MERCENARY && !c.isUpgrade();
    }
}
