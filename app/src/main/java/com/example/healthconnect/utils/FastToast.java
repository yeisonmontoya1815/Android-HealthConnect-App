package com.example.healthconnect.utils;

import android.content.Context;
import android.widget.Toast;

public class FastToast {
    private FastToast() {
    }

    /**
     * Show a toast message with the given message
     *
     * @param context The context to show the toast
     * @param message The message to show
     */
    public static void show(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
