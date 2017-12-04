package com.example.rusha.popularmovies.utilities;

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

/**
 * Created by rusha on 6/16/2017.
 */

public class ReviewUtility {

    public static final String LOG_TAG = ReviewUtility.class.getSimpleName();

    private ReviewUtility() {
    }

    public static String fetchData(String requestURL) {
        // Create URL object
        URL url = createURL(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Parse JSON string and create an {@ArrayList<NewsItem>} object
        String items;
        items = extractMovieData(jsonResponse);

        return items;
    }

    public static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
            Log.v(LOG_TAG, url.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error Creating URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHTTPRequest(URL url) throws IOException {

        // If the url is empty, return early
        String jsonResponse = null;
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and
            // parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results", e);
        } finally {
            // Close connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Close stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream inputstream) throws IOException {
        StringBuilder streamOutput = new StringBuilder();
        if (inputstream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputstream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                streamOutput.append(line);
                line = reader.readLine();
            }
        }
        return streamOutput.toString();
    }

    private static String extractMovieData(String DataJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(DataJSON)) {
            return null;
        }

        String url = "";

        try {

            JSONObject rootObject = new JSONObject(DataJSON);
            JSONArray resultsArray = rootObject.getJSONArray("results");
            // Variables for JSON parsing
            if (resultsArray != null) {
                if (resultsArray.getJSONObject(0) != null) {
                    JSONObject current = resultsArray.getJSONObject(0);
                    if (current != null)
                        if (current.has("url"))
                            url = current.getString("url");
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        // Return the list of Items
        return url;
    }
}
