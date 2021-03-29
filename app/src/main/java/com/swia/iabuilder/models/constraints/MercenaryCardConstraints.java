package com.swia.iabuilder.models.constraints;

import com.swia.datasets.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class MercenaryCardConstraints implements CardConstraint {

    private int overlaps;
    private final List<CardAllowance> allowanceList = new ArrayList<>();

    public MercenaryCardConstraints() {
        overlaps = 0;
        allowanceList.add(new DoctorAphra());
        allowanceList.add(new EliteJawa());
        allowanceList.add(new MercenaryTemporaryAlliance());
    }

    private boolean isOverlap(Card card) {
        if (!applicable(card)) {
            return false;
        }
        int n = 0;
        for (CardAllowance allowance : allowanceList) {
            if (allowance.isValid(card)) {
                n++;
            }
        }
        return n > 1;
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
                int old = allowance.getAllowance();
                allowance.add(card);
                if (old != allowance.getAllowance()) {
                    break;
                }
            }
        }
    }

    @Override
    public void remove(Card card) {
        if (overlaps > 0 && isOverlap(card)) {
            overlaps--;
        } else {
            for (CardAllowance allowance : allowanceList) {
                int old = allowance.getAllowance();
                allowance.remove(card);
                if (old != allowance.getAllowance()) {
                    break;
                }
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
        return n > overlaps && checkAll(card);
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
