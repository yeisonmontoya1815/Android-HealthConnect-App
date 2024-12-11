package com.example.healthconnect.utils;

import android.app.AlertDialog;
import android.content.Context;

public class ConfirmationDialog {
    /**
     * Displays a confirmation dialog with a title, message, and actions for "Yes" and "No".
     *
     * @param context            The context where the dialog should be displayed.
     * @param title              The title of the dialog.
     * @param message            The message of the dialog.
     * @param positiveButtonText The text to display on the "Yes" button.
     * @param negativeButtonText The text to display on the "No" button.
     * @param onConfirm          Action to perform when "Yes" is clicked.
     * @param onDeny             Action to perform when "No" is clicked.
     */
    public static void showConfirmationDialog(
            Context context,
            String title,
            String message,
            String positiveButtonText,
            String negativeButtonText,
            Runnable onConfirm,
            Runnable onDeny
    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText,
                        (dialog, which) -> {
                            if (onConfirm != null) {
                                onConfirm.run();
                            }
                        })
                .setNegativeButton(negativeButtonText,
                        (dialog, which) -> {
                            if (onDeny != null) {
                                onDeny.run();
                            }
                            dialog.dismiss();
                        });

        builder.create().show();
    }
}
