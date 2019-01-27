package com.example.marwa.worldnews.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marwa.worldnews.News;
import com.example.marwa.worldnews.NewsLoader;
import com.example.marwa.worldnews.R;
import com.example.marwa.worldnews.adapters.NewsAdapter;
import com.example.marwa.worldnews.adapters.NewsCursorAdapter;
import com.example.marwa.worldnews.data.NewsCursorLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CultureFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {

    /**
     * Adapter for the list of news stories.
     */
    NewsAdapter adapter;
    NetworkInfo networkInfo;
    /**
     * TextView that is displayed when the list is empty.
     */
    private TextView emptyStateTextView;
    /**
     * a ProgressBar variable to show and hide the progress bar.
     */
    private ProgressBar loadingIndicator;

    private SwipeRefreshLayout swipeRefreshLayout;

    LoaderManager.LoaderCallbacks<List<News>> loader = this;

    ListView newsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.news_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));


        // Find a reference to the {@link ListView} in the layout.
        newsListView = (ListView) rootView.findViewById(R.id.newsList);

        // Find a reference to an empty TextView
        emptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        // Set the TextView on the ListView.
        newsListView.setEmptyView(emptyStateTextView);

        //Find the ProgressBar using findViewById.
        loadingIndicator = (ProgressBar) rootView.findViewById(R.id.loading_indicator);

        // Create a new adapter that takes an empty list of news stories as input.
        adapter = new NewsAdapter(getActivity(), new ArrayList<News>());

        // Set the adapter on the {@link ListView}.
        newsListView.setAdapter(adapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity.
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter.
            loaderManager.initLoader(Link.NEWS_LOADER_ID, null, this);
        } else {
            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);

            // Define and instantiate NewsCursorAdapter
            NewsCursorAdapter newsCursorAdapter = new NewsCursorAdapter(getContext(), null);
            // Set the adapter on the listView
            newsListView.setAdapter(newsCursorAdapter);
            // Show the stored data
            getActivity().getSupportLoaderManager().initLoader(
                    Link.CULTURE_LOADER_ID, null, new NewsCursorLoader(getContext(), Link.CULTUREJ, newsCursorAdapter));
        }

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //swipeRefreshLayout.setRefreshing(true);
                                        //getLoaderManager().restartLoader(Link.NEWS_LOADER_ID, null, loader);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
        );

        newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (newsListView == null || newsListView.getChildCount() == 0)
                                ? 0
                                : newsListView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        return rootView;
    }

    // onCreateLoader instantiates and returns a new Loader for the given ID
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        // getString retrieves a String value from the preferences.
        // The second parameter is the default value for this preference.
        String country = sharedPrefs.getString(
                getString(R.string.country_key),
                getString(R.string.country_default));

        String date = sharedPrefs.getString(
                getString(R.string.date_key),
                getString(R.string.date_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(Link.NEWS_REQUEST_URL);
        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter(Link.PARAM_QUERY, country);
        uriBuilder.appendQueryParameter(Link.PARAM_SECTION, Link.CULTURE);
        uriBuilder.appendQueryParameter(Link.PARAM_SHOW_TAGS, Link.AUTHOR);
        uriBuilder.appendQueryParameter(Link.PARAM_SHOW_FIELDS, Link.PIC_DIS);
        uriBuilder.appendQueryParameter(Link.PARAM_PAGE_SIZE, Link.SIZE);
        uriBuilder.appendQueryParameter(Link.PARAM_ORDER_BY, date);
        uriBuilder.appendQueryParameter(Link.PARAM_API_KEY, Link.KEY);

        // Return the completed uri
        return new NewsLoader(getContext(), uriBuilder.toString() , Link.CULTUREJ);

    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        //Hide the indicator after the data is appeared
        loadingIndicator.setVisibility(View.GONE);
        // Hide refresh animation
        swipeRefreshLayout.setRefreshing(false);

        // Check if connection is still available, otherwise show appropriate message
        if (networkInfo != null && networkInfo.isConnected()) {
            // If there is a valid list of news stories, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                adapter.clear();
                adapter.addAll(data);
            } else {
                emptyStateTextView.setVisibility(View.VISIBLE);
                emptyStateTextView.setText(getString(R.string.no_news));
            }

        } else {
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Restart the loader when we get back to the MainActivity
        getLoaderManager().restartLoader(Link.NEWS_LOADER_ID, null, this);
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(Link.NEWS_LOADER_ID, null, this);
    }

}
