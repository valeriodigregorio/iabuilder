package com.swia.iabuilder.models;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.CompanionCard;
import com.swia.datasets.cards.DeploymentCard;
import com.swia.datasets.cards.Size;
import com.swia.iabuilder.models.constraints.CardUniqueNames;
import com.swia.iabuilder.models.constraints.DeploymentCardConstraints;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DeploymentDeck extends Deck {

    public static final int MAX_POINTS = 40;
    private static final int CROSS_TRAINING = 24;
    private static final int MAUL = 152;
    private static final int IACP_4LOM = 190;
    private static final int IACP_DARKSABER = 202;
    private static final int IACP_MARA_JADE = 206;

    private Faction faction = null;
    private DeploymentCardConstraints constraints = null;
    private final CardUniqueNames uniqueNames = new CardUniqueNames();

    private Set<String> commandDeckRestrictions = new HashSet<>();
    private HashSet<String> skirmishUpgradesRestrictions = new HashSet<>();
    private boolean anyNonUnique = false;
    private boolean anyNonSpectre = false;
    private boolean hasMaul = false;
    private boolean hasDarksaber = false;

    public DeploymentDeck() {
        super(CardType.DEPLOYMENT, MAX_POINTS);
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
        constraints = new DeploymentCardConstraints(faction);
        clear();
    }

    @Override
    public boolean isValid(Card card, Army army) {
        return card instanceof DeploymentCard && constraints != null && constraints.isValid(card);
    }

    @Override
    public boolean isAllowed(Card card) {
        if (super.isAllowed(card) && uniqueNames.check(card)) {
            return constraints != null && constraints.check(card);
        }
        return false;
    }

    public boolean isAllowedUpgrade(DeploymentCard card) {
        if (card == null || !card.isUpgrade()) {
            return false;
        }

        Set<String> restrictions = getUpgradeRestrictions();
        if (!restrictions.isEmpty()) {
            for (String restriction : card.getRestrictions()) {
                String[] list = restriction.split(",\\s+");
                boolean match = true;
                for (String r : list) {
                    if (!restrictions.contains(r)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return true;
                }
            }
        }

        return card.getRestrictions().length == 0;
    }

    @Override
    protected void onAdd(Card card) {
        constraints.add(card);
        uniqueNames.add(card);
        updateCommandDeckRestrictions(card);
        updateUpgradeRestrictions(card);
    }

    @Override
    protected void onRemove(Card card) {
        constraints.remove(card);
        uniqueNames.remove(card);
        resetRestrictions();
    }

    @Override
    protected void onClear() {
        if (uniqueNames != null) {
            uniqueNames.reset();
        }
        if (constraints != null) {
            constraints.reset();
        }
        resetRestrictions();
    }

    private void resetRestrictions() {
        commandDeckRestrictions = new HashSet<>();
        skirmishUpgradesRestrictions = new HashSet<>();
        anyNonUnique = false;
        anyNonSpectre = false;
        hasMaul = false;
        hasDarksaber = false;
        for (Card card : getCards()) {
            updateCommandDeckRestrictions(card);
            updateUpgradeRestrictions(card);
        }
    }

    public Set<String> getCommandDeckRestrictions() {
        return commandDeckRestrictions;
    }

    public Set<String> getUpgradeRestrictions() {
        return skirmishUpgradesRestrictions;
    }

    private void updateCommandDeckRestrictions(Card card) {
        DeploymentCard deploymentCard = ((DeploymentCard) card);
        commandDeckRestrictions.add("Any Figure");

        if (deploymentCard.getSize() == Size.SMALL) {
            commandDeckRestrictions.add("Any Small Figure");
        } else if (deploymentCard.getSize() == Size.LARGE) {
            commandDeckRestrictions.add("Any Large Figure");
        }

        if (deploymentCard.isMassive()) {
            commandDeckRestrictions.add("Any Massive Figure");
        }

        for (CompanionCard c : CompanionCard.getCardsFor(deploymentCard)) {
            Collections.addAll(commandDeckRestrictions, c.getTraits());
        }

        Affiliation affiliation = deploymentCard.getAffiliation();
        if (deploymentCard.getCardSystem() == CardSystem.IACP) {
            switch (deploymentCard.getId()) {
                case IACP_4LOM:
                    commandDeckRestrictions.add("Brawler");
                    commandDeckRestrictions.add("Creature");
                    commandDeckRestrictions.add("Large Creature");
                    commandDeckRestrictions.add("Force User");
                    commandDeckRestrictions.add(affiliation.getName() + " Force User");
                    commandDeckRestrictions.add("Guardian");
                    commandDeckRestrictions.add("Heavy Weapon");
                    commandDeckRestrictions.add("Hunter");
                    commandDeckRestrictions.add("Leader");
                    commandDeckRestrictions.add("Smuggler");
                    commandDeckRestrictions.add("Spy");
                    commandDeckRestrictions.add("Trooper");
                    commandDeckRestrictions.add("Vehicle");
                    commandDeckRestrictions.add("Any Ready Vehicle");
                    commandDeckRestrictions.add("Wookiee");
                    break;
                case MAUL:
                    hasMaul = true;
                    break;
                case IACP_DARKSABER:
                    hasDarksaber = true;
                    break;
                case IACP_MARA_JADE:
                    affiliation = faction.getAffiliation();
                    switch (affiliation) {
                        case REBEL:
                            commandDeckRestrictions.add("Guardian");
                            break;
                        case IMPERIAL:
                            commandDeckRestrictions.add("Hunter");
                            break;
                        case MERCENARY:
                            commandDeckRestrictions.add("Smuggler");
                            break;
                    }
                    break;
            }

            if (hasDarksaber && hasMaul) {
                String imperial = Affiliation.IMPERIAL.getName();
                commandDeckRestrictions.add(imperial);
                commandDeckRestrictions.add("Any " + imperial + " Figure");
                commandDeckRestrictions.add(imperial + " Force User");
                commandDeckRestrictions.add(imperial + " Maul");
                hasMaul = false;
            }
        }

        if (deploymentCard.getId() == CROSS_TRAINING) {
            commandDeckRestrictions.add("Spy");
        }

        boolean isUpdate = false;
        String[] traits = deploymentCard.getTraits();
        for (String trait : traits) {
            if (!trait.equals("Skirmish Upgrade")) {
                commandDeckRestrictions.add(trait);
                switch (trait) {
                    case "Vehicle":
                        commandDeckRestrictions.add("Any Ready Vehicle");
                        break;
                    case "Force User":
                        commandDeckRestrictions.add(affiliation.getName() + " Force User");
                        break;
                    case "Creature":
                        if (deploymentCard.getSize() == Size.LARGE) {
                            commandDeckRestrictions.add("Large Creature");
                        }
                        break;
                }
            } else {
                isUpdate = true;
            }
        }

        if (!isUpdate) {
            if (deploymentCard.isUnique()) {
                commandDeckRestrictions.add("Any Unique Figure");
                commandDeckRestrictions.add(deploymentCard.getName());
                commandDeckRestrictions.add(affiliation.getName() + " " + deploymentCard.getName());
            }
            if (affiliation != Affiliation.NEUTRAL) {
                commandDeckRestrictions.add(affiliation.getName());
                commandDeckRestrictions.add("Any " + affiliation.getName() + " Figure");
            }
        }
    }

    public void updateUpgradeRestrictions(Card card) {
        DeploymentCard deploymentCard = ((DeploymentCard) card);
        if (deploymentCard.isSkirmishUpgrade()) {
            return;
        }

        skirmishUpgradesRestrictions.add(deploymentCard.getName());

        Affiliation affiliation = deploymentCard.getAffiliation();
        skirmishUpgradesRestrictions.add(affiliation.getName());

        int n = deploymentCard.getDeploymentGroup();
        skirmishUpgradesRestrictions.add(n + " figure" + (n == 1 ? "" : "s"));

        boolean isGuardian = false;
        for (String trait : deploymentCard.getTraits()) {
            if (!trait.equals("Squad Upgrade")) {
                skirmishUpgradesRestrictions.add(trait);
            }
            isGuardian |= trait.equals("Guardian");
        }

        if (!deploymentCard.isMassive()) {
            skirmishUpgradesRestrictions.add("Non-massive");
        }

        if (deploymentCard.isUnique()) {
            skirmishUpgradesRestrictions.add("Unique");
            if (isGuardian) {
                skirmishUpgradesRestrictions.add("Unique Guardian");
            }
        } else {
            anyNonUnique = true;
            skirmishUpgradesRestrictions.add("Non-unique");
        }

        if (!deploymentCard.isSpectre()) {
            anyNonSpectre = true;
        }

        if (!anyNonUnique) {
            skirmishUpgradesRestrictions.add("All Unique Figures");
        } else {
            skirmishUpgradesRestrictions.remove("All Unique Figures");
        }

        if (!anyNonSpectre) {
            skirmishUpgradesRestrictions.add("All Spectre Figures");
        } else {
            skirmishUpgradesRestrictions.remove("All Spectre Figures");
        }
    }
}
