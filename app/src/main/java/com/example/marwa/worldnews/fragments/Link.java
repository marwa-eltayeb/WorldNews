package com.example.marwa.worldnews.fragments;

class Link {

    /**
     * URL for News data from the Guardian.
     */
    final static String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?";


    /**
     * Constant value for the NEWS loader ID.
     */
    final static int NEWS_LOADER_ID = 1;


    // Parameters and values
    final static String PARAM_QUERY = "q";
    final static String PARAM_SECTION = "section";
    final static String LIFE_AND_STYLE = "lifeandstyle";
    final static String PARAM_SHOW_TAGS = "show-tags";
    final static String PARAM_ORDER_BY = "order-by";
    final static String PIC_DIS = "thumbnail,trailText";
    final static String PARAM_PAGE_SIZE = "page-size";
    final static String SIZE = "20";
    final static String PARAM_SHOW_FIELDS = "show-fields";
    final static String AUTHOR = "contributor";
    final static String PARAM_API_KEY = "api-key";
    final static String KEY = "37b5b5aa-d7fb-4219-b589-3e1af665cfff";

    // Sections
    final static String NEWS = "world";
    final static String SPORT = "sport";
    final static String CULTURE = "culture";
    final static String TECHNOLOGY = "technology";
}
