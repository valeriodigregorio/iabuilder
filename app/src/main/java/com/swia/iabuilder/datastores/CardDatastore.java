package com.swia.iabuilder.datastores;

import com.swia.datasets.cards.Affiliation;
import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.CommandCard;
import com.swia.datasets.cards.DeployableCard;
import com.swia.datasets.cards.DeploymentCard;
import com.swia.datasets.cards.Size;
import com.swia.datasets.cards.Trait;
import com.swia.iabuilder.models.Army;
import com.swia.iabuilder.views.viewholders.CardViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CardDatastore {

    private ArrayList<CardViewHolder.CardEntry> entries = new ArrayList<>();

    public CardDatastore(Card[] cards) {
        for(Card card : cards) {
            this.entries.add(new CardViewHolder.CardEntry(card, true, false));
        }
    }

    public CardDatastore whereAffiliationIs(Affiliation affiliation) {
        if (affiliation == null) {
            return this;
        }
        ArrayList<CardViewHolder.CardEntry> entries = new ArrayList<>();
        for(CardViewHolder.CardEntry entry : this.entries) {
            CardType cardType = entry.getCard().getCardType();
            if (cardType == CardType.DEPLOYMENT) {
                DeploymentCard card = (DeploymentCard) entry.getCard();
                if (card.getAffiliation() == affiliation) {
                    entries.add(entry);
                }
            }
        }
        this.entries = entries;
        return this;
    }

    public CardDatastore whereTraitIs(Trait trait) {
        if (trait == null || trait == Trait.ANY) {
            return this;
        }
        ArrayList<CardViewHolder.CardEntry> entries = new ArrayList<>();
        for(CardViewHolder.CardEntry entry : this.entries) {
            CardType cardType = entry.getCard().getCardType();
            if (cardType == CardType.DEPLOYMENT || cardType == CardType.COMPANION) {
                DeployableCard card = (DeployableCard) entry.getCard();
                if (card.hasTrait(trait)) {
                    entries.add(entry);
                }
            }
        }
        this.entries = entries;
        return this;
    }

    public CardDatastore whereRestrictionIs(String restriction) {
        if (restriction == null || restriction.equals("All")) {
            return this;
        }
        ArrayList<CardViewHolder.CardEntry> entries = new ArrayList<>();
        for(CardViewHolder.CardEntry entry : this.entries) {
            CardType cardType = entry.getCard().getCardType();
            if (cardType == CardType.COMMAND) {
                CommandCard card = (CommandCard) entry.getCard();
                String[] restrictions = card.getRestrictions();
                if (restriction.equals("None") && restrictions.length == 0) {
                    entries.add(entry);
                } else {
                    for (String r : restrictions) {
                        if (r.contains(restriction)) {
                            entries.add(entry);
                            break;
                        }
                    }
                }
            }
        }
        this.entries = entries;
        return this;
    }

    private static boolean matchFigureType(DeploymentCard card, String figureType) {
        switch (figureType) {
            case "All":
                return true;
            case "Regular":
                return !card.isElite() && !card.isUnique();
            case "Elite":
                return card.isElite() && !card.isUnique();
            case "Unique":
                return card.isUnique();
            default:
                return false;
        }
    }

    public CardDatastore whereFigureTypeIs(String figureType) {
        if (figureType == null || figureType.equals("All")) {
            return this;
        }
        ArrayList<CardViewHolder.CardEntry> entries = new ArrayList<>();
        for(CardViewHolder.CardEntry entry : this.entries) {
            CardType cardType = entry.getCard().getCardType();
            if (cardType == CardType.DEPLOYMENT) {
                if (matchFigureType((DeploymentCard) entry.getCard(), figureType)) {
                    entries.add(entry);
                }
            }
        }
        this.entries = entries;
        return this;
    }

    private static boolean matchFigureSize(DeployableCard card, String figureSize) {
        switch (figureSize) {
            case "All":
                return true;
            case "Massive":
                return card.isMassive();
            default:
                Size size = Size.fromString(figureSize);
                return card.getSize() == size;
        }
    }

    public CardDatastore whereFigureSizeIs(String figureSize) {
        if (figureSize == null || figureSize.equals("All")) {
            return this;
        }
        ArrayList<CardViewHolder.CardEntry> entries = new ArrayList<>();
        for(CardViewHolder.CardEntry entry : this.entries) {
            CardType cardType = entry.getCard().getCardType();
            if (cardType == CardType.DEPLOYMENT || cardType == CardType.COMPANION) {
                if (matchFigureSize((DeployableCard) entry.getCard(), figureSize)) {
                    entries.add(entry);
                }
            }
        }
        this.entries = entries;
        return this;
    }

    public CardDatastore whereCardContains(String text) {
        if (text == null || text.length() == 0) {
            return this;
        }
        text = text.toUpperCase();
        ArrayList<CardViewHolder.CardEntry> entries = new ArrayList<>();
        for(CardViewHolder.CardEntry entry : this.entries) {
            Card card = entry.getCard();
            StringBuilder content = new StringBuilder(card.getName());
            if (card instanceof DeploymentCard) {
                content.append(((DeploymentCard) card).getDescription());
            }
            if (content.toString().toUpperCase().contains(text)) {
                entries.add(entry);
            }
        }
        this.entries = entries;
        return this;
    }

    public CardDatastore whereValidIn(Army army) {
        if (army == null) {
            return this;
        }
        boolean showDisabled = ConfigStore.getShowDisabled();
        ArrayList<CardViewHolder.CardEntry> entries = new ArrayList<>();
        for(CardViewHolder.CardEntry entry : this.entries) {
            Card card = entry.getCard();
            if (army.isValid(card)) {
                boolean enabled = army.canAdd(card);
                boolean shortlisted = army.isShortlisted(card);
                if (showDisabled || enabled || (shortlisted && army.isAllowed(card))) {
                    entries.add(new CardViewHolder.CardEntry(card, enabled, shortlisted));
                }
            }
        }
        this.entries = entries;
        return this;
    }

    public CardDatastore orderBy(String field) {
        if (field == null) {
            return this;
        }
        Comparator<CardViewHolder.CardEntry> comparator = CardViewHolder.CardEntry.getComparator(field);
        Collections.sort(entries, comparator);
        return this;
    }

    public ArrayList<CardViewHolder.CardEntry> getCollection() {
        return entries;
    }
}
