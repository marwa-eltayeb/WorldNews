package com.example.marwa.worldnews.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.marwa.worldnews.fragments.CultureFragment;
import com.example.marwa.worldnews.fragments.LifestyleFragment;
import com.example.marwa.worldnews.fragments.NewsFragment;
import com.example.marwa.worldnews.R;
import com.example.marwa.worldnews.fragments.SportFragment;
import com.example.marwa.worldnews.fragments.TechnologyFragment;

/**
 * Created by Marwa on 1/29/2018.
 */

public class CategoryAdapter extends FragmentPagerAdapter {


    /**
     * Context of the app
     */
    private Context context;

    /**
     * Create a new {@link CategoryAdapter} object.
     *
     * @param context is the context of the app
     * @param fm      is the fragment manager that will keep each fragment's state in the adapter
     *                across swipes.
     */
    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NewsFragment();
        } else if (position == 1) {
            return new SportFragment();
        } else if (position == 2) {
            return new CultureFragment();
        } else if (position == 3) {
            return new LifestyleFragment();
        } else {
            return new TechnologyFragment();
        }
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 5;
    }

    /**
     * Get the title of each page.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.category_news);
        } else if (position == 1) {
            return context.getString(R.string.category_sport);
        } else if (position == 2) {
            return context.getString(R.string.category_culture);
        } else if (position == 3) {
            return context.getString(R.string.category_lifestyle);
        } else {
            return context.getString(R.string.category_technology);
        }
    }

}
