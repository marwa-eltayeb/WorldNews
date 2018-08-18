package com.example.marwa.worldnews.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Marwa on 7/28/2018.
 */

public class ReminderUtilities {


    private static final int REMINDER_INTERVAL_HOURS = 24;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.HOURS.toSeconds(REMINDER_INTERVAL_HOURS));
    //private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    private static final String REMINDER_JOB_TAG = "read_news_tag";

    private static boolean sInitialized;

    private static FirebaseJobDispatcher dispatcher;

    synchronized public static void scheduleReadingNewsReminder(@NonNull final Context context) {

        // If the job has already been initialized, return
        if (sInitialized) return;

        // Create a new GooglePlayDriver
        Driver driver = new GooglePlayDriver(context);
        // Create a new FirebaseJobDispatcher with the driver
        dispatcher = new FirebaseJobDispatcher(driver);


        /* Create the Job to periodically create reminders to drink water */
        Job constraintReminderJob = dispatcher.newJobBuilder()
                /* The Service that will be used to write to preferences */
                .setService(NewsReminderFirebaseJobService.class)
                /*
                 * Set the UNIQUE tag used to identify this Job.
                 */
                .setTag(REMINDER_JOB_TAG)

                .setConstraints(Constraint.ON_ANY_NETWORK)
                // Even between reboots
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /** make a window of execution */
                .setTrigger(Trigger.executionWindow(59, 60))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        // Use dispatcher's schedule method to schedule the job
        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(constraintReminderJob);

        // Set sInitialized to true to mark that we're done setting up the job
        /* The job has been initialized */
        sInitialized = true;
    }

    public static void unSchedule(){
        if(dispatcher != null){
            dispatcher.cancelAll();
            sInitialized = false;
        }
    }



}
