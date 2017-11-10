package com.flancer.flancer.fragments;

/**
 * Created by Yannick on 22/10/2017.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flancer.flancer.R;
import com.flancer.flancer.SettingsActivity;
import com.flancer.flancer.listeners.OnMapAndViewReadyListener;
import com.flancer.flancer.tasks.FetchJobTask;
import com.flancer.flancer.tasks.FetchMapTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;

public class DetailFragment extends Fragment implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    ArrayAdapter<String[]> mJobAdapter;

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private String mJobId;
    private int iJobId;
    private String[][] resultStrs = new String[0][];
    private GoogleMap mMap;
    private LatLng LOCATION;
    private Marker mLocation;

    String street, city, country, company, title, description, phone, email, begin_date, end_date;

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
        try {
            resultStrs = new FetchJobTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Bind data to the UI
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            mJobId = intent.getStringExtra(Intent.EXTRA_TEXT) + "";
            iJobId = Integer.parseInt(mJobId);

            street = resultStrs[iJobId][7] + " " + resultStrs[iJobId][8];
            city = resultStrs[iJobId][9] + " " + resultStrs[iJobId][10];
            country = resultStrs[iJobId][11];
            title = resultStrs[iJobId][0];
            company = resultStrs[iJobId][1];
            description = resultStrs[iJobId][6];
            phone = resultStrs[iJobId][2];
            email = resultStrs[iJobId][3];
            begin_date = resultStrs[iJobId][4];
            end_date = resultStrs[iJobId][5];

            ((TextView) rootView.findViewById(R.id.title)).setText(title);
            ((TextView) rootView.findViewById(R.id.company)).setText(company);
            ((TextView) rootView.findViewById(R.id.description)).setText(description);
            ((TextView) rootView.findViewById(R.id.phone)).setText(phone);
            ((TextView) rootView.findViewById(R.id.email)).setText(email);
            ((TextView) rootView.findViewById(R.id.date)).setText("From " + begin_date +
                    System.lineSeparator() + "Until " + end_date);
            ((TextView) rootView.findViewById(R.id.address)).setText(
                    street + System.lineSeparator() +
                    city + System.lineSeparator() +
                    country);

            // Reverse lookup for address
            String jobAddress =  street + ", " + city + ", " + country;

            double cords[] = new double[2];

            try {
                cords = new FetchMapTask(getActivity()).execute(jobAddress).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            LOCATION = new LatLng(cords[1], cords[0]);
        }

        ((Button) rootView.findViewById(R.id.do_dial)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(i);
            }
        });

        ((Button) rootView.findViewById(R.id.do_mail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                startActivity(i);
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);

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
        String shareString =
                title + System.lineSeparator() + System.lineSeparator() +
                "Company: " + company + System.lineSeparator() + System.lineSeparator() +
                "From: " + begin_date + System.lineSeparator() +
                "Until: " + end_date + System.lineSeparator() + System.lineSeparator() +
                 description + System.lineSeparator() + System.lineSeparator() +
                "Contact information " + System.lineSeparator() +
                "Phone: " + System.lineSeparator() + phone + System.lineSeparator() + System.lineSeparator() +
                "Email: " + System.lineSeparator() + email + System.lineSeparator() + System.lineSeparator() +
                "Address: " + System.lineSeparator() +
                street + System.lineSeparator() +
                city + System.lineSeparator() +
                country + System.lineSeparator() + System.lineSeparator() +
                "Powered by Flancer";
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareString);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add lots of markers to the map.
        addMarkersToMap();

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowLongClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with marker.");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(LOCATION)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    private void addMarkersToMap() {
        // Uses a colored icon.
        mLocation = mMap.addMarker(new MarkerOptions()
                .position(LOCATION)
                .title(company)
                .snippet(street + System.lineSeparator() + city)
                .icon(BitmapDescriptorFactory.defaultMarker()));
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(getActivity(), "Map is not ready yet", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}