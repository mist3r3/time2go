package com.time2.go;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.time2.go.LoginActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import butterknife.ButterKnife;
import butterknife.Bind;



public class CalendarActivity extends AppCompatActivity  {

    MaterialCalendarView calendar;
    ListView listView;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar=(MaterialCalendarView)findViewById(R.id.calendarView);
        final String testString[]={};
        OnDateSelectedListener listenerOnDate=new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {

                //TODO Implement the events of the specified date.
                /*
                To be exact, you need to create an array of strings containing the events from
                the user on the specified date.

                 */
                listView=(ListView)findViewById(R.id.listView);
                String[] car={"Camaro","Tesla","Ferrari","Ferrari","Ferrari","Ferrari","Ferrari"};


                adapter=new ArrayAdapter<String>(listView.getContext(),android.R.layout.simple_list_item_1,car);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    //TODO Implement the onclick passage to the view containing the event.
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getBaseContext(),adapterView.getItemAtPosition(i)+ "is selected",Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        calendar.setOnDateChangedListener(listenerOnDate);

    }



}

