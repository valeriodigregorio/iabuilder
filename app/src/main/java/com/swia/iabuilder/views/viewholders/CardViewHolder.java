package com.swia.iabuilder.views.viewholders;

import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.swia.datasets.cards.Card;
import com.swia.iabuilder.R;

import java.util.Comparator;

public class CardViewHolder extends CollectionViewHolder<CardViewHolder.CardEntry> {

    private final int ratio;
    private final ImageView card;
    private final ImageView shortlistMark;
    private static Drawable placeholder = null;

    public CardViewHolder(@NonNull View itemView, int ratio) {
        super(itemView);
        this.ratio = ratio;
        card = itemView.findViewById(R.id.imgCard);
        shortlistMark = itemView.findViewById(R.id.imgShortlistMark);
        if (placeholder == null) {
            placeholder = new ColorDrawable(itemView.getResources().getColor(R.color.colorCleanBackground, null));
        }
    }

    @Override
    public void onBind(CardEntry item) {
        Card card = item.getCard();
        int drawableId = card.getDrawableId();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(itemView.getResources(), drawableId, options);
        options.inJustDecodeBounds = false;
        int width = options.outWidth / ratio;
        int height = options.outHeight / ratio;
        Picasso.get()
                .load(drawableId)
                .resize(width, height)
                .placeholder(placeholder)
                .into(this.card);
        setEnabled(item.isEnabled());
        setShortlisted(item.isShortlisted());
    }

    private void setEnabled(boolean enabled) {
        if (enabled) {
            int color = ContextCompat.getColor(itemView.getContext(), R.color.colorEnabledForeground);
            card.setForeground(new ColorDrawable(color));
        } else {
            int color = ContextCompat.getColor(itemView.getContext(), R.color.colorDisabledForeground);
            card.setForeground(new ColorDrawable(color));
        }
    }

    private void setShortlisted(boolean shortlisted) {
        shortlistMark.setVisibility(shortlisted ? View.VISIBLE : View.GONE);
    }

    public static class CardEntry implements Comparable<CardEntry> {

        private final Card card;
        private final boolean enabled;
        private final boolean shortlisted;

        public CardEntry(Card card, boolean enabled, boolean shortlisted) {
            this.card = card;
            this.enabled = enabled;
            this.shortlisted = shortlisted;
        }

        public Card getCard() {
            return card;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public boolean isShortlisted() {
            return shortlisted;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            CardEntry entry = (obj instanceof CardEntry ? (CardEntry) obj : null);
            return entry != null && compareTo(entry) == 0;
        }

        @Override
        public int compareTo(CardEntry entry) {
            if (isShortlisted() != entry.isShortlisted()) {
                return (isShortlisted() ? -1 : 1);
            }
            int diff = card.compareTo(entry.getCard());
            if (diff == 0) {
                if (isEnabled() != entry.isEnabled()) {
                    return (isEnabled() ? -1 : 1);
                }
            }
            return diff;
        }

        public int compareByName(CardEntry entry) {
            if (isShortlisted() != entry.isShortlisted()) {
                return (isShortlisted() ? -1 : 1);
            }
            int diff = card.compareByName(entry.getCard());
            if (diff == 0) {
                if (isEnabled() != entry.isEnabled()) {
                    return (isEnabled() ? -1 : 1);
                }
            }
            return diff;
        }

        public int compareByCost(CardEntry entry) {
            if (isShortlisted() != entry.isShortlisted()) {
                return (isShortlisted() ? -1 : 1);
            }
            int diff = card.compareByCost(entry.getCard());
            if (diff == 0) {
                if (isEnabled() != entry.isEnabled()) {
                    return (isEnabled() ? -1 : 1);
                }
            }
            return diff;
        }

        public static Comparator<CardEntry> getComparator(String field) {
            switch (field) {
                case "Cost":
                    return CardViewHolder.CardEntry::compareByCost;
                case "Name":
                    return CardViewHolder.CardEntry::compareByName;
                default:
                    return CardViewHolder.CardEntry::compareTo;
            }
        }
    }
}