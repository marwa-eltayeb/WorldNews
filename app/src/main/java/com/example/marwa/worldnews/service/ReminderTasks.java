package com.example.marwa.worldnews.service;

import android.content.Context;
import android.content.Intent;

import com.example.marwa.worldnews.activities.MainActivity;
import com.example.marwa.worldnews.notifications.NotificationUtils;

/**
 * Created by Marwa on 7/25/2018.
 */

public class ReminderTasks {


    public static final String ACTION_READ_NEWS_STORY = "read_news_story";
    // Add a public static constant called ACTION_DISMISS_NOTIFICATION
    public static  final  String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_NOTIFICATION_REMINDER = "notification-reminder";

    public static void executeTask(Context context, String action) {
        if (ACTION_READ_NEWS_STORY.equals(action)) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            NotificationUtils.clearAllNotifications(context);
        }else if(ACTION_DISMISS_NOTIFICATION.equals(action)){
            // If the user ignored the reminder, clear the notification
            NotificationUtils.clearAllNotifications(context);
        }else if (ACTION_NOTIFICATION_REMINDER.equals(action)) {
            NotificationUtils.remindUserAboutReadingNews(context);
        }

    }

}
