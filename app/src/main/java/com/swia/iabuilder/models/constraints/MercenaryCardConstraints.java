package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.DeploymentCard;

public class MercenaryCardConstraints implements CardConstraint {

    private final EliteJawa eliteJawa;
    private final MercenaryTemporaryAlliance temporaryAlliance;

    private int overlaps;

    public MercenaryCardConstraints() {
        overlaps = 0;
        eliteJawa = new EliteJawa();
        temporaryAlliance = new MercenaryTemporaryAlliance();
    }

    private boolean isOverlap(Card card) {
        DeploymentCard c = (DeploymentCard) card;
        return applicable(c) && temporaryAlliance.isValid(card) && eliteJawa.isValid(card);
    }

    private boolean applicable(DeploymentCard card) {
        return eliteJawa.applicable(card) || temporaryAlliance.applicable(card);
    }

    @Override
    public void reset() {
        overlaps = 0;
        eliteJawa.reset();
        temporaryAlliance.reset();
    }

    @Override
    public void add(Card card) {
        if (isOverlap(card)) {
            overlaps++;
        } else {
            eliteJawa.add(card);
            temporaryAlliance.add(card);
        }
    }

    @Override
    public void remove(Card card) {
        if (overlaps > 0 && isOverlap(card)) {
            overlaps--;
        } else {
            eliteJawa.remove(card);
            temporaryAlliance.remove(card);
        }
    }

    @Override
    public boolean check(Card card) {
        if (!applicable((DeploymentCard) card)) {
            return true;
        }
        if (eliteJawa.getAllowance() + temporaryAlliance.getAllowance() > overlaps) {
            return temporaryAlliance.check(card) || eliteJawa.check(card);
        }
        return false;
    }

    @Override
    public boolean isValid(Card card) {
        return eliteJawa.isValid(card) || temporaryAlliance.isValid(card);
    }
}
