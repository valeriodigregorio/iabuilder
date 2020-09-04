package com.swia.iabuilder.parsers.vassal;

import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.models.Faction;
import com.swia.iavd.IavdFile;
import com.swia.iavd.model.Affiliation;
import com.swia.iavd.model.Card;
import com.swia.iavd.model.CardSystem;
import com.swia.iavd.model.DeploymentCard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class VassalParser {

    private static final String IMPERIAL_TEMPORARY_ALLIANCE = DeploymentCard.getId(Affiliation.IMPERIAL, "Temporary Alliance", true, false, "Hired Guns");
    private static final String MERCENARY_TEMPORARY_ALLIANCE = DeploymentCard.getId(Affiliation.MERCENARY, "Temporary Alliance", true, false, "A Common Enemy");
    private static final String FFG_SASKA_TEFT = DeploymentCard.getId(Affiliation.REBEL, "Saska Teft", true, false, null);
    private static final String IACP_SASKA_TEFT = DeploymentCard.getId(Affiliation.REBEL, "Saska Teft", true, true, "Underworld Innovator");
    private static final String ELITE_JAWA = DeploymentCard.getId(Affiliation.MERCENARY, "Jawa", false, true, null);

    private static Faction getFaction(List<Card> cards) {
        Faction faction = Faction.REBEL;
        boolean saska = false;
        boolean ejawa = false;

        for (Card card : cards) {
            if (card instanceof DeploymentCard) {
                if (card.getId().equals(IMPERIAL_TEMPORARY_ALLIANCE)) {
                    return Faction.IMPERIAL;
                } else if (card.getId().equals(MERCENARY_TEMPORARY_ALLIANCE)) {
                    return Faction.MERCENARY;
                } else if (card.getId().equals(FFG_SASKA_TEFT) || card.getId().equals(IACP_SASKA_TEFT)) {
                    saska = true;
                } else if (card.getId().equals(ELITE_JAWA)) {
                    ejawa = true;
                } else {
                    Affiliation affiliation = ((DeploymentCard) card).getAffiliation();
                    Faction f = Faction.fromAffiliation(com.swia.datasets.cards.Affiliation.fromString(affiliation.toString()));
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

    private static com.swia.datasets.cards.CardSystem getCardSystem(List<Card> cards) {
        for (Card card : cards) {
            if (card.getCardSystem() == CardSystem.IACP) {
                return com.swia.datasets.cards.CardSystem.IACP;
            }
        }
        return com.swia.datasets.cards.CardSystem.FFG;
    }

    private static CardType getCardType(Card card) {
        if (card instanceof com.swia.iavd.model.DeploymentCard) {
            return CardType.DEPLOYMENT;
        } else if (card instanceof com.swia.iavd.model.CommandCard) {
            return CardType.COMMAND;
        } else {
            return null;
        }
    }

    public static Army load(InputStream stream) throws IOException {
        List<Card> cards = IavdFile.load(stream);
        com.swia.datasets.cards.CardSystem cardSystem = getCardSystem(cards);
        Faction faction = getFaction(cards);
        Army army = new Army(cardSystem, faction, "", 0, 0);
        boolean valid = true;
        for (Card card : cards) {
            com.swia.datasets.cards.Card c = getCardType(card).getCard(cardSystem, card.getId());
            if (c != null) {
                valid &= army.add(c);
            }
        }
        return valid ? army : null;
    }

    public static void save(OutputStream stream, Army army) throws IOException {
        List<Card> cards = new ArrayList<>();
        CardSystem cardSystem = CardSystem.valueOf(army.getCardSystem().toString().toUpperCase());
        for (com.swia.datasets.cards.Card card : army.getCards(CardType.DEPLOYMENT)) {
            Card c = IavdFile.getCard(cardSystem, card.getStringId());
            cards.add(c);
        }
        for (com.swia.datasets.cards.Card card : army.getCards(CardType.COMMAND)) {
            Card c = IavdFile.getCard(cardSystem, card.getStringId());
            cards.add(c);
        }
        IavdFile.save(stream, cards);
    }
}
