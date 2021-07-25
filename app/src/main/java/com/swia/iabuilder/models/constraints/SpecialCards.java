package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.models.Faction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpecialCards implements CardConstraint {

    private final ArrayList<CrossFactionConstraint> constraints = new ArrayList<>();
    private final Army army;

    public SpecialCards(Army army) {
        this.army = army;
    }

    private CrossFactionConstraint getCrossFactionContraint(Card card) {
        Faction faction = army.getFaction();
        switch (card.getId()) {
            case ImperialTemporaryAlliance.CARD_ID:
                return faction == Faction.IMPERIAL ? new ImperialTemporaryAlliance() : null;
            case MercenaryTemporaryAlliance.CARD_ID:
                return faction == Faction.MERCENARY ? new MercenaryTemporaryAlliance() : null;
            case SaskaTeft.CARD_ID:
                return faction == Faction.REBEL ? new SaskaTeft() : null;
            case EliteJawa.CARD_ID:
                return faction == Faction.MERCENARY ? new EliteJawa() : null;
            case DoctorAphra.CARD_ID:
                return faction == Faction.MERCENARY ? new DoctorAphra() : null;
            default:
                return null;
        }
    }

    private static CrossFactionConstraint[] getCrossFactionContraints(Faction faction) {
        switch (faction) {
            case REBEL:
                return new CrossFactionConstraint[] { new SaskaTeft() };
            case IMPERIAL:
                return new CrossFactionConstraint[] { new ImperialTemporaryAlliance() };
            case MERCENARY:
                return new CrossFactionConstraint[] { new DoctorAphra(), new EliteJawa(), new MercenaryTemporaryAlliance() };
        }
        return new CrossFactionConstraint[] { };
    }

    @Override
    public void reset() {
        constraints.clear();
    }

    private void reassign() {
        ArrayList<Card> cards = new ArrayList<>();
        for (CrossFactionConstraint constraint: constraints) {
            cards.addAll(constraint.getCards());
            constraint.reset();
        }
        for (Card card : cards) {
            add(card);
        }
    }

    @Override
    public void add(Card card) {
        CrossFactionConstraint constraint = getCrossFactionContraint(card);
        if (constraint != null) {
            constraints.add(constraint);
            Collections.sort(constraints, new SpacialCardsComparator());
            reassign();
            return;
        }
        for (int i = 0; i < constraints.size(); i++) {
            constraint = constraints.get(i);
            if (constraint.isAllowed(card)) {
                constraint.add(card);
                return;
            }
        }
    }

    @Override
    public void remove(Card card) {
        if (getCrossFactionContraint(card) != null) {
            for (int i = constraints.size() - 1; i >= 0; i--) {
                CrossFactionConstraint constraint = constraints.get(i);
                if (constraint.getCardId() == card.getId()) {
                    List<Card> cards = constraint.getCards();
                    constraints.remove(i);
                    for (Card c : cards) {
                        army.getDeck(CardType.DEPLOYMENT).remove(c);
                    }
                    return;
                }
            }
        }
    }

    @Override
    public boolean isValid(Card card) {
        for (int i = 0; i < constraints.size(); i++) {
            if (constraints.get(i).isValid(card)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPotentiallyValid(Card card, Faction faction) {
        if (isValid(card)) {
            return true;
        }
        CrossFactionConstraint[] constraints = getCrossFactionContraints(faction);
        for (CrossFactionConstraint constraint : constraints) {
            if (constraint.isValid(card)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAllowed(Card card) {
        for (int i = 0; i < constraints.size(); i++) {
            if (constraints.get(i).isAllowed(card)) {
                return true;
            }
        }
        return false;
    }

    private static class SpacialCardsComparator implements Comparator<CrossFactionConstraint> {
        @Override
        public int compare(CrossFactionConstraint c1, CrossFactionConstraint c2) {
            return c1.getPriority() - c2.getPriority();
        }
    }
}
