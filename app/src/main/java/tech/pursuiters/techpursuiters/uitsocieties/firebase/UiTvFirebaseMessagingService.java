package tech.pursuiters.techpursuiters.uitsocieties.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import tech.pursuiters.techpursuiters.uitsocieties.MainActivity;
import tech.pursuiters.techpursuiters.uitsocieties.R;

import static android.R.attr.data;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static tech.pursuiters.techpursuiters.uitsocieties.ClubContract.uiTv_LINK;

/**
 * Created by Shubhi on 12/29/2017.
 */

public class UiTvFirebaseMessagingService extends FirebaseMessagingService{

    @Override

    public void onMessageReceived(RemoteMessage remoteMessage){

        Log.v("RemoteMess---",String.valueOf(remoteMessage.getData().size()));

        Map<String, String> received;
        Intent intent = new Intent(this, MainActivity.class);

        if(!remoteMessage.getData().isEmpty()) {
            received = remoteMessage.getData();
            if (received.containsKey("Update")){
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uiTv_LINK));
            }
        }
        else {
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        try {
            if (remoteMessage.getNotification().getTitle() != null)
                notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
            else
                notificationBuilder.setContentTitle("uiTv");
            notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        }
        catch (Exception e){
            notificationBuilder.setContentTitle("uiTv");
            e.printStackTrace();
        }

        notificationBuilder.setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.mipmap.ic_launcher))
                            .setContentIntent(pendingIntent);

        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(""));

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());

        Log.d(TAG,"From:"+remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void scheduleJob() {
    }

    private void handleNow() {
    }
}
