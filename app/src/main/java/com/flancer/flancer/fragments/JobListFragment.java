package com.flancer.flancer.fragments;

/**
 * Created by Yannick on 22/10/2017.
 */


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flancer.flancer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class JobListFragment extends Fragment {

    ArrayAdapter<String> mJobAdapter;

    public JobListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] jobsArray = {
                "Studio 384 &middot; web developer",
                "Flancer &middot; Android developer",
                "Flancer &middot; iOS developer",
                "Flancer &middot; Windows developer",
                "PXL &middot; security teacher"
        };
        List<String> jobList = new ArrayList<String>(Arrays.asList(jobsArray));


        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mJobAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_job, // The name of the layout ID.
                        R.id.list_item_job_textview, // The ID of the textview to populate.
                        jobList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_job);
        listView.setAdapter(mJobAdapter);

        return rootView;
    }

    public class FetchJobTask extends AsyncTask<Void, Void, Void> {
        private final String LOG_TAG = FetchJobTask.class.getSimpleName();

        // Get a reference to the ListView, and attach this adapter to it.

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jobJsonString = null;

            try {
                URL url = new URL("hhttp://flancer.studio384.be/job/");

                // Create the request to Flancer, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
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
                Log.e("PlaceholderFragment", "Error ", e);
                // Fail means we've got nothing to do
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return null;
        }
    }
}