package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;
import com.swia.datasets.cards.Trait;

public class EliteJawa extends CardAllowance {
    public static final int CARD_ID = 68;
    public static final int BASE_ALLOWANCE = 3;
    public static final Affiliation CARD_AFFILIATION = Affiliation.MERCENARY;

    public EliteJawa() {
        super(BASE_ALLOWANCE, CARD_ID, CARD_AFFILIATION);
    }

    @Override
    public boolean applicable(Card card) {
        DeploymentCard c = (DeploymentCard) card;
        Affiliation affiliation = c.getAffiliation();
        return affiliation != CARD_AFFILIATION && !c.isUpgrade() && c.hasTrait(Trait.DROID);
    }
}
