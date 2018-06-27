package com.example.marwa.worldnews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marwa on 5/1/2018.
 */

import com.example.marwa.worldnews.data.NewsContract.NewsEntry;

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the news table
        String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + " ("
                + NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NewsEntry.COLUMN_NEWS_TITLE + " TEXT NOT NULL, "
                + NewsEntry.COLUMN_NEWS_SECTION + " TEXT, "
                + NewsEntry.COLUMN_NEWS_DATE + " TEXT, "
                + NewsEntry.COLUMN_NEWS_AUTHOR + " TEXT, "
                + NewsEntry.COLUMN_NEWS_DESC + " TEXT, "
                + " UNIQUE (" + NewsEntry.COLUMN_NEWS_TITLE + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_NEWS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the Database
        db.execSQL("DROP TABLE IF EXISTS " + NewsEntry.TABLE_NAME);
        // Create a new one
        onCreate(db);
    }


}
