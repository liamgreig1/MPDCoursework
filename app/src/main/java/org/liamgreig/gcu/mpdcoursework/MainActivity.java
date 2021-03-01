package org.liamgreig.gcu.mpdcoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView rawDataDisplay;
    private RecyclerView rvEarthquake;
    private EarthquakeAdapter adapter;
    private String result;
    private String url1 = "";
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    ArrayList<EarthquakeClass> earthquakeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView) findViewById(R.id.rawDataDisplay);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        Log.e("MyTag", "after startButton");
        EarthquakeClass anQuake = new EarthquakeClass("Test","Test","Test","Test","Test","Test","Test","Test","Test");
        earthquakeList.add(anQuake);
        startProgress();
        rvEarthquake = findViewById(R.id.rvEarthquake);
        rvEarthquake.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EarthquakeAdapter(earthquakeList);
        rvEarthquake.setAdapter(adapter);
        // More Code goes here
    }

    @Override
    public void onClick(View v) {
        Log.e("MyTag", "in onClick");

        Log.e("MyTag", "after startProgress");
    }

    public void startProgress() {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();


    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable {

        private String url;

        public Task(String aurl) {
            url = aurl;
        }
        @Override
        public void run() {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag", "after ready");
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                //
                while ((inputLine = in.readLine()) != null) {
                    if (result != null) {
                        result = result + inputLine;
                    } else {
                        result = inputLine;
                    }

//                    Log.e("MyTag", inputLine);

                }
                in.close();

                earthquakeList = XMLPullParserHandler.parseData(result);
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }

            //
            // Now that you have the xml data you can parse it
            //

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(earthquakeList.toString());
                }
            });
        }
    }
}