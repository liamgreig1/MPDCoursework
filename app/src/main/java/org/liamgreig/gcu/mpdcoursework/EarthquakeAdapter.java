package org.liamgreig.gcu.mpdcoursework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import static org.liamgreig.gcu.mpdcoursework.R.layout.earthquake_item;

public class EarthquakeAdapter extends
        RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {

    private List<EarthquakeClass> mEarthquake;
    private AdapterClickListener mAdapterClickListener;

    public EarthquakeAdapter(List<EarthquakeClass> earthquakes, AdapterClickListener adapterClickListener){
        mEarthquake = earthquakes;
        mAdapterClickListener = adapterClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textLocation;
        private TextView textStrength;
        private Button infoBtn;
        private View earthquakeItemView;
        AdapterClickListener adapterClickListener;


        public ViewHolder(View itemView, AdapterClickListener adapterClickListener){
            super(itemView);

            textLocation = itemView.findViewById(R.id.textLocation);
            textStrength = itemView.findViewById(R.id.textStrength);
            infoBtn = itemView.findViewById(R.id.infoBtn);
            earthquakeItemView = itemView.findViewById(R.id.earthquake_itemView);
            this.adapterClickListener = adapterClickListener;
            infoBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            adapterClickListener.onAdapterClickListener(itemView, getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View earthquakeView = inflater.inflate(earthquake_item,  parent, false);

        return new ViewHolder(earthquakeView, mAdapterClickListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EarthquakeClass earthquake = mEarthquake.get(position);
        double strength = Double.parseDouble(earthquake.getStrength());
        Button infoButton = holder.infoBtn;
        View earthItem = holder.earthquakeItemView;
        if (strength<=0.9){
            infoButton.setBackgroundColor(Color.GREEN);
        }
        if(strength>=1 && strength<=2){
            infoButton.setBackgroundColor(Color.YELLOW);
        }
        if(strength>2){
            infoButton.setBackgroundColor(Color.RED);
        }
        TextView txtLoc = holder.textLocation;
        txtLoc.setText("Location: " + earthquake.getLocation());
        TextView txtStr = holder.textStrength;
        txtStr.setText("Strength: " + earthquake.getStrength());

        infoButton.setText("info");
        infoButton.setEnabled(true);


    }

    @Override
    public int getItemCount() {
        return mEarthquake.size();
    }

    // parent activity will implement this method to respond to click events
    public interface AdapterClickListener {
        void onAdapterClickListener(View view, int position);
    }
}
