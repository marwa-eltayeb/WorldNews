package com.example.marwa.worldnews.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Marwa on 7/25/2018.
 */


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NewsReminderService extends IntentService {

    public NewsReminderService() {
        super("NewsReminderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}