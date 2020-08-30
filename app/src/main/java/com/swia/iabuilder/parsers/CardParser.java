package com.swia.iabuilder.parsers;

import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;

import java.util.List;

/**
 * Converts a generic ID to the corresponding card and vice versa.
 */
public interface CardParser<T> {

    /**
     * Retrieves the card system used by a set of deployment and command cards.
     *
     * @param deploymentCardIds List of deployment card IDs.
     * @param commandCardIds    List of command card IDs.
     * @return The card system.
     */
    CardSystem getCardSystem(List<T> deploymentCardIds, List<T> commandCardIds);

    /**
     * Converts ID to the respective card object.
     *
     * @param cardSystem The card system of the card.
     * @param cardType   The card type of the card.
     * @param id         The ID of the card.
     * @return The corresponding card object instance.
     */
    Card toCard(CardSystem cardSystem, CardType cardType, T id);

    /**
     * Converts a card object to the respective id.
     *
     * @param card A card object instance.
     * @return The corresponding ID of the card.
     */
    T fromCard(Card card);

}
