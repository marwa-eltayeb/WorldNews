package com.example.marwa.worldnews.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.marwa.worldnews.R;
import com.example.marwa.worldnews.adapters.CategoryAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments.
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page.
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager.
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager.
        tabLayout.setupWithViewPager(viewPager);

        // If we want to change the mode after rotation.
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) viewPager.getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        // Gets the width of screen.
        int width = metrics.widthPixels;
        // Gets the Height of screen.
        int height = metrics.heightPixels;

        // Landscape Orientation
        if (width > height) {
            // Change the mode to fixed
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }

    /**
     * Initialize the contents of the Activity's options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/main.xml file.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Setup the specific action that occurs when any of the items in the Options Menu are selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting) {
            // Display the SettingsActivity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
