package com.flancer.flancer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.flancer.flancer.fragments.JobListFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new JobListFragment())
                .commit();
        }
    }
}
