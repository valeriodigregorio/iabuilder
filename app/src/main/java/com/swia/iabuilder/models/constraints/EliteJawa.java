package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;
import com.swia.datasets.cards.Trait;

import static com.swia.iabuilder.models.constraints.SaskaTeft.PRIORITY;

public class EliteJawa extends CrossFactionConstraint {
    public static final int CARD_ID = 68;
    public static final int ALLOWANCE = 3;
    private static final int PRIORITY = 2;

    public EliteJawa() {
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
        Affiliation affiliation = c.getAffiliation();
        return (affiliation == Affiliation.REBEL || affiliation == Affiliation.IMPERIAL) &&
                !c.isUpgrade() && c.hasTrait(Trait.DROID);
    }
}
