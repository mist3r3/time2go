package com.time2.go;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


public class CalendarActivity extends AppCompatActivity  {

    MaterialCalendarView calendar;
    ListView listView;
    ArrayAdapter<String> adapter;
    CalendarDay date;
    int startDateDay=0;
    int startDateMonth=0;
    int startDateYear=0;
    int endDateDay=0;
    int endDateMonth=0;
    int endDateYear=0;
    DateType dateType=new DateType();
    EventDetails[] listEvent;
    String[] myString;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*Start the select date option*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar=(MaterialCalendarView)findViewById(R.id.calendarView);
        db=new DatabaseHelper(getApplicationContext());

        OnDateSelectedListener listenerOnDate=new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {

                //TODO Implement the events of the specified date.
                /*
                To be exact, you need to create an array of strings containing the events from
                the user on the specified date.

                 */
                startDateDay=date.getDay();
                startDateMonth=date.getMonth();
                startDateYear=date.getYear();
                endDateDay=date.getDay();
                endDateMonth=date.getMonth();
                endDateYear=date.getYear();
                dateType.day=startDateDay;
                dateType.month=startDateMonth;
                dateType.year=startDateYear;


                listView=(ListView)findViewById(R.id.listView);
                listEvent=db.displayEvents(dateType);
                if (listEvent!=null) {
                    int n= listEvent.length;
                    int i=0;
                    myString=new String[n];
                    while (i<n){
                        myString[i]=listEvent[i].header.name;
                        i++;

                    }
                    adapter = new ArrayAdapter<String>(listView.getContext(), android.R.layout.simple_list_item_1, myString);
                    listView.setAdapter(adapter);
                }
                else {
                    String[] myStringEmpty = new String[1];
                    myStringEmpty[0]="No item";
                    listView.setEmptyView(listView);
                    adapter = new ArrayAdapter<String>(listView.getContext(), android.R.layout.simple_list_item_1, myStringEmpty);
                    listView.setAdapter(adapter);
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    //TODO Implement the onclick passage to the view containing the event.
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String check=(String)adapterView.getItemAtPosition(0);
                        boolean checkBool = check.equals("No item");
                        if (checkBool) return;
                        String myName = (String)adapterView.getItemAtPosition(i);
                        Toast.makeText(getBaseContext(),adapterView.getItemAtPosition(i) + "with id : " +
                                 " is selected",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), EventDetailsActivity.class);
                        int idEvent=0;
                        i=0;
                        while (i!=listEvent.length){
                            if (myName.equals(listEvent[i].header.name)){
                                idEvent=listEvent[i].header.id;
                                break;
                            }
                            i++;
                        }
                        Toast.makeText(getBaseContext(),adapterView.getItemAtPosition(i) + "with id : " +
                               idEvent + " is selected",Toast.LENGTH_LONG).show();
                        intent.putExtra("idEvent",idEvent);
                        intent.putExtra("startDateDay",startDateDay);
                        intent.putExtra("startDateMonth",startDateMonth);
                        intent.putExtra("startDateYear",startDateYear);
                        intent.putExtra("endDateDay",endDateDay);
                        intent.putExtra("endDateMonth", endDateMonth);
                        intent.putExtra("endDateYear",endDateYear);
                        startActivityForResult(intent, 0);

                    }
                });

            }
        };
        calendar.setOnDateChangedListener(listenerOnDate);

        /*Start the onclicklistener*/

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddEventActivity.class);
                intent.putExtra("startDateDay",startDateDay);
                intent.putExtra("startDateMonth",startDateMonth);
                intent.putExtra("startDateYear",startDateYear);
                intent.putExtra("endDateDay",endDateDay);
                intent.putExtra("endDateMonth",endDateMonth);
                intent.putExtra("endDateYear",endDateYear);
                startActivityForResult(intent, 0);
            }
        });

    }



}

