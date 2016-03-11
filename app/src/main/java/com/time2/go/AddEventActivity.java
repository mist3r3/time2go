package com.time2.go;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.Bind;
/**
 * Created by eliemadar on 1/5/16.
 */
public class AddEventActivity extends AppCompatActivity  {

    private static final String TAG = "";
    @Bind(R.id.btn_add) Button _addbutton;
    @Bind(R.id.date_add_event) TextView _datetext;
    @Bind(R.id.add_event_name) EditText _nametext;
    @Bind(R.id.add_event_description) EditText _descriptiontext;
    //@Bind(R.id.place_autocomplete_fragment) fragment _sourcetext;
    //@Bind(R.id.place_autocomplete_fragment2) fragment _destinationtext;
    @Bind(R.id.add_beginning_time) TextView _beginningtext;
    @Bind(R.id.add_end_time) TextView _endtext;
    @Bind(R.id.add_event_margin_time) EditText _margintext;
    int hourTimeBeg=9;
    int hourTimeEnd=10;
    int minTimeBeg=0;
    int minTimeEnd=0;
    EventDetails event;
    int test=0;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
        final Calendar calendar = Calendar.getInstance();
        db=new DatabaseHelper(getApplicationContext());
        event=new EventDetails();
        Intent i=getIntent();
        Bundle extras=i.getExtras();
        if (extras != null && i!=null) {
            event.startDate.day = i.getIntExtra("startDateDay", 0);
            event.startDate.month = i.getIntExtra("startDateMonth", 0);
            event.startDate.year = i.getIntExtra("startDateYear", 0);
            event.endDate.day = i.getIntExtra("endDateDay", 0);
            event.endDate.month = i.getIntExtra("endDateMonth", 0);
            event.endDate.year = i.getIntExtra("endDateYear", 0);
        }

        _datetext.setText(event.startDate.getDateOnly());
        /*Autocomplete part START*/
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                event.sourceLocation.locationLat=place.getLatLng().latitude;
                event.sourceLocation.locationLong=place.getLatLng().longitude;
                event.sourceLocation.locationName=place.getName().toString();
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                event.destinationLocation.locationLat=place.getLatLng().latitude;
                event.destinationLocation.locationLong=place.getLatLng().longitude;
                event.destinationLocation.locationName=place.getName().toString();
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        /*Autocomplete part END*/


        _beginningtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(AddEventActivity.this,onTimeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        });
        _endtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(AddEventActivity.this,onTimeSetListener2,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        });
        _addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.header.name=_nametext.getText().toString();
                event.eventDescription=_descriptiontext.getText().toString();
                event.startDate.hours=hourTimeBeg;
                event.startDate.minutes=minTimeBeg;
                event.endDate.hours=hourTimeEnd;
                event.endDate.minutes=minTimeEnd;
                db.addEvent(event);
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener= new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            DecimalFormat df=new DecimalFormat("00");
            String time=String.valueOf(df.format(i))+":"+String.valueOf(df.format(i1));
            hourTimeBeg=i;
            minTimeBeg=i1;
            _beginningtext.setText(time);
        }
    };


    TimePickerDialog.OnTimeSetListener onTimeSetListener2= new TimePickerDialog.OnTimeSetListener(){
      @Override
      public void onTimeSet(TimePicker timePicker, int i, int i1) {
          DecimalFormat df=new DecimalFormat("00");
          java.sql.Date date;
          String time=String.valueOf(df.format(i))+":"+String.valueOf(df.format(i1));
          hourTimeEnd=i;
          minTimeEnd=i1;
          _endtext.setText(time);
      }
    };
}