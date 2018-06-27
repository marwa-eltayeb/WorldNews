package com.example.marwa.worldnews;

import android.text.Html;

/**
 * Created by Marwa on 1/21/2018.
 */

public class News {
    /**
     * Title of the news story.
     */
    private String title;
    /**
     * Section of the news story.
     */
    private String section;
    /**
     * Publication date of the news story.
     */
    private String publicationDate;
    /**
     * Name of the author of the news story.
     */
    private String authorName;
    /**
     * Image resource for the news story.
     */
    private String imageResource;
    /**
     * Short description of the news story.
     */
    private String shortDescription;
    /**
     * Website URL of the news story.
     */
    private String webUrl;

    /**
     * create a new {@link News} object;
     *
     * @param title            is title of the News.
     * @param section          is section of the News.
     * @param publicationDate  is publication Date of the News
     * @param authorName       is name of the author.
     * @param imageResource    is image resource for the News
     * @param shortDescription is Short Description of the News story
     * @param webUrl           is website URL of the News stories
     */
    News(String title, String section, String publicationDate, String authorName, String imageResource, String shortDescription, String webUrl) {
        this.title = title;
        this.section = section;
        this.publicationDate = publicationDate;
        this.authorName = authorName;
        this.imageResource = imageResource;
        this.shortDescription = shortDescription;
        this.webUrl = webUrl;
    }

    /**
     * returns the title of the News
     */
    public String getTitle() {
        return title;
    }

    /**
     * returns the section of the News
     */
    public String getSection() {
        return section;
    }

    /**
     * returns the date of the publication.
     */
    public String getPublicationDate() {
        return publicationDate;
    }

    /**
     * returns the name of the author
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * returns the image resource for the News
     */
    public String getImageResource() {
        return imageResource;
    }

    /**
     * Returns whether or not there is an image for this news story.
     */
    public boolean hasImage() {
        return !getImageResource().equals("");
    }

    /**
     * Returns short Description of the News's story.
     */
    public String getShortDescription() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            //Returns styled text from the provided HTML string
            return Html.fromHtml(shortDescription, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(shortDescription).toString();
        }
    }

    /**
     * Returns the website URL to find more information about the News stories.
     */
    public String getWebUrl() {
        return webUrl;
    }

}
