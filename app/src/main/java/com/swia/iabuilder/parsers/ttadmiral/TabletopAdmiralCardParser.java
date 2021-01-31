package com.swia.iabuilder.parsers.ttadmiral;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.parsers.CardParser;

import java.util.List;

public class TabletopAdmiralCardParser implements CardParser<Integer> {

    private static final int TTA_FIRST_IACP_DEPLOYMENT_CARD = 175;
    private static final int TTA_FIRST_IACP_COMMAND_CARD = 233;

    @Override
    public CardSystem getCardSystem(List<Integer> deploymentCardIds, List<Integer> commandCardIds) {
        for (int id : deploymentCardIds) {
            if (id >= TTA_FIRST_IACP_DEPLOYMENT_CARD) {
                return CardSystem.IACP;
            }
        }
        for (int id : commandCardIds) {
            if (id >= TTA_FIRST_IACP_COMMAND_CARD) {
                return CardSystem.IACP;
            }
        }
        return CardSystem.FFG;
    }

    @Override
    public Card toCard(CardSystem cardSystem, CardType cardType, Integer id) {
        Card[] cards = cardType.getAllCards(cardSystem);
        for (Card card : cards) {
            for (int ttaId : card.getTabletopAdmiral()) {
                if (ttaId == id) {
                    return card;
                }
            }
        }
        return null;
    }

    @Override
    public Integer fromCard(Card card) {
        CardType cardType = card.getCardType();
        return card.getTabletopAdmiral()[0];
    }

}
