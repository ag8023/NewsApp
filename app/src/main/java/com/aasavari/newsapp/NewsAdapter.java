package com.aasavari.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aasavari on 3/2/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, List<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the news adapter at the position parameter
        News currNews = getItem(position);
        //check if the view passed in is new or a recycled
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        //get the view objects from the list item layout, to publish the news object details
        TextView newsTitle = (TextView) convertView.findViewById(R.id.news_title);
        TextView newsSection = (TextView) convertView.findViewById(R.id.news_section);
        TextView newsAuthor = (TextView)convertView.findViewById(R.id.news_author);
        newsTitle.setText(currNews.getNewsTitle());
        newsSection.setText(currNews.getNewsSection());
        newsAuthor.setText(currNews.getNewsAuthor());
        return convertView;
    }
}
