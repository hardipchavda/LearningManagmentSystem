package app.preplotus.utilities;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import app.preplotus.R;
import app.preplotus.activities.MainActivity;
import app.preplotus.activities.NotesActivity;
import app.preplotus.activities.SubTopicsActivity;
import app.preplotus.activities.SubscriptionActivity;
import app.preplotus.activities.TestsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("subtitle"), remoteMessage.getData().get("body"), Objects.requireNonNull(remoteMessage.getData().get("redirect_type")), remoteMessage.getData().get("notes_id"), remoteMessage.getData().get("notes_title"), remoteMessage.getData().get("test_type"), remoteMessage.getData().get("test_id"), remoteMessage.getData().get("title_subject"));
        }
    }

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
    }

    private void sendNotification(String title,String subtitle, String messageBody, String redirect_type, String notes_id, String note_title, String test_type, String test_id, String title_subject) {
        PendingIntent pendingIntent = null;

        if (redirect_type.equals("Home")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else if (redirect_type.equals("Subscription")) {
            Intent intent = new Intent(this, SubscriptionActivity.class);
            intent.putExtra("from","notification");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else if (redirect_type.equals("AllTest")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("redirect_type", "alltest");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else if (redirect_type.equals("AllNotes")) {
            Intent intent = new Intent(this, NotesActivity.class);
            intent.putExtra("from","notification");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else if (redirect_type.equals("Notes")) {
            Intent intent = new Intent(this, SubTopicsActivity.class);
            intent.putExtra("topicId", notes_id);
            intent.putExtra("topicTitle", note_title);
            intent.putExtra("from","notification");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else if (redirect_type.equals("Test")) {
            Intent intent = new Intent(this, TestsActivity.class);
            intent.putExtra("id", test_id);
            intent.putExtra("title", title_subject);
            intent.putExtra("from","notification");
            if (test_type.equals("Subject Package")) {
                intent.putExtra("type", "subject");
            } else {
                intent.putExtra("type", "package");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        String channelId = "prep_lms";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = null;

        if (subtitle!=null && subtitle.trim().length()>0){
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.arrow, subtitle, pendingIntent).build();

            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_stat_lotus_logo_png)
                            .setColor(getResources().getColor(R.color.purple_700))
                            .setContentTitle(title)
                            .setContentText(Html.fromHtml(messageBody, FROM_HTML_MODE_LEGACY))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(messageBody, FROM_HTML_MODE_LEGACY)))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
            .addAction(action);
        } else {
            notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_stat_lotus_logo_png)
                            .setColor(getResources().getColor(R.color.purple_700))
                            .setContentTitle(title)
                            .setContentText(Html.fromHtml(messageBody, FROM_HTML_MODE_LEGACY))
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml(messageBody, FROM_HTML_MODE_LEGACY)))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "LMSChanel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        int id = new Random(System.currentTimeMillis()).nextInt(1000);
        notificationManager.notify(id/* ID of notification */, notificationBuilder.build());
    }

}
