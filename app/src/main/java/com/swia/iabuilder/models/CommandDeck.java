package com.swia.iabuilder.models;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.CommandCard;

import java.util.Set;

public class CommandDeck extends Deck {

    private static final int MAX_POINTS = 15;
    private static final int MAX_CARDS = 15;

    public CommandDeck() {
        super(CardType.COMMAND, MAX_POINTS, MAX_CARDS);
    }

    @Override
    public boolean isValid(Card card, Army army) {
        if (card instanceof CommandCard) {
            CommandCard c = (CommandCard) card;
            String[] cardRestrictions = c.getRestrictions();
            if (cardRestrictions == null || cardRestrictions.length == 0) {
                return true;
            }
            DeploymentDeck deck = (DeploymentDeck) army.getDeck(CardType.DEPLOYMENT);
            Set<String> traitsRestrictions = deck.getCommandDeckRestrictions();
            for (String restriction : cardRestrictions) {
                if (traitsRestrictions.contains(restriction)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onAdd(Card card) {
    }

    @Override
    protected void onRemove(Card card) {
    }

    @Override
    protected void onClear() {
    }
}
