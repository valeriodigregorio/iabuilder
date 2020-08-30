package com.swia.iabuilder.models.comparators;

import com.swia.iabuilder.models.constraints.EliteJawa;
import com.swia.iabuilder.models.constraints.ImperialTemporaryAlliance;
import com.swia.iabuilder.models.constraints.MercenaryTemporaryAlliance;
import com.swia.iabuilder.models.constraints.SaskaTeft;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;

import java.util.Comparator;

public class DeploymentCardSafeComparator implements Comparator<Card> {

    public static final Comparator<Card> Instance = new DeploymentCardSafeComparator();

    private static final int DARTH_VADER = 25;
    private static final int DRIVEN_BY_HATRED = 147;
    private static final int CHEWBACCA = 22;
    private static final int WOOKIEE_AVENGER = 160;
    private static final int HAN_SOLO = 50;
    private static final int ROGUE_SMUGGLER = 157;

    public static int isSpecialCard(DeploymentCard card) {
        switch (card.getId()) {
            case ImperialTemporaryAlliance.CARD_ID:
                return 10;
            case MercenaryTemporaryAlliance.CARD_ID:
                return 20;
            case DARTH_VADER:
                return 30;
            case DRIVEN_BY_HATRED:
                return 40;
            case CHEWBACCA:
                return 50;
            case WOOKIEE_AVENGER:
                return 60;
            case HAN_SOLO:
                return 70;
            case ROGUE_SMUGGLER:
                return 80;
            case SaskaTeft.CARD_ID:
                return 90;
            case EliteJawa.CARD_ID:
                return 100;
            default:
                return 0;
        }
    }

    @Override
    public int compare(Card c1, Card c2) {
        DeploymentCard card1 = (c1 instanceof DeploymentCard ? (DeploymentCard) c1 : null);
        DeploymentCard card2 = (c2 instanceof DeploymentCard ? (DeploymentCard) c2 : null);
        if (card1 == null || card2 == null) {
            return card1 != null ? -1 : (card2 != null ? 1 : 0);
        }

        int p1 = isSpecialCard(card1);
        int p2 = isSpecialCard(card2);
        if (p1 > 0 && p2 > 0) {
            return Integer.compare(p1, p2);
        } else {
            if (p1 > 0) {
                return -1;
            }
            if (p2 > 0) {
                return 1;
            }
        }
        boolean u1 = card1.isUpgrade();
        boolean u2 = card2.isUpgrade();
        if (u1 || u2) {
            if (!u1) {
                return -1;
            }
            if (!u2) {
                return 1;
            }
        }
        return card1.compareTo(card2);
    }
}
