package com.swia.iabuilder.parsers;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.DeploymentCard;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.models.comparators.DeploymentCardSafeComparator;
import com.swia.iabuilder.models.Faction;
import com.swia.iabuilder.models.constraints.EliteJawa;
import com.swia.iabuilder.models.constraints.ImperialTemporaryAlliance;
import com.swia.iabuilder.models.constraints.MercenaryTemporaryAlliance;
import com.swia.iabuilder.models.constraints.SaskaTeft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base URI serializer. It implements a generic URI serializer independent from the URI format.
 *
 * @param <E> Type of encode used by the deserializer. An encode represents an army.
 * @param <I> Type of ID used by the serializer. An ID uniquely identifies a card.
 */
public abstract class BaseArmyMarshaller<E, I> implements ArmyMarshaller<E> {

    private final CardParser<I> parser;

    /**
     * Constructs a base URI serializer with a URI parser.
     *
     * @param parser The URI parser used by the serializer.
     */
    protected BaseArmyMarshaller(CardParser<I> parser) {
        this.parser = parser;
    }

    /**
     * Get the IDs of all deployment cards.
     *
     * @param code The code to convert.
     * @return The list of IDs of all deployment cards.
     */
    protected abstract List<I> getDeploymentIds(E code);

    /**
     * Get the IDs of all command cards.
     *
     * @param code The code to convert.
     * @return The list of IDs of all command cards.
     */
    protected abstract List<I> getCommandIds(E code);

    /**
     * Converts a list of deployment card IDs into a code.
     *
     * @param cardSystem The card system of a deployment card.
     * @param ids        A list of IDs of deployment cards.
     * @return The corresponding code for the list of IDs.
     */
    protected abstract E toDeploymentCode(CardSystem cardSystem, List<I> ids);

    /**
     * Converts a list of command card IDs into a code.
     *
     * @param cardSystem The card system of a command card.
     * @param ids        A list of IDs of command cards.
     * @return The corresponding code for the list of IDs.
     */
    protected abstract E toCommandCode(CardSystem cardSystem, List<I> ids);

    /**
     * Combine decks into a single encode.
     *
     * @param deploymentCards The encode of deployment deck.
     * @param commandCards    The encode of command deck.
     * @return The resulting encode for both decks.
     */
    protected abstract E combine(E deploymentCards, E commandCards);

    /**
     * Check if a code is valid for this marshaller.
     *
     * @param code The code to convert.
     * @return True if the code is valid for this marshaller. Otherwise, false.
     */
    protected abstract boolean isValid(E code);

    private List<Card> toCards(CardSystem cardSystem, CardType cardType, List<I> ids) {
        ArrayList<Card> cards = new ArrayList<>();
        for (I id : ids) {
            Card card = parser.toCard(cardSystem, cardType, id);
            if (card != null) {
                cards.add(card);
            }
        }
        return cards;
    }

    private List<I> getIds(List<Card> cards) {
        ArrayList<I> ids = new ArrayList<>();
        for (Card card : cards) {
            I id = parser.fromCard(card);
            if (id != null) {
                ids.add(id);
            }
        }
        return ids;
    }

    private List<I> getDeploymentIds(Army army) {
        return getIds(army.getCards(CardType.DEPLOYMENT));
    }

    private List<I> getCommandIds(Army army) {
        return getIds(army.getCards(CardType.COMMAND));
    }

    private Faction getFaction(List<Card> cards) {
        Faction faction = Faction.REBEL;
        boolean saska = false;
        boolean ejawa = false;

        for (Card card : cards) {
            if (card instanceof DeploymentCard) {
                switch (card.getId()) {
                    case ImperialTemporaryAlliance.CARD_ID:
                        return Faction.IMPERIAL;
                    case MercenaryTemporaryAlliance.CARD_ID:
                        return Faction.MERCENARY;
                    case SaskaTeft.CARD_ID:
                        saska = true;
                        break;
                    case EliteJawa.CARD_ID:
                        ejawa = true;
                        break;
                    default:
                        Faction f = Faction.fromAffiliation(((DeploymentCard) card).getAffiliation());
                        faction = (f == null ? faction : f);
                }
            }
        }

        if (saska) {
            return Faction.REBEL;
        } else if (ejawa) {
            return Faction.MERCENARY;
        }

        return faction;
    }

    public Army deserialize(E code, String name) {
        if (code == null) {
            return null;
        }

        List<I> deploymentCardIds = getDeploymentIds(code);
        List<I> commandCardIds = getCommandIds(code);
        CardSystem cardSystem = parser.getCardSystem(deploymentCardIds, commandCardIds);

        List<Card> deploymentCards = toCards(cardSystem, CardType.DEPLOYMENT, deploymentCardIds);
        List<Card> commandCards = toCards(cardSystem, CardType.COMMAND, commandCardIds);
        Faction faction = getFaction(deploymentCards);

        Army army = new Army(cardSystem, faction, name, 0, 0);
        Collections.sort(deploymentCards, DeploymentCardSafeComparator.Instance);
        for (Card card : deploymentCards) {
            army.add(card);
        }

        for (Card card : commandCards) {
            army.add(card);
        }

        return army;
    }

    public E serialize(Army army) {
        if (army == null) {
            return null;
        }
        List<I> ids = getDeploymentIds(army);
        E code = toDeploymentCode(army.getCardSystem(), ids);
        ids = getCommandIds(army);
        E commandCode = toCommandCode(army.getCardSystem(), ids);
        return combine(code, commandCode);
    }

}
