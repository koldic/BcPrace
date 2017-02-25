/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author David
 */
public class DateFormatCount {
    
    private final String dateFormat;
    private String newDateFormat;
    private final Locale locale;
    private final DateFormat simpleDateFormat;
    private DateFormat newSimpleDateFormat;
    private int dateFormatCount = 0;
    private boolean dateFormatChanged = false;

    public DateFormatCount(String dateFormat, Locale local) {
        this.dateFormat = dateFormat;
        this.locale = local;
        this.simpleDateFormat = new SimpleDateFormat(dateFormat, this.locale);
        this.dateFormatCount++;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public int getDateFormatCount() {
        return dateFormatCount;
    }
    
    public void adddDateFormatCount(){
        this.dateFormatCount++;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getNewDateFormat() {
        return newDateFormat;
    }

    public DateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public DateFormat getNewSimpleDateFormat() {
        return newSimpleDateFormat;
    }

    public boolean isDateFormatChanged() {
        return dateFormatChanged;
    }
    
    public void changeDateFormat(String newDateFormat){
        this.newDateFormat = newDateFormat;
        this.newSimpleDateFormat = new SimpleDateFormat(this.newDateFormat, this.locale);
        this.dateFormatChanged = true;
    }
    
}
