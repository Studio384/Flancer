package com.flancer.flancer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.flancer.flancer.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new DetailFragment())
                .commit();
        }
    }
}