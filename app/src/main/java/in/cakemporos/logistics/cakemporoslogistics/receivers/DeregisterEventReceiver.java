package in.cakemporos.logistics.cakemporoslogistics.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.activities.SplashActivity;
import in.cakemporos.logistics.cakemporoslogistics.staticvals.NotificationIDS;

/**
 * Created by roger on 9/18/2016.
 */
public class DeregisterEventReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(final Context context, Intent intent) {
        //show dialog

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
        nMgr.cancel(NotificationIDS.DEREGISTER);

        AlertDialog dialog = new AlertDialog.Builder(context).setMessage(R.string.logged_out_message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, SplashActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).setCancelable(false)
                .create();
        dialog.show();
    }

}
