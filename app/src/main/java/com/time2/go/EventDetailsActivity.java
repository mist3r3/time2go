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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.DecimalFormat;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliemadar on 1/5/16.
 */
public class EventDetailsActivity extends AppCompatActivity {
    private static final String TAG = "";
    @Bind(R.id.btn_modify) Button _modifybutton;
    @Bind(R.id.btn_delete) Button _deletebutton;
    @Bind(R.id.event_detail_date) TextView _datetext;
    @Bind(R.id.event_detail_name) EditText _nametext;
    @Bind(R.id.event_detail_beginning_time) TextView _beginningtext;
    @Bind(R.id.event_detail_end_time) TextView _endtext;
    @Bind(R.id.event_detail_margin_time) EditText _margintext;
    int hourTimeBeg=0;
    int hourTimeEnd=0;
    int minTimeBeg=0;
    int minTimeEnd=0;


    @Bind(R.id.event_detail_description) EditText _descriptiontext;
    EventDetails event;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        final Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        ButterKnife.bind(this);
        Intent i=getIntent();
        Bundle extras=i.getExtras();
        int idEvent=0;
        event=new EventDetails();
        if (extras != null && i!=null) {
            idEvent = i.getIntExtra("idEvent", 0);
        }
        db=new DatabaseHelper(getApplicationContext());
        event=db.getEventDetail(idEvent);

        hourTimeBeg=event.startDate.hours;
        hourTimeEnd=event.endDate.hours;
        minTimeBeg=event.startDate.minutes;
        minTimeEnd=event.endDate.minutes;

        _datetext.setText(event.startDate.getDateOnly());
        _nametext.setText(event.header.name);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_detail);
        autocompleteFragment.setText(event.sourceLocation.locationName);
        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2_detail);
        autocompleteFragment.setText(event.sourceLocation.locationName);
        autocompleteFragment2.setText(event.destinationLocation.locationName);
        _descriptiontext.setText(event.eventDescription);

        _beginningtext.setText(event.startDate.getHourOnly());
        _endtext.setText(event.endDate.getHourOnly());
        _margintext.setText(Integer.toString(event.alertInterval));
        _modifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //event.header.name = _nametext.getText().toString();
                event.eventDescription = _descriptiontext.getText().toString();
                event.startDate.hours = hourTimeBeg;
                event.endDate.hours = hourTimeEnd;
                event.startDate.minutes = minTimeBeg;
                event.endDate.minutes = minTimeEnd;

                db.updateEvent(event);
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });
        _deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteEvent(event);
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        _beginningtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(EventDetailsActivity.this, onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });
        _endtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(EventDetailsActivity.this, onTimeSetListener2, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                event.sourceLocation.locationLat = place.getLatLng().latitude;
                event.sourceLocation.locationLong = place.getLatLng().longitude;
                event.sourceLocation.locationName = place.getName().toString();
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                event.destinationLocation.locationLat = place.getLatLng().latitude;
                event.destinationLocation.locationLong = place.getLatLng().longitude;
                event.destinationLocation.locationName = place.getName().toString();
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
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
