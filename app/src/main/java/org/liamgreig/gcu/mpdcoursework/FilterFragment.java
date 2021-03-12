package org.liamgreig.gcu.mpdcoursework;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to create an instance of this fragment.
 */
public class FilterFragment extends Fragment implements View.OnClickListener {

    private EditText startDate;
    private EditText endDate;
    private Date dateStart;
    private Date dateEnd;
    private Button filterBtn;
    private RadioGroup radioGroup;

    ArrayList<EarthquakeClass> filteredEqList = new ArrayList<>();

    @Override
    public void onDestroyView() {
        startDate.setVisibility(View.INVISIBLE);
        endDate.setVisibility(View.INVISIBLE);
        filterBtn.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        super.onDestroyView();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter, container, false);
        startDate = v.findViewById(R.id.startDate);
        endDate = v.findViewById(R.id.endDate);
        filterBtn = v.findViewById(R.id.filterBtn);
        radioGroup = v.findViewById(R.id.radioGroup2);
        radioGroup.clearCheck();
        radioGroup.setEnabled(false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                filterBtn.setEnabled(true);
            }
        });
        startDate.setInputType(InputType.TYPE_NULL);
        endDate.setInputType(InputType.TYPE_NULL);
        filterBtn.setEnabled(false);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
        filterBtn.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog picker;
        if (v == startDate){
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            picker = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    monthOfYear +=1;
                    String pickedDate = dayOfMonth + "/" + monthOfYear + "/" + year;
                    startDate.setText(pickedDate);
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        dateStart = formatter.parse(pickedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, year, month, day);
            picker.show();
        }
        if(v == endDate){
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            picker = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    monthOfYear+=1;
                    String pickedDate = dayOfMonth + "/" + monthOfYear + "/" + year;
                    endDate.setText(pickedDate);
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        dateEnd = formatter.parse(pickedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, year, month, day);
            picker.show();

        }
        if(v == filterBtn){
            filteredEqList = new ArrayList<EarthquakeClass>();
            try {
                getDatesBetweenStartAndFinish();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private void selectedFilter() throws ParseException {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        String radioText = "";
        if(selectedId == -1){
            Toast.makeText(this.getContext(), "No filter has been selected", Toast.LENGTH_SHORT).show();
        }else{
            RadioButton radioButton = radioGroup.findViewById(selectedId);
            radioText = radioButton.getText().toString();
            switch (radioText){
                case "Furthest North":
                    getMostNorth();
                    break;
                case "Furthest South":
                    getMostSouth();
                    break;
                case "Furthest East":
                    getMostEast();
                    break;
                case "Furthest West":
                    getMostWest();
                    break;
                case "Deepest Earthquake":
                    getMostDeep();
                    break;
                case "Shallowest Earthquake":
                    getShallowDepth();
                    break;
                case "Greatest Magnitude":
                    getMostMag();
                    break;
                default:
                    Toast.makeText(this.getContext(), "default", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void getDatesBetweenStartAndFinish() throws ParseException {
        ArrayList<EarthquakeClass> earthquakeList = getArguments().getParcelableArrayList("list");
        if(dateStart == null || dateEnd == null){
            Toast.makeText(this.getContext(), "Please select a date range", Toast.LENGTH_SHORT).show();
        }else{
            for (EarthquakeClass item: earthquakeList){
                if (item.getPubDate().after(dateStart) && item.getPubDate().before(dateEnd)){
                    filteredEqList.add(item);
                }
            }
            if(filteredEqList.isEmpty()){
                Toast.makeText(this.getContext(), "No Earthquakes between those dates", Toast.LENGTH_SHORT).show();
            }else{
                selectedFilter();
            }
        }
    }

    private void getMostNorth() throws ParseException {
        double max = filteredEqList.get(0).getGeoLat();
        EarthquakeClass maxItem = null;
        for (EarthquakeClass item: filteredEqList) {
            if (item.getGeoLat() >= max) {
                maxItem = item;
                max = item.getGeoLat();
            }
        }
        Log.d("TAG", "getMostNorth: " + maxItem);
        if (maxItem != null) {
            displayDialog(maxItem);
        }else{
            Toast.makeText(this.getContext(), "No Earthquakes between those dates", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMostSouth() throws ParseException {
        double min = filteredEqList.get(0).getGeoLat();
        EarthquakeClass minItem = null;
        for (EarthquakeClass item: filteredEqList) {
            if (item.getGeoLat() <= min) {
                minItem = item;
                min = item.getGeoLat();
            }
        }
        Log.d("TAG", "getMostSouth: " + minItem);
        if (minItem != null) {
            displayDialog(minItem);
        }else{
            Toast.makeText(this.getContext(), "No Earthquakes between those dates", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMostEast() throws ParseException {
        double max = filteredEqList.get(0).getGeoLong();
        EarthquakeClass maxItem = null;
        for (EarthquakeClass item: filteredEqList) {
            if (item.getGeoLong() >= max) {
                maxItem = item;
                max = item.getGeoLong();
            }
        }
        Log.d("TAG", "getMostEast: " + maxItem);
        if (maxItem != null) {
            displayDialog(maxItem);
        }else{
            Toast.makeText(this.getContext(), "No Earthquakes between those dates", Toast.LENGTH_SHORT).show();
        }
    }
    private void getMostWest() throws ParseException {
        double min = filteredEqList.get(0).getGeoLong();
        EarthquakeClass minItem = null;
        for (EarthquakeClass item: filteredEqList) {
            if (item.getGeoLong() <= min) {
                minItem = item;
                min = item.getGeoLong();
            }
        }
        Log.d("TAG", "getMostWest: " + minItem);
        if (minItem != null) {
            displayDialog(minItem);
        }else{
            Toast.makeText(this.getContext(), "No Earthquakes between those dates", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMostMag() throws ParseException {
        double max = filteredEqList.get(0).getStrength();
        EarthquakeClass maxItem = null;
        for (EarthquakeClass item: filteredEqList) {
            if (item.getStrength() >= max) {
                maxItem = item;
                max = item.getStrength();
            }
        }
        Log.d("TAG", "getMostMag: " + maxItem);
        if (maxItem != null) {
            displayDialog(maxItem);
        }else{
            Toast.makeText(this.getContext(), "No Earthquakes between those dates", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMostDeep() throws ParseException {
        double max = filteredEqList.get(0).getDepth();
        EarthquakeClass maxItem = null;
        for (EarthquakeClass item: filteredEqList) {
            if (item.getDepth() >= max) {
                maxItem = item;
                max = item.getDepth();
            }
        }
        Log.d("TAG", "getMostDepth: " + maxItem);
        if (maxItem != null) {
            displayDialog(maxItem);
        }else{
            Toast.makeText(this.getContext(), "No Earthquakes between those dates", Toast.LENGTH_SHORT).show();
        }
    }

    private void getShallowDepth() throws ParseException {
        double min = filteredEqList.get(0).getDepth();
        EarthquakeClass minItem = null;
        for (EarthquakeClass item: filteredEqList) {
            if (item.getDepth() <= min) {
                minItem = item;
                min = item.getDepth();
            }
        }
        Log.d("TAG", "getShallowDepth: " + minItem);
        if (minItem != null) {
            displayDialog(minItem);
        }else{
            Toast.makeText(this.getContext(), "No Earthquakes between those dates", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayDialog(EarthquakeClass item) throws ParseException {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("Most Northern Earthquake\nLocation: " + item.getLocation() +
                "\nStrength: " + item.getStrength() +
                "\nLat/Long: " + item.getGeoLat() + "/" + item.getGeoLong() +
                "\nCategory: " + item.getCategory() +
                "\nDepth: " + item.getDepth() + " km" +
                "\nDate of Earthquake: " + item.getPubDate()
        );
        builder.setCancelable(false);
        builder.setNegativeButton("Close", ((dialog, which) -> {
            dialog.cancel();
        }));
        AlertDialog alert = builder.create();
        alert.show();
    }

}