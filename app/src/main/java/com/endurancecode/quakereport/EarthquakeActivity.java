/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.endurancecode.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    /* URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /*
     * To access and modify the instance of the EarthquakeAdapter from the onPostExecute() method,
     * we need to make it a global variable in the EarthquakeActivity.
     */
    private EarthquakeAdapter earthquakeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        /* Find a reference to the {@link ListView} in the layout */
        ListView earthquakeListView = findViewById(R.id.list);

        /* Create a new {@link EarthquakeAdapter} that takes an empty ArrayList as input */
        earthquakeAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        /* Set the adapter on the {@link ListView} so the list can be populated in the user interface */
        earthquakeListView.setAdapter(earthquakeAdapter);

        /* Set OnItemClickListener in the ListView items */
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* Get the selected {@link Earthquake} object */
                Earthquake selectedEarthquake = earthquakeAdapter.getItem(position);

                /* Set and start a web browser intent with the earthquake url */
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(selectedEarthquake.getUrl()));
                startActivity(browserIntent);
            }
        });

        /* Get a reference to the LoaderManager, in order to interact with loaders. */
        LoaderManager loaderManager = getLoaderManager();

        /*
         * Initialize the loader. Pass in the int ID constant defined above and pass in null for
         * the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
         * because this activity implements the LoaderCallbacks interface).
         */
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        /* Clear the adapter of previous earthquake data */
        earthquakeAdapter.clear();

        /*
         * If there is a valid list of {@link Earthquake}s, then add them to the adapter's
         * data set. This will trigger the ListView to update.
         */
        earthquakeAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        /* Loader reset, so we can clear out our existing data */
        earthquakeAdapter.clear();
    }
}