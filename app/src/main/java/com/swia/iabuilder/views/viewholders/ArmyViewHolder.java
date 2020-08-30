package com.swia.iabuilder.views.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.swia.iabuilder.R;
import com.swia.iabuilder.models.Army;

public class ArmyViewHolder extends CollectionViewHolder<Army> {

    private final ImageView cardSystem;
    private final TextView name;
    private final TextView description;
    private final TextView results;

    public ArmyViewHolder(View itemView) {
        super(itemView);
        cardSystem = itemView.findViewById(R.id.imgCardSystem);
        name = itemView.findViewById(R.id.txtName);
        description = itemView.findViewById(R.id.txtDescription);
        results = itemView.findViewById(R.id.txtResults);
    }

    public void onBind(Army army) {
        switch (army.getCardSystem()) {
            case FFG:
                cardSystem.setImageResource(R.drawable.ic_ffg);
                break;
            case IACP:
                cardSystem.setImageResource(R.drawable.ic_iacp);
                break;
            default:
                throw new IllegalArgumentException(army.getCardSystem().toString());
        }
        name.setText(army.getName().toUpperCase());
        description.setText(army.getDefaultName());
        results.setText(army.getVictories() + "/" + army.getDefeats());
    }
}
