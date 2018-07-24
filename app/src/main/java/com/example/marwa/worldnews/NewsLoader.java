package com.example.marwa.worldnews;

import android.content.ContentValues;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.marwa.worldnews.data.NewsContract.NewsEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marwa on 1/21/2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * Query URL
     */
    private String url;

    private String section;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public NewsLoader(Context context, String url, String section) {
        super(context);
        this.url = url;
        this.section = section;
    }

    /**
     * Force the data to load.
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {
        // If there is no URL, return null.
        if (url == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news stories.
        List<News> news =  QueryUtils.fetchNewsData(url);


        //Log.i("InsertBulkNews" , " list size is : "+news.size() );

        if(news!= null){
            deleteNews();
            insertBulkNews(news);
        }else {
            Log.i("InsertBulkNews","News is null");
        }

        return news;
    }

    private void insertBulkNews(List<News> newsList) {
        Log.v("INSERT","Insert Method Run");
        List<ContentValues> newsValues = new ArrayList<>();
        for (int i = 0; i < newsList.size(); i++)
        {
            newsValues.add(createNewsContentValues(newsList.get(i)));
        }

        getContext().getContentResolver().bulkInsert(
                NewsEntry.CONTENT_URI,
                newsValues.toArray(new ContentValues[newsList.size()]));

    }


    // Delete the old news stories
    private void deleteNews() {
        Log.v("DELETE","Delete Method Run");
        String selection = NewsEntry.COLUMN_NEWS_SECTION + "=?";
        String[] selectionArgs = new String[]{section};
        getContext().getContentResolver().delete(NewsEntry.CONTENT_URI,selection,selectionArgs);
    }


    // Insert new news stories inside the database
    private ContentValues createNewsContentValues(News news) {
        String news_title = news.getTitle();
        String news_section = news.getSection();
        String news_date = news.getPublicationDate();
        String news_author = news.getAuthorName();
        String news_desc = news.getShortDescription();

        ContentValues newsValues = new ContentValues();
        newsValues.put(NewsEntry.COLUMN_NEWS_TITLE, news_title);
        newsValues.put(NewsEntry.COLUMN_NEWS_SECTION, news_section);
        newsValues.put(NewsEntry.COLUMN_NEWS_DATE, news_date);
        newsValues.put(NewsEntry.COLUMN_NEWS_AUTHOR, news_author);
        newsValues.put(NewsEntry.COLUMN_NEWS_DESC, news_desc);
        return newsValues;
    }




}
