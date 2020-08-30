package com.swia.iabuilder.models;

import com.swia.iabuilder.R;
import com.swia.datasets.cards.Affiliation;

public enum Faction {
    REBEL("Rebel", R.drawable.ic_rebel),
    IMPERIAL("Imperial", R.drawable.ic_imperial),
    MERCENARY("Mercenary", R.drawable.ic_mercenary);

    private final String name;
    private final int drawableId;

    Faction(String name, int drawableId) {
        this.name = name;
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public Affiliation getAffiliation() {
        return Affiliation.valueOf(name.toUpperCase());
    }

    public static Faction fromAffiliation(Affiliation affiliation) {
        if (affiliation == Affiliation.NEUTRAL) {
            return null;
        } else {
            return Faction.valueOf(affiliation.toString().toUpperCase());
        }
    }
}
