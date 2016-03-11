package com.time2.go;

import android.app.usage.UsageEvents;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.renderscript.Element;

/**
 * Created by eliemadar on 3/10/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TTG";

    // Table Names
    private static final String TABLE_USER = "ttg_login";
    private static final String TABLE_EVENTS = "ttg_events";

    // Column name user
    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";
    private static final String USER_MAIL = "userMail";
    private static final String USER_PASSWORD = "userPassword";

    // Column name events
    private static final String EVENT_NAME = "eventName";
    private static final String EVENT_DESCRIPTION = "eventDescription";
    private static final String EVENT_SOURCE_LOCATION_NAME = "eventSourceLocationName";
    private static final String EVENT_DEST_LOCATION_NAME = "eventDestLocationName";
    private static final String EVENT_SOURCE_LONG = "eventSourceLong";
    private static final String EVENT_SOURCE_LAT = "eventSourceLat";
    private static final String EVENT_DEST_LONG = "eventDestLong";
    private static final String EVENT_DEST_LAT = "eventDestLat";
    private static final String EVENT_START_TIME = "eventStartTime";
    private static final String EVENT_END_TIME = "eventEndTime";
    private static final String EVENT_ESTIMATED = "eventEstimated";
    private static final String EVENT_INTERVAL = "eventInterval";




    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + USER_NAME + " TEXT,"
            + USER_MAIL + " TEXT,"
            + USER_PASSWORD + " TEXT" + ");";

    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE "
            + TABLE_EVENTS + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EVENT_NAME + " TEXT,"
            + EVENT_DESCRIPTION + " TEXT,"
            + EVENT_SOURCE_LOCATION_NAME + " TEXT,"
            + EVENT_DEST_LOCATION_NAME + " TEXT,"
            + EVENT_SOURCE_LONG + " REAL,"
            + EVENT_SOURCE_LAT + " REAL,"
            + EVENT_DEST_LONG + " REAL,"
            + EVENT_DEST_LAT + " REAL,"
            + EVENT_START_TIME + " TEXT,"
            + EVENT_END_TIME + " TEXT,"
            + EVENT_ESTIMATED + " TEXT,"
            + EVENT_INTERVAL + " INTEGER" + ");";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_EVENTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        // create new tables
        onCreate(db);
    }
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_MAIL, user.userEmail);
        values.put(USER_NAME, user.userName);
        values.put(USER_PASSWORD, user.userPassword);

        // insert row
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    public boolean checkUser(){
    String myQuery = "SELECT * FROM " + TABLE_USER + " WHERE " + USER_NAME + " = 'elie'; ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(myQuery, null);


        if (c == null || c.getCount()<=0)
            return false;

        c.moveToFirst();

        User td = new User();
        td.userEmail= c.getString(c.getColumnIndex(USER_MAIL));
        td.userName= c.getString(c.getColumnIndex(USER_NAME));

        if (td.userName.equals("elie")){
            return true;
        }
        return false;
    }
    public void addEvent(EventDetails event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, event.header.name);
        values.put(EVENT_DESCRIPTION,event.eventDescription);
        values.put(EVENT_SOURCE_LOCATION_NAME, event.sourceLocation.locationName);
        values.put(EVENT_DEST_LOCATION_NAME, event.destinationLocation.locationName);
        values.put(EVENT_SOURCE_LONG, event.sourceLocation.locationLong);
        values.put(EVENT_SOURCE_LAT, event.sourceLocation.locationLat);
        values.put(EVENT_DEST_LONG, event.destinationLocation.locationLong);
        values.put(EVENT_DEST_LAT, event.destinationLocation.locationLat);
        values.put(EVENT_START_TIME, event.startDate.toString());
        values.put(EVENT_END_TIME, event.endDate.toString());
        values.put(EVENT_ESTIMATED, event.estimatedDate.toString());
        values.put(EVENT_INTERVAL, event.alertInterval);

        // insert row
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }
    public EventDetails[] displayEvents(DateType date) {
        SQLiteDatabase db = this.getReadableDatabase();

        String myQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + EVENT_START_TIME +  " LIKE '%" +
                            date.getDateOnly() + "%';";

        Cursor c = db.rawQuery(myQuery, null);
        int i=0;
        if (c == null || c.getCount()<=0)
            return null;
        int count = c.getCount();
        EventDetails[] eventDetails=new EventDetails[count];
        c.moveToFirst();
        while (i!=c.getCount()) {
            eventDetails[i]=new EventDetails();
            eventDetails[i].header.name = c.getString(c.getColumnIndex(EVENT_NAME));
            eventDetails[i].header.id = c.getInt(c.getColumnIndex(USER_ID));
            i++;
            c.moveToNext();
        }
        // insert row
        db.close();
        return eventDetails;
    }
    public int getIdEventFromName(String name){
    SQLiteDatabase db=this.getReadableDatabase();
        String myQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + EVENT_NAME +  " = '" +
                name + "';";
        Cursor c = db.rawQuery(myQuery, null);
        if (c == null || c.getCount()<=0)
            return 0;
        c.moveToFirst();

        int id = c.getInt(c.getColumnIndex(USER_ID));
        return id;
    }

    public EventDetails getEventDetail(int idEvent){
        SQLiteDatabase db=this.getReadableDatabase();
        EventDetails myEvent;
        myEvent=new EventDetails();
        String myQuery = "SELECT * FROM " + TABLE_EVENTS + " WHERE " + USER_ID +  " = '" +
                Integer.toString(idEvent) + "';";
        Cursor c = db.rawQuery(myQuery, null);
        if (c == null || c.getCount()<=0)
            return null;
        c.moveToFirst();
        myEvent.header.id=c.getInt(c.getColumnIndex(USER_ID));
        myEvent.header.name = c.getString(c.getColumnIndex(EVENT_NAME));
        myEvent.eventDescription = c.getString(c.getColumnIndex(EVENT_DESCRIPTION));
        myEvent.sourceLocation.locationName=c.getString(c.getColumnIndex(EVENT_SOURCE_LOCATION_NAME));
        myEvent.destinationLocation.locationName=c.getString(c.getColumnIndex(EVENT_DEST_LOCATION_NAME));
        myEvent.startDate.stringToDate(c.getString(c.getColumnIndex(EVENT_START_TIME)));
        myEvent.endDate.stringToDate(c.getString(c.getColumnIndex(EVENT_END_TIME)));
        myEvent.alertInterval=c.getInt(c.getColumnIndex(EVENT_INTERVAL));

        return myEvent;
    }
    public void updateEvent(EventDetails event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, event.header.name);
        values.put(EVENT_DESCRIPTION,event.eventDescription);
        values.put(EVENT_SOURCE_LOCATION_NAME, event.sourceLocation.locationName);
        values.put(EVENT_DEST_LOCATION_NAME, event.destinationLocation.locationName);
        values.put(EVENT_SOURCE_LONG, event.sourceLocation.locationLong);
        values.put(EVENT_SOURCE_LAT, event.sourceLocation.locationLat);
        values.put(EVENT_DEST_LONG, event.destinationLocation.locationLong);
        values.put(EVENT_DEST_LAT, event.destinationLocation.locationLat);
        values.put(EVENT_START_TIME, event.startDate.toString());
        values.put(EVENT_END_TIME, event.endDate.toString());
        values.put(EVENT_ESTIMATED, event.estimatedDate.toString());
        values.put(EVENT_INTERVAL, event.alertInterval);
        db.update(TABLE_EVENTS, values, USER_ID + " = ?", new String[]{Integer.toString(event.header.id)});

        // insert row
        db.close();
    }
    public void deleteEvent (EventDetails event){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_EVENTS, USER_ID + " = ?", new String[]{Integer.toString(event.header.id)});
    }
}
