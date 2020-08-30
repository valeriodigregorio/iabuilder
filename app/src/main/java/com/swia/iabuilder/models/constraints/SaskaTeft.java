package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;

public class SaskaTeft extends CardAllowance {
    public static final int CARD_ID = 108;
    public static final int BASE_ALLOWANCE = 1;
    public static final Affiliation CARD_AFFILIATION = Affiliation.REBEL;

    public SaskaTeft() {
        super(BASE_ALLOWANCE, CARD_ID, CARD_AFFILIATION);
    }

    @Override
    public boolean applicable(Card card) {
        DeploymentCard c = (DeploymentCard) card;
        return c.getAffiliation() == Affiliation.MERCENARY && !c.isUpgrade();
    }
}
