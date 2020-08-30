package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;
import com.swia.iabuilder.models.Faction;

public class DeploymentCardConstraints implements CardConstraint {

    private final CardConstraint constraint;

    public DeploymentCardConstraints(Faction faction) {
        switch (faction) {
            case REBEL:
                constraint = new SaskaTeft();
                break;
            case IMPERIAL:
                constraint = new ImperialTemporaryAlliance();
                break;
            case MERCENARY:
                constraint = new MercenaryCardConstraints();
                break;
            default:
                throw new IllegalArgumentException(faction.getName());
        }
    }

    @Override
    public void reset() {
        constraint.reset();
    }

    @Override
    public void add(Card card) {
        constraint.add(card);
    }

    @Override
    public void remove(Card card) {
        constraint.remove(card);
    }

    @Override
    public boolean check(Card card) {
        return constraint.check(card);
    }

    @Override
    public boolean isValid(Card card) {
        return constraint.isValid(card);
    }
}
