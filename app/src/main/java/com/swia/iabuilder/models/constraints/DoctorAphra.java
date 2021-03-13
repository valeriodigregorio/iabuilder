package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;

public class DoctorAphra extends CardAllowance {
    public static final int CARD_ID = 209;
    public static final int BASE_ALLOWANCE = 2;
    public static final Affiliation CARD_AFFILIATION = Affiliation.MERCENARY;

    public static final int IACP_0_0_0 = 0;
    public static final int IACP_BT_1 = 9;

    public DoctorAphra() {
        super(BASE_ALLOWANCE, CARD_ID, CARD_AFFILIATION);
    }

    @Override
    public boolean applicable(Card card) {
        return card.getId() == IACP_0_0_0 || card.getId() == IACP_BT_1;
    }
}
