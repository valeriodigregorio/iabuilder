package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;

public class DoctorAphra extends CrossFactionConstraint {
    public static final int CARD_ID = 209;
    public static final int ALLOWANCE = 2;
    private static final int PRIORITY = 1;

    public static final int IACP_0_0_0 = 0;
    public static final int IACP_BT_1 = 9;

    public DoctorAphra() {
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
        return card.getId() == IACP_0_0_0 || card.getId() == IACP_BT_1;
    }
}
