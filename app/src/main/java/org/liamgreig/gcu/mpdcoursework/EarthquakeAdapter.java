package org.liamgreig.gcu.mpdcoursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.ViewHolder> {

    private List<EarthquakeClass> mEarthquake;
//    private LayoutInflater mInflator;
//    private ItemClickListner mClickListner;

    public EarthquakeAdapter(List<EarthquakeClass> earthquakes){
        mEarthquake = earthquakes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textLocation;
        public TextView textStrength;
        public Button infoBtn;

        public ViewHolder(View itemView){
            super(itemView);

            textLocation = itemView.findViewById(R.id.textLocation);
            textStrength = itemView.findViewById(R.id.textStrength);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View earthquakeView = inflater.inflate(R.layout.earthquake_item,  parent, false);

        ViewHolder viewHolder = new ViewHolder(earthquakeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EarthquakeClass earthquake = mEarthquake.get(position);

        TextView txtLoc = holder.textLocation;
        txtLoc.setText(earthquake.getLocation());
        TextView txtStr = holder.textStrength;
        txtStr.setText(earthquake.getStrength());
        Button infoButton = holder.infoBtn;


    }

    @Override
    public int getItemCount() {
        return mEarthquake.size();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
