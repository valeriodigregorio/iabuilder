package com.swia.iabuilder.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.swia.datasets.cards.CardSystem;
import com.swia.iabuilder.R;
import com.swia.iabuilder.utils.DoubleClickHelper;

public class CreateArmyDialog implements DialogInterface.OnClickListener {

    private final Context context;
    private final Dialog dialog;
    private OnCreateListener listener;

    public CreateArmyDialog(Context context) {
        this.context = context;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        dialogBuilder.setView(R.layout.dialog_army_create).setTitle(this.context.getResources().getString(R.string.dialog_army_create_title));
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

        EditText edtName = dialog.findViewById(R.id.edtName);
        String name = edtName.getText().toString();
        if (which == DialogInterface.BUTTON_POSITIVE) {
            if (!name.isEmpty()) {
                confirm(name);
                return;
            } else {
                Toast.makeText(context, context.getString(R.string.error_invalid_name), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, context.getString(R.string.error_creation_canceled), Toast.LENGTH_SHORT).show();
        }
        dialog.cancel();
    }

    private void confirm(String name) {
        CardSystem cardSystem;
        RadioGroup grpCardSystem = dialog.findViewById(R.id.grpCardSystem);
        int selectedId = grpCardSystem.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.radFfg:
                cardSystem = CardSystem.FFG;
                break;
            case R.id.radIacp:
                cardSystem = CardSystem.IACP;
                break;
            default:
                RadioButton button = dialog.findViewById(selectedId);
                throw new IllegalArgumentException((String) button.getText());
        }
        if (listener != null) {
            listener.onCreated(cardSystem, name);
        }
    }

    public CreateArmyDialog setListener(OnCreateListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnCreateListener {
        void onCreated(CardSystem cardSystem, String name);
    }
}
