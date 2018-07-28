package com.example.marwa.worldnews.service;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Marwa on 7/28/2018.
 */

public class NewsReminderFirebaseJobService extends JobService {


    private AsyncTask mBackgroundTask;

    /**
     * The entry point to your Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     *
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     *
     * @return whether there is more work remaining.
     */


    // * It executes that task that sends out the notification which we just made
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        // By default, jobs are executed on the main thread, so make an anonymous class extending
        //  AsyncTask called mBackgroundTask.
        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                // Use ReminderTasks to execute the new charging reminder task you made, use
                // this service as the context (WaterReminderFirebaseJobService.this) and return null
                // when finished.
                Context context = NewsReminderFirebaseJobService.this;
                // * Start the JonService
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_NOTIFICATION_REMINDER);
                return null;
            }


            @Override
            protected void onPostExecute(Object o) {
                // * The job is successful, so no need to reschedule
                jobFinished(jobParameters, false);
            }
        };



        // Execute the AsyncTask
        mBackgroundTask.execute();
        // It signals that our job is still doing some work
        return true;
    }


    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        // f mBackgroundTask is valid, cancel it
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        // As soon as the condition are remet, the job should be retried again
        return true;
    }




}


