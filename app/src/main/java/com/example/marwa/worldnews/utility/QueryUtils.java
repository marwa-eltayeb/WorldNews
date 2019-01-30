package com.example.marwa.worldnews.utility;

import android.text.TextUtils;
import android.util.Log;

import com.example.marwa.worldnews.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marwa on 1/21/2018.
 */

public class QueryUtils {

    /**
     * Create a private constructor (No object is permitted).
     */
    private QueryUtils() {
    }

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the news dataSet and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Return the list of {@link News}
        return extractFeaturesFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        // Store our URL
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        // It seems like open up a browser and we are gonna use it to fetch the contents of a URl
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            //Set up url connection
            //To make sure that it is the right type, we cast it to HttpURLConnection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news stories JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeaturesFromJson(String NewsJSON) {

        if(TextUtils.isEmpty(NewsJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding news stories to
        List<News> news = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a base JSONObject
            JSONObject baseJsonResponse = new JSONObject(NewsJSON);

            // Extract the JSONObject associated with the key called "response"
            JSONObject response = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results".
            // which represents a list of news stories.
            JSONArray newsArray = response.getJSONArray("results");

            // For each news in the NewsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length(); i++) {
                // String for news's title, section, publicationDate, authorName , thumbnail, trailText and webUrl.
                String title = "";
                String section = "";
                String publicationDate = "";
                String authorName = "";
                String thumbnail = "";
                String trailText = "";
                String webUrl = "";

                // Get a single News story at position i within the list of News Articles
                JSONObject currentNews = newsArray.getJSONObject(i);

                try {
                    // Extract the value for the key called "webTitle"
                    title = currentNews.getString("webTitle");
                } catch (Exception e) {
                    Log.v(LOG_TAG, "No title");
                }

                try {
                    // Extract the value for the key called "sectionName"
                    section = currentNews.getString("sectionName");
                } catch (Exception e) {
                    Log.v(LOG_TAG, "No section");
                }

                try {
                    // Extract the value for the key called "webPublicationDate"
                    publicationDate = currentNews.getString("webPublicationDate");
                } catch (Exception e) {
                    Log.v(LOG_TAG, "No Publication Date");
                }

                try {
                    // Extract the JSONArray associated with the key called "tags",
                    JSONArray tags = currentNews.getJSONArray("tags");
                    // Loop through each item of tags
                    for (int j = 0; j < tags.length(); j++) {
                        JSONObject authorNameObject = tags.getJSONObject(j);
                        // Extract the value for the key called "webTitle"
                        authorName = authorNameObject.getString("webTitle");
                    }
                } catch (Exception e) {
                    Log.v(LOG_TAG, "No author Name");
                }

                JSONObject subJsonObject = null;
                try {
                    // Extract the JSONObject associated with the key called "fields"
                    subJsonObject = currentNews.getJSONObject("fields");
                    // Extract the value for the key called "thumbnail"
                    thumbnail = subJsonObject.getString("thumbnail");
                } catch (Exception e) {
                    Log.v(LOG_TAG, "No image for this piece of news");
                }

                try {
                    // Extract the value for the key called "trailText"
                    trailText = subJsonObject.getString("trailText");
                } catch (Exception e) {
                    Log.v(LOG_TAG, "No description for this piece of news");
                }

                try {
                    // Extract the value for the key called "webUrl"
                    webUrl = currentNews.getString("webUrl");
                } catch (Exception e) {
                    Log.v(LOG_TAG, "No Web Url");
                }

                // Create a new {@link News} object.
                News newsArticles = new News(title, section, publicationDate, authorName, thumbnail, trailText, webUrl);
                // Add the new news story to the list of news stories.
                news.add(newsArticles);
            }
        } catch (JSONException e) {
            // A log message is printed with the exception.
            Log.e(LOG_TAG, "Problem parsing the news stories JSON results", e);
        }
        // Returns the list of news stories.
        return news;
    }






}
