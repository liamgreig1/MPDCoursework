//Liam Greig - S1635449
//Mobile Platform Development Coursework 20/21
package org.liamgreig.gcu.mpdcoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements OnClickListener, EarthquakeAdapter.AdapterClickListener {

    private RecyclerView rvEarthquake;
    private EarthquakeAdapter adapter;
    private Button mapBtn;
    private Button filterBtn;
    private Button listBtn;
    private String result;
    private final String url1 = "";
    private final String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    ArrayList<EarthquakeClass> earthquakeList = new ArrayList<>();
    MapsFragment fragment = new MapsFragment();
    FilterFragment filterFragment = new FilterFragment();
    Context mainContext = this;
    EarthquakeAdapter.AdapterClickListener adapterContext = this;

    Handler handler = new Handler();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Toast.makeText(MainActivity.this, "Landscape Mode", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(MainActivity.this, "Portrait Mode", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components
        mapBtn = findViewById(R.id.btnMap);
        filterBtn = findViewById(R.id.showFilterBtn);
        listBtn = findViewById(R.id.listBtn);
        rvEarthquake = findViewById(R.id.rvEarthquake);
        periodicUpdate.run();
        mapBtn.setOnClickListener(this);
        filterBtn.setOnClickListener(this);
        listBtn.setOnClickListener(this);
        listBtn.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        Log.e("MyTag", "in onClick");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", earthquakeList);
        fragment.setArguments(bundle);
        filterFragment.setArguments(bundle);
        if(v == mapBtn){
            transaction.add(android.R.id.content,fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        if(v == filterBtn){
            transaction.add(android.R.id.content,filterFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            rvEarthquake.setVisibility(View.INVISIBLE);
            filterBtn.setEnabled(false);
            listBtn.setEnabled(true);
        }
        if(v == listBtn){
            transaction.remove(filterFragment).commit();
            filterFragment.onDestroyView();
            rvEarthquake.setVisibility(View.VISIBLE);
            filterBtn.setEnabled(true);
            listBtn.setEnabled(false);
        }
        Log.e("MyTag", "after startProgress");
    }

    private final Runnable periodicUpdate = new Runnable () {
        public void run() {
            // scheduled another events to be in 1 Hour
            AsyncTask<Void, Integer, Void> runTask = new GetXMLAsyncTask();
            handler.postDelayed(periodicUpdate, 10*360000);
            Log.e("MyTag", "Update");
            runTask.execute();
        }
    };

    @Override
    public void onAdapterClickListener(View view, int position) throws ParseException {
        EarthquakeClass earthquakeClass = earthquakeList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.setMessage("More Info !\nLocation: " + earthquakeClass.getLocation() +
                "\nStrength: " + earthquakeClass.getStrength() +
                "\nLat/Long: " + earthquakeClass.getGeoLat() + "/" + earthquakeClass.getGeoLong() +
                "\nCategory: " + earthquakeClass.getCategory() +
                "\nDepth: " + earthquakeClass.getDepth() + " km" +
                "\nDate of Earthquake: " + earthquakeClass.getPubDate()
        );
        builder.setCancelable(false);
        builder.setNegativeButton("Close", ((dialog, which) -> {
            dialog.cancel();
        }));
        AlertDialog alert = builder.create();
        alert.show();
//        Toast.makeText(MainActivity.this, "You Clicked: " + earthquakeClass.getLocation(), Toast.LENGTH_SHORT).show();
    }


    private class GetXMLAsyncTask extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(urlSource);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag", "after ready");

                while ((inputLine = in.readLine()) != null) {
                    if (result != null) {
                        result = result + inputLine;
                    } else {
                        result = inputLine;
                    }
                }
                in.close();

                earthquakeList = XMLPullParserHandler.parseData(result);
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            rvEarthquake.setLayoutManager(new LinearLayoutManager(mainContext));
            adapter = new EarthquakeAdapter(earthquakeList,adapterContext);
            rvEarthquake.setAdapter(adapter);
        }
    }

}