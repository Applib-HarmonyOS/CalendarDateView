package com.yyx.library;

import java.util.Calendar;

public class DateUtils {

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get the day of the month by year and month
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
        return dateOfMonth;
    }

    /**
     * Returns the day of the week on the 1st of the current month
     *
     * @param year  years
     * @param month Month, passed into the system to obtain, does not need to be normal
     */
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

}
