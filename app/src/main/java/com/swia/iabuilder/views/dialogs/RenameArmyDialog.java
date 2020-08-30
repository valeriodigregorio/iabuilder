package com.swia.iabuilder.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.swia.iabuilder.R;
import com.swia.iabuilder.utils.DoubleClickHelper;

public class RenameArmyDialog implements DialogInterface.OnClickListener {

    private final Context context;
    private final Dialog dialog;
    private final String name;
    private OnRenameListener listener;

    public RenameArmyDialog(Context context, String name) {
        this.context = context;
        this.name = name;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.dialog_army_edit).setTitle(context.getResources().getString(R.string.dialog_army_edit_title));
        dialogBuilder.setPositiveButton(android.R.string.ok, this);
        dialogBuilder.setNegativeButton(android.R.string.cancel, this);

        dialog = dialogBuilder.create();
    }

    public void show() {
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        EditText edtName = dialog.findViewById(R.id.edtName);
        if (edtName != null) {
            edtName.setText(name);
            edtName.setSelection(name.length());
        }
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
            Toast.makeText(context, context.getString(R.string.error_renaming_canceled), Toast.LENGTH_SHORT).show();
        }
        dialog.cancel();
    }

    private void confirm(String name) {
        if (listener != null) {
            listener.onRenamed(name);
        }
    }

    public RenameArmyDialog setListener(OnRenameListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnRenameListener {
        void onRenamed(String name);
    }
}
