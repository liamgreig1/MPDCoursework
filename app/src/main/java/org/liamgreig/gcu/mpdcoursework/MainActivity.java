package org.liamgreig.gcu.mpdcoursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvEarthquake;
    private EarthquakeAdapter adapter;
    private String result;
    private String url1 = "";
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    ArrayList<EarthquakeClass> earthquakeList = new ArrayList<>();
    Context mainContext = this;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("MyTag", "in onCreate");
        // Set up the raw links to the graphical components
        rvEarthquake = findViewById(R.id.rvEarthquake);
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        periodicUpdate.run();
    }



    @Override
    public void onClick(View v) {
        Log.e("MyTag", "in onClick");

        Log.e("MyTag", "after startProgress");
    }

    private Runnable periodicUpdate = new Runnable () {
        public void run() {
            // scheduled another events to be in 1 Hour
            AsyncTask<Void, Integer, Void> runTask = new GetXMLAsyncTask();
            handler.postDelayed(periodicUpdate, 10*360000);
            Log.e("MyTag", "Update");
            runTask.execute();
        }
    };


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

//                    Log.e("MyTag", inputLine);

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
            Toast.makeText(MainActivity.this,"Invoke onPostExecute()", Toast.LENGTH_SHORT).show();
            rvEarthquake.setLayoutManager(new LinearLayoutManager(mainContext));
            adapter = new EarthquakeAdapter(earthquakeList);
            rvEarthquake.setAdapter(adapter);

        }
    }

}