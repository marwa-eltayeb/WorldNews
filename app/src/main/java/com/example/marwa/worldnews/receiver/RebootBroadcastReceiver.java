package com.example.marwa.worldnews.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;

import com.example.marwa.worldnews.R;
import com.example.marwa.worldnews.service.ReminderUtilities;

/**
 * Created by Marwa on 7/31/2018.
 */

// An inner class
public class RebootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Get the action of the intent passed
        String action = intent.getAction();

        //Whether it is rebooting or not
        boolean isRebooting = (action.equals(Intent.ACTION_BOOT_COMPLETED));

        if (isRebooting) {
            // Show notification again
            Toast.makeText(context, "Rebooted", Toast.LENGTH_SHORT).show();
            setupNotification(context);
        }
    }


    private void setupNotification(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean notification = sharedPrefs.getBoolean(context.getString(R.string.notification_key),
                context.getResources().getBoolean(R.bool.notification_default));
        if (notification) {
            // Schedule the notification reminder
            ReminderUtilities.scheduleReadingNewsReminder(context);
        } else {
            ReminderUtilities.unSchedule();
        }
    }

}


