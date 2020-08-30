package com.swia.iabuilder.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.swia.iabuilder.R;

public class ReleaseNotesDialog implements DialogInterface.OnClickListener {

    private final Dialog dialog;
    private final String releaseNotes;

    public ReleaseNotesDialog(Context context, int old_release, int new_release) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(R.layout.dialog_release_notes);
        dialogBuilder.setTitle(context.getResources().getString(R.string.dialog_release_notes_title));
        dialogBuilder.setPositiveButton(android.R.string.ok, this);

        String packageName = context.getPackageName();
        StringBuilder builder = new StringBuilder();
        for (int i = new_release; i > old_release; i--) {
            if (i < new_release) {
                builder.append("\n");
            }
            int id = context.getResources().getIdentifier("dialog_release_notes_version_" + i, "string", packageName);
            builder.append(context.getResources().getString(id));
        }
        releaseNotes = builder.toString();
        dialog = dialogBuilder.create();
    }

    public void show() {
        if (releaseNotes != null && releaseNotes.length() > 0){
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            TextView textView = dialog.findViewById(R.id.txtReleaseNotes);
            if (textView != null) {
                textView.setMovementMethod(new ScrollingMovementMethod());
                textView.setText(releaseNotes);
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
}
