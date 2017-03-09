package com.aasavari.newsapp;

/**
 * Created by Aasavari on 3/2/2017.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.input;
import static com.aasavari.newsapp.MainActivity.LOG_TAG;


/**
 * Helper methods related to requesting and receiving news data from Guardian API.
 */

public final class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */

    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL");
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if the url is null, then return early
        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //if the request was successful(response code 200),
            //then read the input stream and parse the response.

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else
                Log.e(LOG_TAG, "Error Response Code: " + urlConnection.getResponseCode());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news json results", e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractResultsFromJson(String newsJSON) {
        //if the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(newsJSON))
            return null;

        //create an empty ArrayList that we can add news to
        List<News> news = new ArrayList<News>();

        // Try to parse the JSON response string.. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            //create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            //now extract the json object associated with the response object
            JSONObject responseObj = baseJsonResponse.getJSONObject("response");
            //now extract the JSONArray associated with the key called "results" which represents the
            //each news article and its associated information
            JSONArray newsArray = responseObj.getJSONArray("results");

            //for each news article in the newsArray, create an(@link News) object
            for (int i = 0; i < newsArray.length(); i++) {
                //get a single news object at position i within the newsArray
                JSONObject item = newsArray.getJSONObject(i);
                String fullName = "";
                //for a given news item object, extract the keys called webTitle, sectionName and webUrl
                String webTitle = item.getString("webTitle");
                Log.i(LOG_TAG, "TEST: webTitle is " + webTitle);
                String sectionName = item.getString("sectionName");
                Log.i(LOG_TAG, "TEST: sectionName is " + sectionName);
                String webUrl = item.getString("webUrl");
                Log.i(LOG_TAG, "TEST: webUrl is " + webUrl);
                JSONArray tagsArray = item.getJSONArray("tags");
                for(int j = 0; j<tagsArray.length(); j++){
                    JSONObject tagItem = tagsArray.getJSONObject(j);
                    String firstName = tagItem.getString("firstName");
                    String lastName = tagItem.getString("lastName");
                    fullName = firstName + " " + lastName;
                }

                // Create a new {@link News} object with the title, section
                // and url from the JSON response.
                // Add the new {@link News} to the list of News.
                news.add(new News(webTitle, sectionName, fullName, webUrl));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        }

        // Return the News list
        return news;
    }

    public static List<News> fetchNewsData(String requestUrl) {

        //Create a url object;
        URL url = createURL(requestUrl);

        //perform an HTTPRequest to this url and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request", e);
        }
        List<News> news = extractResultsFromJson(jsonResponse);
        return news;
    }


} // QueryUtils class
