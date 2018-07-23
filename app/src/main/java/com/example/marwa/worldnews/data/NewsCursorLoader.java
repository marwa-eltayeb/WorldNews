package com.example.marwa.worldnews.data;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.marwa.worldnews.adapters.NewsCursorAdapter;
import com.example.marwa.worldnews.data.NewsContract.NewsEntry;


/**
 * Created by Marwa on 5/7/2018.
 */

public class NewsCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private NewsCursorAdapter newsCursorAdapter;
    private String section;

    public NewsCursorLoader(Context context,String section,NewsCursorAdapter newsCursorAdapter) {
        this.context = context;
        this.newsCursorAdapter = newsCursorAdapter;
        this.section = section;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        String[] projection = {
                NewsEntry._ID,
                NewsEntry.COLUMN_NEWS_TITLE,
                NewsEntry.COLUMN_NEWS_SECTION,
                NewsEntry.COLUMN_NEWS_DATE,
                NewsEntry.COLUMN_NEWS_AUTHOR,
                NewsEntry.COLUMN_NEWS_DESC,
        };


        String selection = NewsEntry.COLUMN_NEWS_SECTION + "=?";
        String[] selectionArgs = new String[]{section};
        return new CursorLoader(context,
                NewsEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link NewsCursorAdapter} with this new cursor containing updated news data
        newsCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        newsCursorAdapter.swapCursor(null);
    }


}
