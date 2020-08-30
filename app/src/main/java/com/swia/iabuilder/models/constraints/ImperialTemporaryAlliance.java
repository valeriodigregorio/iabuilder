package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;

public class ImperialTemporaryAlliance extends CardAllowance {
    public static final int CARD_ID = 119;
    public static final int BASE_ALLOWANCE = 2;
    public static final Affiliation CARD_AFFILIATION = Affiliation.IMPERIAL;

    public ImperialTemporaryAlliance() {
        super(BASE_ALLOWANCE, CARD_ID, CARD_AFFILIATION);
    }

    @Override
    public boolean applicable(Card card) {
        DeploymentCard c = (DeploymentCard) card;
        Affiliation affiliation = c.getAffiliation();
        return affiliation == Affiliation.MERCENARY && !c.isUpgrade();
    }
}
