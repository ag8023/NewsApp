package com.aasavari.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Aasavari on 3/2/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    //Query Url
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<News> loadInBackground() {
        //here we execute the network request
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (mUrl == null)
            return null;
        Log.i(LOG_TAG, "TEST:loadInBackground:AsyncTaskLoader");
        List<News> result = QueryUtils.fetchNewsData(mUrl);
        return result;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "Test:onStartLoading");
    }
}
