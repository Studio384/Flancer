package com.flancer.flancer.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yannick on 6/11/2017.
 */

public class FetchJobTask extends AsyncTask<Void, Void, String[][]> {
    private final String LOG_TAG = FetchJobsTask.class.getSimpleName();

    /**
     * Parse the data from the full json string that we will receive
     */
    private String[][] getJobDataFromJson(String jobJsonStr) throws JSONException {
        JSONArray jobArray = new JSONArray(jobJsonStr);

        String[][] resultStrs = new String[jobArray.length()][9];
        for (int i = 0; i < jobArray.length(); i++) {
            int id;
            String company;
            String title;
            String city;

            JSONObject jobObject = jobArray.getJSONObject(i);

            resultStrs[i][0] = jobObject.getString("title");
            resultStrs[i][1] = jobObject.getString("company");
            resultStrs[i][2] = jobObject.getString("description");
            resultStrs[i][3] = jobObject.getString("street");
            resultStrs[i][4] = jobObject.getString("number");
            resultStrs[i][5] = jobObject.getString("zip");
            resultStrs[i][6] = jobObject.getString("city");
            resultStrs[i][7] = jobObject.getString("country");
            resultStrs[i][8] = jobObject.getString("minimum_bid");
        }

        return resultStrs;
    }

    @Override
    protected String[][] doInBackground(Void... params) {
        HttpURLConnection jobUrlConnection = null;
        BufferedReader reader = null;
        String jobJsonString = null;

        try {
            final String FLANCER_JOB_BASE_URL = "http://flancer.studio384.be/job/";

            Uri jobUri = Uri.parse(FLANCER_JOB_BASE_URL).buildUpon().build();

            URL jobUrl = new URL(jobUri.toString());

            // Create the request to Flancer, and open the connection
            jobUrlConnection = (HttpURLConnection) jobUrl.openConnection();
            jobUrlConnection.setRequestMethod("GET");
            jobUrlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = jobUrlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) { // Empty means we've got nothing to do
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Simplify debugging with new lines
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) { // Stream was empty, don't parse
                return null;
            }

            jobJsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // Fail means we've got nothing to do
            return null;
        } finally {
            if (jobUrlConnection != null) {
                jobUrlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getJobDataFromJson(jobJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[][] result) {
    }
}
