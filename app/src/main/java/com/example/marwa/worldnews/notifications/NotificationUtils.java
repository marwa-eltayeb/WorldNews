package com.example.marwa.worldnews.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.marwa.worldnews.R;
import com.example.marwa.worldnews.activities.MainActivity;
import com.example.marwa.worldnews.service.NewsReminderService;
import com.example.marwa.worldnews.service.ReminderTasks;

/**
 * Created by Marwa on 7/25/2018.
 */

public class NotificationUtils {

    /*
    * This notification ID can be used to access our notification after we've displayed it. This
    * can be handy when we need to cancel the notification, or perhaps update it. This number is
    * arbitrary and can be set to whatever you like. 1138 is in no way significant.
    */
    private static final int NEWS_REMINDER_NOTIFICATION_ID = 1138;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int NEWS_REMINDER_PENDING_INTENT_ID = 3417;

    private static final int ACTION_READ_PENDING_INTENT_ID = 1;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;


    //  Create a method to clear all notifications
    //  Hide Notification after action
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }



    public static void remindUserAboutReadingNews(Context context) {
        Uri sound = Uri.parse("android.resource://com.example.marwa.worldnews/" + R.raw.beeb);

        // Get the NotificationManager using context.getSystemService
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(sound)
                .setContentIntent(contentIntent(context))
                .addAction(readNewsAction(context))
                .addAction(ignoreReminderAction(context))
                // The notification is automatically canceled when the user clicks it in the panel
                .setAutoCancel(true);

        // If the build version is greater than JELLY_BEAN and lower than OREO,
        // set the notification's priority to PRIORITY_HIGH.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        // Trigger the notification by calling notify on the NotificationManager.
        // Pass in a unique ID of your choosing for the notification and notificationBuilder.build()
        notificationManager.notify(NEWS_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }


    //  Add a static method called ignoreReminderAction
    private static NotificationCompat.Action ignoreReminderAction(Context context) {
        // Create an Intent to launch WaterReminderIntentService
        Intent ignoreReminderIntent = new Intent(context, NewsReminderService.class);
        // Set the action of the intent to designate you want to dismiss the notification
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);
        // Create a PendingIntent from the intent to launch WaterReminderIntentService
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // Create an Action for the user to ignore the notification (and dismiss it)
        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.drawable.ic_cancel_black_24px,
                "cancel",
                ignoreReminderPendingIntent);
        // Return the action
        return ignoreReminderAction;
    }



    //  Add a static method called readNewsAction
    private static NotificationCompat.Action readNewsAction(Context context) {
        // Create an Intent to launch NewsReminderService
        Intent readNewsIntent = new Intent(context, NewsReminderService.class);
        // Set the action of the intent to designate you want to read the news story
        readNewsIntent.setAction(ReminderTasks.ACTION_READ_NEWS_STORY);
        // Create a PendingIntent from the intent to launch NewsReminderService
        PendingIntent readNewsPendingIntent = PendingIntent.getService(
                context,
                ACTION_READ_PENDING_INTENT_ID,
                readNewsIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        // Create an Action for the user to tell them to read the news story
        NotificationCompat.Action readNewsAction = new NotificationCompat.Action(R.drawable.ic_read,
                "read",
                readNewsPendingIntent);
        // Return the action
        return readNewsAction;
    }



    // Create a helper method called contentIntent with a single parameter for a Context. It
    // should return a PendingIntent. This method will create the pending intent which will trigger when
    // the notification is pressed. This pending intent should open up the MainActivity.
    private static PendingIntent contentIntent(Context context) {
        // Create an intent that opens up the MainActivity
        // Create a PendingIntent using getActivity that:
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return  PendingIntent.getActivity(
                context,
                NEWS_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }




    // Create a helper method called largeIcon which takes in a Context as a parameter and
    // returns a Bitmap. This method is necessary to decode a bitmap needed for the notification.
    private static Bitmap largeIcon(Context context) {
        // Get a Resources object from the context.
        Resources res = context.getResources();
        // Create and return a bitmap using BitmapFactory.decodeResource, passing in the
        // resources object and R.drawable.ic_local_drink_black_24px
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        return largeIcon;

    }


}
