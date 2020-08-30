package com.swia.iabuilder.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swia.iabuilder.R;
import com.swia.iabuilder.utils.DoubleClickHelper;

public class ResultsDialog implements DialogInterface.OnClickListener {

    private final Context context;
    private final Dialog dialog;
    private OnChangedListener listener;
    private int victories;
    private int defeats;

    public ResultsDialog(Context context, int victories, int defeats) {
        this.context = context;
        this.victories = victories;
        this.defeats = defeats;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.dialog_results).setTitle(context.getResources().getString(R.string.dialog_results_title));
        dialogBuilder.setPositiveButton(android.R.string.ok, this);

        dialog = dialogBuilder.create();
    }

    public void show() {
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TextView txtVictories = dialog.findViewById(R.id.txtVictories);
        if (txtVictories != null) {
            txtVictories.setText(String.valueOf(victories));
        }

        TextView txtDefeats = dialog.findViewById(R.id.txtDefeats);
        if (txtDefeats != null) {
            txtDefeats.setText(String.valueOf(defeats));
        }

        FloatingActionButton fab = dialog.findViewById(R.id.btnDecrementVictories);
        fab.setOnClickListener(v -> {
            victories -= 1;
            if (victories < 0) {
                victories = 0;
            }
            if (txtVictories != null) {
                txtVictories.setText(String.valueOf(victories));
            }
        });

        fab = dialog.findViewById(R.id.btnDecrementDefeats);
        fab.setOnClickListener(v -> {
            defeats -= 1;
            if (defeats < 0) {
                defeats = 0;
            }
            if (txtDefeats != null) {
                txtDefeats.setText(String.valueOf(defeats));
            }
        });

        fab = dialog.findViewById(R.id.btnIncrementVictories);
        fab.setOnClickListener(v -> {
            victories += 1;
            if (txtVictories != null) {
                txtVictories.setText(String.valueOf(victories));
            }
        });

        fab = dialog.findViewById(R.id.btnIncrementDefeats);
        fab.setOnClickListener(v -> {
            defeats += 1;
            if (txtDefeats != null) {
                txtDefeats.setText(String.valueOf(defeats));
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if (!DoubleClickHelper.testAndSet()) {
            return;
        }

        if (which == DialogInterface.BUTTON_POSITIVE) {
            confirm(victories, defeats);
            return;
        }

        dialog.cancel();
    }

    private void confirm(int victories, int defeats) {
        if (listener != null) {
            listener.onChanged(victories, defeats);
        }
    }

    public ResultsDialog setListener(OnChangedListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnChangedListener {
        void onChanged(int victories, int defeats);
    }
}
