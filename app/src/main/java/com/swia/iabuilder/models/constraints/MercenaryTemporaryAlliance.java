package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;

public class MercenaryTemporaryAlliance extends CrossFactionConstraint {
    public static final int CARD_ID = 118;
    public static final int ALLOWANCE = 2;
    private static final int PRIORITY = 3;

    public MercenaryTemporaryAlliance() {
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
        return c.getAffiliation() == Affiliation.REBEL && !c.isUpgrade();
    }
}
