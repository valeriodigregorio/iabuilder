package com.swia.iabuilder.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.swia.datasets.cards.Card;
import com.swia.datasets.cards.CardSystem;
import com.swia.datasets.cards.CardType;
import com.swia.datasets.cards.CompanionCard;
import com.swia.datasets.cards.DeploymentCard;
import com.swia.datasets.cards.FormCard;
import com.swia.iabuilder.R;
import com.swia.iabuilder.views.recyclerviews.ViewerRecyclerView;

public class ViewerActivity extends AppCompatActivity {

    private static final String ARG_CARD_SYSTEM = "card_system";
    private static final String ARG_CARD_TYPE = "card_type";
    private static final String ARG_CARD_ID = "card_id";

    public static void show(Context context, Card card) {
        Intent intent = new Intent(context, ViewerActivity.class);
        intent.putExtra(ARG_CARD_SYSTEM, card.getCardSystem().ordinal());
        intent.putExtra(ARG_CARD_TYPE, card.getCardType().ordinal());
        intent.putExtra(ARG_CARD_ID, card.getId());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        Intent intent = getIntent();

        CardSystem cardSystem = CardSystem.values()[intent.getIntExtra(ARG_CARD_SYSTEM, 0)];
        CardType cardType = CardType.values()[intent.getIntExtra(ARG_CARD_TYPE, 0)];
        int cardId = intent.getIntExtra(ARG_CARD_ID, 0);
        Card card = cardType.getCard(cardSystem, cardId);
        if (card != null) {
            ViewerRecyclerView recyclerView = findViewById(R.id.rclViewer);
            recyclerView.add(card);
            if (card instanceof DeploymentCard) {
                for (FormCard c : FormCard.getCardsFor((DeploymentCard) card)) {
                    recyclerView.add(c);
                }
                for (CompanionCard c : CompanionCard.getCardsFor((DeploymentCard) card)) {
                    recyclerView.add(c);
                }
            }
        }
    }
}
