package com.flancer.flancer.tasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Yannick on 7/11/2017.
 */

public class FetchMapTask extends AsyncTask<String, Void, double[]> {
    private final String LOG_TAG = FetchJobsTask.class.getSimpleName();
    ArrayAdapter<String> mMapAdapter;
    private final Context mContext;
    String mJobAddress;
    double cords[] = new double[2];
    double latitude;

    public FetchMapTask(Context context) {
        mContext = context;
    }

    @Override
    protected double[] doInBackground(String... params) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        List<Address> addresses = null;
        mJobAddress = params[0];

        try {
            addresses = geocoder.getFromLocationName(mJobAddress, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = addresses.get(0);
        cords[0] = address.getLongitude();
        cords[1] = address.getLatitude();

        return cords;
    }

    @Override
    protected void onPostExecute(double[] result) {
    }
}
