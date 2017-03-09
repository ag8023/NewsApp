package com.aasavari.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    //url to query for the news articles
    private static final String GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search?q=debates&api-key=test&show-tags=contributor";
    // Constant value for the News Loader ID. We can choose any integer.
    //This really only comes into play if you're using multiple loaders.
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mNewsAdapter;
    private TextView mEmptyStateView;
    private ProgressBar mProgressBar;

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "TEST:onCreateLoader");
        //create a news loader for the given url
        NewsLoader loader = new NewsLoader(this, GUARDIAN_REQUEST_URL);
        return loader;
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "TEST:inLoaderReset");
        //loader resets so we can clear our existing data
        mNewsAdapter.clear();
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newses) {
        Log.i(LOG_TAG, "TEST:onLoadFinished");
        mProgressBar.setVisibility(View.GONE);
        //clear the adapter of previous news data
        mNewsAdapter.clear();
        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        mNewsAdapter.addAll(newses);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmptyStateView = (TextView) findViewById(R.id.empty_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //check for network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = connMgr.getActiveNetworkInfo();
        if (nwInfo != null && nwInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            //start a new loader or re-connect to the existing one
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            Log.i(LOG_TAG, "TEST:initLoader called from within onCreate");
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateView.setText(R.string.no_internet);
        }
        ArrayList<News> news = new ArrayList<>();

        ListView newsList = (ListView) findViewById(R.id.news_list_view);

        //newsList.setEmptyView(mEmptyStateView);
        mNewsAdapter = new NewsAdapter(this, news);
        newsList.setAdapter(mNewsAdapter);

        //clicking on any of the news articles should open a webpage giving more info about this article
        //here we set the listener for itemclick on the listview and create implicit intents
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currNews = mNewsAdapter.getItem(position);
                Uri newsUri = Uri.parse(currNews.getNewsUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
    }
}
