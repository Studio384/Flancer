package com.flancer.flancer.fragments;

/**
 * Created by Yannick on 22/10/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.flancer.flancer.tasks.FetchJobsTask;
import com.flancer.flancer.R;
import com.flancer.flancer.SettingsActivity;

import java.util.ArrayList;

public class JobListFragment extends Fragment {

    ArrayAdapter<String> mJobsAdapter;

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
        inflater.inflate(R.menu.job, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updatedJobs();
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mJobsAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_job,
                        R.id.list_item_job_textview,
                        new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_job);
        listView.setAdapter(mJobsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String job = mJobsAdapter.getItem(position);
                String jobId = position + "";
                Intent intent = new Intent (getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, jobId);

                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updatedJobs() {
        FetchJobsTask jobsTask = new FetchJobsTask(getActivity(), mJobsAdapter);
        jobsTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updatedJobs();
    }
}