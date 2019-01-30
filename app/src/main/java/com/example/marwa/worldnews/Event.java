package com.example.marwa.worldnews;

/**
 * Created by Marwa on 1/30/2019.
 */

public class Event {

    // Add boolean variable for checking
    protected static boolean isNewsFragmentChanged = false;
    protected static boolean isSportFragmentChanged = false;
    protected static boolean isCultureFragmentChanged = false;
    protected static boolean isLifeAndStyleFragmentChanged = false;
    protected static boolean isTechnologyFragmentChanged = false;

    /**
     * If data is changed, turn variables to true
     */
    public static void onDataChang(){
        isNewsFragmentChanged = true;
        isSportFragmentChanged = true;
        isCultureFragmentChanged = true;
        isLifeAndStyleFragmentChanged = true;
        isTechnologyFragmentChanged = true;
    }
}
