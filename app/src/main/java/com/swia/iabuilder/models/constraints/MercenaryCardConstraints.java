package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class MercenaryCardConstraints implements CardConstraint {

    private int overlaps;
    private List<CardAllowance> allowanceList;

    public MercenaryCardConstraints() {
        overlaps = 0;
        allowanceList = new ArrayList<>();
        allowanceList.add(new MercenaryTemporaryAlliance());
        allowanceList.add(new EliteJawa());
        allowanceList.add(new DoctorAphra());
    }

    private boolean isOverlap(Card card) {
        int n = 0;
        for (CardAllowance allowance : allowanceList) {
            n += allowance.isValid(card) ? 1 : 0;
        }
        return n > 1 && applicable(card);
    }

    private boolean applicable(Card card) {
        for (CardAllowance allowance : allowanceList) {
            if (allowance.applicable(card)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAll(Card card) {
        for (CardAllowance allowance : allowanceList) {
            if (allowance.check(card)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reset() {
        overlaps = 0;
        for (CardAllowance allowance : allowanceList) {
            allowance.reset();
        }
    }

    @Override
    public void add(Card card) {
        if (isOverlap(card)) {
            overlaps++;
        } else {
            for (CardAllowance allowance : allowanceList) {
                allowance.add(card);
            }
        }
    }

    @Override
    public void remove(Card card) {
        if (overlaps > 0 && isOverlap(card)) {
            overlaps--;
        } else {
            for (CardAllowance allowance : allowanceList) {
                allowance.remove(card);
            }
        }
    }

    @Override
    public boolean check(Card card) {
        if (!applicable(card)) {
            return true;
        }
        int n = 0;
        for (CardAllowance allowance : allowanceList) {
            n += allowance.getAllowance();
        }
        if (n > overlaps) {
            return checkAll(card);
        }
        return false;
    }

    @Override
    public boolean isValid(Card card) {
        for (CardAllowance allowance : allowanceList) {
            if (allowance.isValid(card)) {
                return true;
            }
        }
        return false;
    }
}
