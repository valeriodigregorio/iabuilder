package com.swia.iabuilder.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.swia.iabuilder.R;
import com.swia.iabuilder.utils.DoubleClickHelper;

public class DeleteCardDialog implements DialogInterface.OnClickListener {

    private final Context context;
    private final Dialog dialog;
    private OnDeleteListener listener;

    public DeleteCardDialog(Context context) {
        this.context = context;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.dialog_card_removal_title))
                .setMessage(context.getResources().getString(R.string.dialog_card_removal_message));
        dialogBuilder.setPositiveButton(android.R.string.ok, this);
        dialogBuilder.setNegativeButton(android.R.string.cancel, this);

        dialog = dialogBuilder.create();
    }

    public void show() {
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if (!DoubleClickHelper.testAndSet()) {
            return;
        }

        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (listener != null) {
                listener.onDeleted();
            }
        } else {
            Toast.makeText(context, context.getString(R.string.error_removal_canceled), Toast.LENGTH_SHORT).show();
            dialog.cancel();
        }
    }

    public DeleteCardDialog setListener(OnDeleteListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnDeleteListener {
        void onDeleted();
    }
}
