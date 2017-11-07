package com.flancer.flancer.fragments;

/**
 * Created by Yannick on 22/10/2017.
 */

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.flancer.flancer.R;
import com.flancer.flancer.SettingsActivity;
import com.flancer.flancer.tasks.FetchJobTask;
import com.flancer.flancer.tasks.FetchJobsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DetailFragment extends Fragment {

    ArrayAdapter<String[]> mJobAdapter;

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String FLANCER_SHARE_STRING = " with Flancer";
    private String mJobId;
    private int iJobId;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        if (id == R.id.menu_item_share) {
            startShareIntent();
            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[][] resultStrs = new String[0][];

        try {
            resultStrs = new FetchJobTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mJobId = intent.getStringExtra(Intent.EXTRA_TEXT) + "";
            iJobId = Integer.parseInt(mJobId);

            ((TextView) rootView.findViewById(R.id.title)).setText(resultStrs[iJobId][0]);
            ((TextView) rootView.findViewById(R.id.company)).setText(resultStrs[iJobId][1]);
            ((TextView) rootView.findViewById(R.id.description)).setText(resultStrs[iJobId][6]);
            ((TextView) rootView.findViewById(R.id.phone)).setText(resultStrs[iJobId][2]);
            ((TextView) rootView.findViewById(R.id.email)).setText(resultStrs[iJobId][3]);
            ((TextView) rootView.findViewById(R.id.date)).setText("From " + resultStrs[iJobId][4] +
                    System.lineSeparator() + "Until " + resultStrs[iJobId][5]);
            ((TextView) rootView.findViewById(R.id.address)).setText(
                    resultStrs[iJobId][7] + " " + resultStrs[iJobId][8] + System.lineSeparator() +
                    resultStrs[iJobId][9] + " " + resultStrs[iJobId][10] + System.lineSeparator() +
                    resultStrs[iJobId][11]);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail, menu);
    }
    
    public void startShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, mJobId + FLANCER_SHARE_STRING);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }
}