/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.intersistemas.aws;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author willian
 */
public class DateRangeCreator {

    public static Date fromSeconds(Integer quantity) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, quantity);
        return c.getTime();
    }

    public static Date fromMinutes(Integer quantity) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, quantity);
        return c.getTime();
    }

    public static Date fromHours(Integer quantity) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, quantity);
        return c.getTime();
    }

    public static Date fromDays(Integer quantity) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, quantity);
        return c.getTime();
    }

    public static Date fromMonths(Integer quantity) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, quantity);
        return c.getTime();
    }

    public static Date fromYears(Integer quantity) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, quantity);
        return c.getTime();
    }
}
