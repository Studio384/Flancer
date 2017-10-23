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
import com.flancer.flancer.FetchJobTask;
import com.flancer.flancer.R;

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

        FetchJobTask jobTask = new FetchJobTask(getActivity(), mJobAdapter);
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
}