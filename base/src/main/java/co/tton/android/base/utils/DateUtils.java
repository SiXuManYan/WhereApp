package co.tton.android.base.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    private static final String PATTERN = "yyyy-MM-dd HH:mm";
    private static final String PATTERN_YEAR_MONTH_DAY = "yyyy-MM-dd";
    private static final String PATTERN_YEAR_MONTH = "yyyy-MM";

    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(PATTERN, Locale.getDefault());
        return df.format(date);
    }

    public static String formatYearMonthDay(long timestamp) {
        SimpleDateFormat df = new SimpleDateFormat(PATTERN_YEAR_MONTH_DAY, Locale.getDefault());
        return df.format(new Date(timestamp));
    }

    public static String formatYearMonth(long timestamp) {
        SimpleDateFormat df = new SimpleDateFormat(PATTERN_YEAR_MONTH, Locale.getDefault());
        return df.format(new Date(timestamp));
    }

    public static Date convertToDate(long timeInMillis) {
        return new Date(timeInMillis);
    }

    public static boolean isSameDay(long timestamp1, long timestamp2) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(timestamp1);
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTimeInMillis(timestamp2);
        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);

        return year1 == year2 && month1 == month2 && day1 == day2;
    }

}
