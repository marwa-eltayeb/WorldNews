package com.example.marwa.worldnews;

/**
 * Created by Marwa on 1/30/2019.
 */

public class Event {

    public static boolean isNewsFragmentChanged = false;
    public static boolean isSportFragmentChanged = false;
    public static boolean isCultureFragmentChanged = false;
    public static boolean isLifeAndStyleFragmentChanged = false;
    public static boolean isTechnologyFragmentChanged = false;



    public static void onDataChang(){
        isNewsFragmentChanged = true;
        isSportFragmentChanged = true;
        isCultureFragmentChanged = true;
        isLifeAndStyleFragmentChanged = true;
        isTechnologyFragmentChanged = true;
    }
}
