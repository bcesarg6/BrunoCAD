package com.example.brunocad.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.brunocad.R;

public class Toaster {

    private static Toast torrada;

    /**
     * toast longo de propósito geral
     */
    public static void longToast(String msg, Context context) {
        if (torrada != null) torrada.cancel();
        torrada = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        torrada.show();
    }

    /**
     * toast curto de propósito geral
     */
    public static void shortToast(String msg, Context context) {
        if (torrada != null) torrada.cancel();
        torrada = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        torrada.show();
    }

    /**
     * toast de erro generico
     */
    public static void erroToast( Context context) {
        if (torrada != null) torrada.cancel();
        torrada = Toast.makeText(context, context.getResources().getString(R.string.erro_generico), Toast.LENGTH_SHORT);
        torrada.show();
    }
}
