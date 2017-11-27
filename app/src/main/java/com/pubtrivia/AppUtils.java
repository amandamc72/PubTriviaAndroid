package com.pubtrivia;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AppUtils {

    public static void showPopup(Context context, String message) {
        AlertDialog.Builder alertResponse = new AlertDialog.Builder(context);
        alertResponse.setMessage(message);
        alertResponse.setPositiveButton(R.string.popup_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alertResponse.show();

    }
}
