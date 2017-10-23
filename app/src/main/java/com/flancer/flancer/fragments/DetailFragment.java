package com.flancer.flancer.fragments;

/**
 * Created by Yannick on 22/10/2017.
 */

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flancer.flancer.DetailActivity;
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

public class DetailFragment extends Fragment {

    ArrayAdapter<String> mJobAdapter;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Create the menu
        inflater.inflate(R.menu.job, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FetchJobTask jobTask = new FetchJobTask();
        jobTask.execute();

        mJobAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_job,
                        R.id.list_item_job_textview);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_job);
        listView.setAdapter(mJobAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String job = mJobAdapter.getItem(position);
                Intent intent = new Intent (getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, job);

                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchJobTask extends AsyncTask<Void, Void, String[]> {
        private final String LOG_TAG = FetchJobTask.class.getSimpleName();

        private int getId(int id) {
            return id;
        }

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
            JSONArray jobArray = new JSONArray(jobJsonStr);

            String[] resultStrs = new String[jobArray.length()];
            for (int i = 0; i < jobArray.length(); i++) {
                int id;
                int company_id;
                String title;
                String city;

                JSONObject jobObject = jobArray.getJSONObject(i);

                id = jobObject.getInt("id");
                company_id = jobObject.getInt("company_id");
                title = jobObject.getString("title");
                city = jobObject.getString("city");

                resultStrs[i] = "Job " + id + " for " + company_id + " " + title + " in " + city;
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