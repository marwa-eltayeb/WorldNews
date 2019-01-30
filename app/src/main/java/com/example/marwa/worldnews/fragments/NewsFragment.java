package com.example.marwa.worldnews.fragments;


import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marwa.worldnews.Event;
import com.example.marwa.worldnews.News;
import com.example.marwa.worldnews.NewsLoader;
import com.example.marwa.worldnews.R;
import com.example.marwa.worldnews.adapters.NewsAdapter;
import com.example.marwa.worldnews.adapters.NewsCursorAdapter;
import com.example.marwa.worldnews.data.NewsCursorLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener,SharedPreferences.OnSharedPreferenceChangeListener {


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

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

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

        // Get a reference to the ConnectivityManager to check state of network connectivity
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
                    Link.NEWS_LOADER_ID, null, new NewsCursorLoader(getContext(),Link.NEWSJ, newsCursorAdapter));
            loadingIndicator.setVisibility(View.GONE);
        }

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

        if(Event.isNewsFragmentChanged){
            getLoaderManager().restartLoader(Link.NEWS_LOADER_ID, null, this);
            Event.isNewsFragmentChanged = false;
            Toast.makeText(getContext(), "News is activated", Toast.LENGTH_SHORT).show();
        }

        // Register the listener
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);

        return rootView;
    }


    // onCreateLoader instantiates and returns a new Loader for the given ID
    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        // getString retrieves a String value from the preferences.
        // The second parameter is the default value for this preference.
        // Update URI to Use the User’s Preferred option.
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
        uriBuilder.appendQueryParameter(Link.PARAM_SECTION, Link.NEWS);
        uriBuilder.appendQueryParameter(Link.PARAM_SHOW_TAGS, Link.AUTHOR);
        uriBuilder.appendQueryParameter(Link.PARAM_SHOW_FIELDS, Link.PIC_DIS);
        uriBuilder.appendQueryParameter(Link.PARAM_PAGE_SIZE, Link.SIZE);
        uriBuilder.appendQueryParameter(Link.PARAM_ORDER_BY, date);
        uriBuilder.appendQueryParameter(Link.PARAM_API_KEY, Link.KEY);

        // Return the completed uri
        return new NewsLoader(getContext(), uriBuilder.toString(), Link.NEWSJ);

    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        ///Hide the indicator after the data is appeared
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
                // If there is no data, show a message about that
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

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(Link.NEWS_LOADER_ID, null, this);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        inflater.inflate(R.menu.main_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    adapter.getFilter().filter(newText);
                    return true;
                }


                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    adapter.getFilter().filter(query);
                    if(adapter.getCount()==0){
                        getNoResult();
                    }
                    return true;
                }

            };
            searchView.setOnQueryTextListener(queryTextListener);
            searchView.setQueryHint("Search for News");

        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void getNoResult(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setMessage("No Match. Please try again");

        // Create and show the AlertDialog
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                alertDialog.dismiss();
                timer.cancel(); // This will cancel the timer of the system
            }
        }, 2000); // the timer will count 2 seconds....
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.country_key))||key.equals(getString(R.string.date_key))) {
            getLoaderManager().restartLoader(Link.NEWS_LOADER_ID, null, this);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
