package com.time2.go;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateType {
    public int day;
    public int month;
    public int year;

    public int hours;
    public int minutes;

    public DateType() {
    }

    public String toString() {
        if (year < 2016 || year > 2100 || month > 12 || month < 1 || day > 31 || day < 1 || hours < 0 || hours > 23 || minutes < 0 || minutes > 59)
            return "";

        Integer monthInt = new Integer(month);
        Integer dayInt = new Integer(day);
        Integer hoursInt = new Integer(hours);
        Integer minutesInt = new Integer(minutes);
        String monthString;
        String dayString;
        String hoursString;
        String minutesString;

        if (month < 10)
            monthString = "0" + month;
        else
            monthString = monthInt.toString();

        if (day < 10)
            dayString = "0" + day;
        else
            dayString = dayInt.toString();

        if (hours < 10)
            hoursString = "0" + hours;
        else
            hoursString = hoursInt.toString();

        if (minutes < 10)
            minutesString = "0" + minutes;
        else
            minutesString = minutesInt.toString();


        return this.year + "-" + monthString + "-" + dayString + " " + hoursString + ":" + minutesString + ":00";
    }

    public String getDateOnly() {
        if (year < 2016 || year > 2100 || month > 12 || month < 1 || day > 31 || day < 1 || hours < 0 || hours > 23 || minutes < 0 || minutes > 59)
            return "";

        Integer monthInt = new Integer(month);
        Integer dayInt = new Integer(day);

        String monthString;
        String dayString;

        if (month < 10)
            monthString = "0" + month;
        else
            monthString = monthInt.toString();

        if (day < 10)
            dayString = "0" + day;
        else
            dayString = dayInt.toString();

        return this.year + "-" + monthString + "-" + dayString;
    }

    public void stringToDate(String myDateString) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            Date date = fmt.parse(myDateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            this.day=cal.get(Calendar.DAY_OF_MONTH);
            this.month=cal.get(Calendar.MONTH);
            this.year=cal.get(Calendar.YEAR);
            this.hours=cal.get(Calendar.HOUR_OF_DAY);
            this.minutes=cal.get(Calendar.MINUTE);
        } catch(ParseException pe){

        }


    }
    public String getHourOnly() {
        Integer hoursInt = new Integer(hours);
        Integer minutesInt = new Integer(minutes);
        String hoursString;
        String minutesString;

        if (hours < 10)
            hoursString = "0" + hours;
        else
            hoursString = hoursInt.toString();

        if (minutes < 10)
            minutesString = "0" + minutes;
        else
            minutesString = minutesInt.toString();

        return hoursString+ ":" + minutesString;

    }
}