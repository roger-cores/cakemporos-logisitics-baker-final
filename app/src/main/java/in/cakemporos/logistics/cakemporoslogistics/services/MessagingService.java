package in.cakemporos.logistics.cakemporoslogistics.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.cakemporos.logistics.cakemporoslogistics.R;
import in.cakemporos.logistics.cakemporoslogistics.activities.OrderHistoryActivity;
import in.cakemporos.logistics.cakemporoslogistics.activities.SplashActivity;
import in.cakemporos.logistics.cakemporoslogistics.staticvals.IntentFilters;
import in.cakemporos.logistics.cakemporoslogistics.staticvals.NotificationIDS;
import in.cakemporos.logistics.cakemporoslogistics.web.services.AuthenticationService;

/**
 * Created by roger on 9/17/2016.
 */
public class MessagingService extends FirebaseMessagingService {
    String TAG = MessagingService.class.getName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String remoteMsg = remoteMessage.getData().get("scope");
            if(remoteMessage.getData().get("scope").toString().equals("deregister")){
                AuthenticationService.logout(this);
                //send broadcast
                Intent intent = new Intent();
                intent.setAction(IntentFilters.LOGOUT);
                sendBroadcast(intent);
                if(remoteMessage.getData().get("title")!=null && remoteMessage.getData().get("body")!=null){

                    Intent notificationIntent = new Intent(this, SplashActivity.class);

                    PendingIntent contentIntent = PendingIntent.getActivity(
                            this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_untitled_1)
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setContentIntent(contentIntent)
                            .setAutoCancel(true)
                            .setContentText(remoteMessage.getData().get("body"));

                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(NotificationIDS.DEREGISTER, mBuilder.build());
                }

            } else if(remoteMessage.getData().get("scope").equals("baker")){
                if(remoteMessage.getData().get("title")!=null && remoteMessage.getData().get("body")!=null){

                    String collapseKey = remoteMessage.getCollapseKey();

                    Intent notificationIntent = new Intent(this, OrderHistoryActivity.class);

                    PendingIntent contentIntent = PendingIntent.getActivity(
                            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_untitled_1)
                            .setContentIntent(contentIntent)
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setContentText(remoteMessage.getData().get("body"));

                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(collapseKey, NotificationIDS.ORDER_UPDATES, mBuilder.build());
                }
            }


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

}
