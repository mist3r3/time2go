package com.time2.go;

import com.time2.go.DateType;



public class EventDetails {
    public EventBasic header;
    public DateType startDate;
    public DateType endDate;
    public DateType estimatedDate;
    public Location sourceLocation;
    public Location destinationLocation;
    public String eventDescription;
    public int alertInterval;

    public EventDetails()
    {
        header = new EventBasic();
        startDate = new DateType();
        endDate = new DateType();
        estimatedDate = new DateType();
        sourceLocation = new Location();
        destinationLocation = new Location();
    }
}
