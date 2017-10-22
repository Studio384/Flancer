package com.flancer.flancer.fragments;

/**
 * Created by Yannick on 22/10/2017.
 */


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flancer.flancer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Create the menu
        inflater.inflate(R.menu.jobfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            FetchJobTask jobTask = new FetchJobTask();
            jobTask.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public class FetchJobTask extends AsyncTask<Void, Void, String[]> {
        private final String LOG_TAG = FetchJobTask.class.getSimpleName();

        private String getTitle(String title) {
            return title;
        }

        private String getDescription(String description) {
            return description;
        }

        private String getAddressStreet(String street, String number) {
            return street + " " + number;
        }

        private String getAddressCity(String zip, String city) {
            return zip + " " + city;
        }

        private String getCountry(String country) {
            return country;
        }

        private int getCompanyId(int company_id) {
            return company_id;
        }

        /**
         * Parse the data from the full json string that we will receive
         */
        private String[] getJobDataFromJson(String jobJsonStr) throws JSONException {
            JSONObject jobJson = new JSONObject(jobJsonStr);
            JSONArray jobArray = jobJson.getJSONArray("");

            String[] resultStrs = new String[jobArray.length()];
            for (int i = 0; i < jobArray.length(); i++) {
                int company_id;
                String title;
                String city;

                JSONObject jobObject = jobArray.getJSONObject(i);

                company_id = jobObject.getInt("company_id");
                title = jobObject.getString("title");
                city = jobObject.getString("city");

                resultStrs[i] = company_id + " " + title + " in " + city;
            }

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "Job entry: " + s);
            }

            return resultStrs;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jobJsonString = null;

            try {
                final String FLANCER_BASE_URL = "http://flancer.studio384.be/job/";

                Uri builtUri = Uri.parse(FLANCER_BASE_URL).buildUpon().build();

                URL url = new URL(builtUri.toString());

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
                Log.e(LOG_TAG, "Error ", e);
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
        protected void onPostExecute(String[] result) {
            if (result != null) {
                mJobAdapter.clear();

                for (String jobStr : result) {
                    mJobAdapter.add(jobStr);
                }
            }
        }
    }
}