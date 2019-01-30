package com.example.marwa.worldnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marwa.worldnews.News;
import com.example.marwa.worldnews.R;
import com.example.marwa.worldnews.utility.Utility;
import com.example.marwa.worldnews.activities.WebViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Marwa on 1/22/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Construct a new {@link NewsAdapter}
     *
     * @param context of the app
     * @param news    is the list of news stories which is the data source of the adapter
     */
    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        // Check if there is an existing list item view{called convertView} that we can
        // reuse, otherwise, if convertView is null, then inflate a new list item layout
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        // find the news story at the given position in the list of news stories.
        News currentNewsArticle = getItem(position);

        // Find the TextView with view ID title.
        TextView title = (TextView) listItemView.findViewById(R.id.title_text_view);
        // Display the title of the current News in that TextView.
        title.setText(currentNewsArticle.getTitle());

        // Find the TextView with view ID section.
        TextView section = (TextView) listItemView.findViewById(R.id.section_text_view);
        //Display the author of the current News in that TextView/
        section.setText(currentNewsArticle.getSection());

        // Find the TextView with view ID publication Date.
        TextView publicationDate = (TextView) listItemView.findViewById(R.id.publicationDate_text_view);
        // Format the date string.
        String formattedDate = Utility.formatDate(currentNewsArticle.getPublicationDate());
        // Display the date of the current News story in that TextView.
        publicationDate.setText(formattedDate);

        //Find the TextView with view ID author name.
        TextView authorName = (TextView) listItemView.findViewById(R.id.authorName_text_view);
        //Display the author of the current News in that TextView.
        String author = currentNewsArticle.getAuthorName();
        if (author.equals("")) {
            author = getContext().getResources().getString(R.string.unnamed);
        }
        authorName.setText(author);

        //Find the TextView with view ID description.
        TextView description = (TextView) listItemView.findViewById(R.id.description_text_view);
        //Display the description of the current news story in that TextView.
        description.setText(currentNewsArticle.getShortDescription());

        // Find the ImageView in the list_item.xml layout with the ID image.
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.imageOfNewsArticle_image_view);
        // Put the image resource in a String Variable.
        final String imageUrl = currentNewsArticle.getImageResource();

        // Check if an image is provided for this news story or not
        if (currentNewsArticle.hasImage()) {
            //Display the image of the current news story in that ImageView using Picasso
            Picasso.with(getContext()).load(imageUrl).into(imageView);
        } else {
            // Otherwise Set the ImageView to the default image.
            imageView.setImageResource(R.drawable.news);
        }

        // Share the news story
        ImageButton share = (ImageButton) listItemView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.shareNewsStory(getContext(),imageUrl);
            }
        });

        // Set an item click listener on the CardView, which sends an intent to a Web Activity
        // to open a website with more information about the selected news story.
        android.support.v7.widget.CardView parentView = (android.support.v7.widget.CardView) listItemView.findViewById(R.id.card_view);
        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                String url = getItem(position).getWebUrl();
                intent.putExtra("news", url);
                getContext().startActivity(intent);
            }
        });


        return listItemView;
    }





}
