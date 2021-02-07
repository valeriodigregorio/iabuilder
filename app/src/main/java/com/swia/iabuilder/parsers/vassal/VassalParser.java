package com.swia.iabuilder.parsers.vassal;

import android.util.Log;

import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.CommandCard;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.models.Faction;
import com.swia.iabuilder.models.comparators.DeploymentCardSafeComparator;
import com.swia.iavd.IavdFile;
import com.swia.iavd.model.Affiliation;
import com.swia.iavd.model.Card;
import com.swia.iavd.model.CardSystem;
import com.swia.iavd.model.DeploymentCard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
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

    public static Army load(InputStream stream) throws IOException {
        List<Card> cards = IavdFile.load(stream);
        com.swia.datasets.cards.CardSystem cardSystem = getCardSystem(cards);
        Faction faction = getFaction(cards);
        Army army = new Army(cardSystem, faction, "", 0, 0);
        boolean valid = true;
        ArrayList<com.swia.datasets.cards.DeploymentCard> deploymentCards = new ArrayList<>();
        ArrayList<com.swia.datasets.cards.CommandCard> commandCards = new ArrayList<>();
        for (Card card : cards) {
            switch (card.getCardType()) {
                case COMMAND: {
                    com.swia.datasets.cards.Card c = CardType.COMMAND.getCard(cardSystem, card.getId());
                    if (c != null) {
                        commandCards.add((CommandCard) c);
                    }
                }
                case DEPLOYMENT: {
                    com.swia.datasets.cards.Card c = CardType.DEPLOYMENT.getCard(cardSystem, card.getId());
                    if (c != null) {
                        deploymentCards.add((com.swia.datasets.cards.DeploymentCard) c);
                    }
                }
            }
        }
        Collections.sort(deploymentCards, DeploymentCardSafeComparator.Instance);
        for (com.swia.datasets.cards.DeploymentCard card : deploymentCards) {
            valid &= army.add(card);
        }
        for (com.swia.datasets.cards.CommandCard card : commandCards) {
            valid &= army.add(card);
        }
        return valid ? army : null;
    }

    public static void save(OutputStream stream, Army army) throws IOException {
        List<Card> cards = new ArrayList<>();
        CardSystem cardSystem = CardSystem.valueOf(army.getCardSystem().toString().toUpperCase());
        for (com.swia.datasets.cards.Card card : army.getCards(com.swia.datasets.cards.CardType.DEPLOYMENT)) {
            Card c = IavdFile.getCard(cardSystem, card.getStringId());
            if (c == null) {
                Log.e("VassalParser", card.getStringId());
                continue;
            }
            cards.add(c);
        }
        for (com.swia.datasets.cards.Card card : army.getCards(com.swia.datasets.cards.CardType.COMMAND)) {
            Card c = IavdFile.getCard(cardSystem, card.getStringId());
            if (c == null) {
                Log.e("VassalParser", card.getStringId());
                continue;
            }
            cards.add(c);
        }
        IavdFile.save(stream, cards);
    }
}
