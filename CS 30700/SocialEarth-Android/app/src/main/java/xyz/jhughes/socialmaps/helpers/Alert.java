package xyz.jhughes.socialmaps.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Alert {
    public static void createAlertDialog(String title, String descr, Context context) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(descr)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void networkError(Context context) {
        Toast.makeText(context, "No network connection", Toast.LENGTH_LONG).show();
    }

    public static void generalError(Context context) {
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
    }
}
