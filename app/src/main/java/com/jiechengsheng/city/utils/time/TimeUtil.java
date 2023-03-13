package com.jiechengsheng.city.utils.time;

import android.text.TextUtils;

import com.blankj.utilcode.constant.TimeConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Wangsw on 2017/7/1.
 * <p/>
 * "HH:mm:ss"是24小时制的，"hh:mm:ss"是12小时制。
 */
public class TimeUtil {

    /**
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return xxx xx月|天|小时|分钟前
     */
    public static String getTimeLag(String time) {
        String spaceTime = "";
        if (!TextUtils.isEmpty(time)) {
            Long aLong = timeStrToSecond(time);
            spaceTime = getSpaceTime(aLong);
        }
        return spaceTime;
    }

    /**
     * 从时间(毫秒)中提取出日期
     *
     * @param millisecond
     * @return
     */
    public static String getDateFromMillisecond(Long millisecond) {

        Date date = null;
        try {
            date = new Date(millisecond);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        ////今天
        Calendar today = Calendar.getInstance();

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));

        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        //昨天
        Calendar yesterday = Calendar.getInstance();

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);


        // 今年
        Calendar thisYear = Calendar.getInstance();

        thisYear.set(Calendar.YEAR, current.get(Calendar.YEAR));
        thisYear.set(Calendar.MONTH, 0);
        thisYear.set(Calendar.DAY_OF_MONTH, 0);
        thisYear.set(Calendar.HOUR_OF_DAY, 0);
        thisYear.set(Calendar.MINUTE, 0);
        thisYear.set(Calendar.SECOND, 0);


        current.setTime(date);

        //今年以前
        if (current.before(thisYear)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = format.format(date);
            return dateStr;
        } else if (current.after(today)) {
            return "今天";
        } else if (current.before(today) && current.after(yesterday)) {
            return "昨天";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            String dateStr = format.format(date);
            return dateStr;
        }
    }

    /**
     * 从时间(毫秒)中提取出时间(时:分)
     * 时间格式:  时:分
     *
     * @param millisecond
     * @return
     */
    public static String getTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(millisecond);
        String timeStr = simpleDateFormat.format(date);
        return timeStr;
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 将时间转化成毫秒
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static Long timeStrToSecond(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long second = format.parse(time).getTime();
            return second;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }

    }

    /**
     * yyyy-MM-dd HH:mm:ss  -->  MM-dd HH:mm
     *
     * @return
     */
    public static String getFormatTime(String time) {

        if (TextUtils.isEmpty(time)) {
            return "";
        }

        Long millisecond = timeStrToSecond(time);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(millisecond);
        return format.format(date);

    }

    /**
     * yyyy-MM-dd HH:mm:ss  --> yyyy-MM-dd
     *
     * @return
     */
    public static String getFormatTimeYMD(String time) {

        if (TextUtils.isEmpty(time)) {
            return "";
        }

        Long millisecond = timeStrToSecond(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(millisecond);
        return format.format(date);

    }


    /**
     * yyyy-MM-dd HH:mm:ss  -->  MM-dd HH:mm
     *
     * @return
     */
    public static String getFormatTimeHM(String time) {

        if (TextUtils.isEmpty(time)) {
            return "";
        }

        Long millisecond = timeStrToSecond(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(millisecond);
        return format.format(date);

    }


    /**
     * 获取时间间隔
     *
     * @param millisecond
     * @return
     */
    public static String getSpaceTime(Long millisecond) {

//        Calendar calendar = Calendar.getInstance();
//        Long currentMillisecond = calendar.getTimeInMillis();

        long currentMillisecond = System.currentTimeMillis();

        //间隔秒
        long spaceSecond = (currentMillisecond - millisecond) / 1000;

        //一分钟之内
        if (spaceSecond < 60) {
            return "刚刚";
        }
        // 一小时之内
        else if (spaceSecond / 60 < 60) {
            return spaceSecond / 60 + "分钟前";
        }
        //一天之内
        else if (spaceSecond / (60 * 60) > 0 && spaceSecond / (60 * 60) < 24) {
            return spaceSecond / (60 * 60) + "小时前";
        }

        //30天之内
        else if (spaceSecond / (60 * 60 * 24) > 0 && spaceSecond / (60 * 60 * 24) < 30) {
            return spaceSecond / (60 * 60 * 24) + "天前";
        }

        // 一年之内
        else if (spaceSecond / (60 * 60 * 24 * 30) > 0 && spaceSecond / (60 * 60 * 24 * 30) < 12) {
            return spaceSecond / (60 * 60 * 24 * 30) + "个月前";
        }
        // 其他 显示发帖时间2017-7-1 15:04:33
        else {
            return getDateTimeFromMillisecond(millisecond);
        }
    }


    /**
     * 比较时间
     *
     * @param time
     * @param days
     * @return 超过days天 返回true
     */
    public static boolean comparativeTimeInterval(String time, int days) {

        long aLong = timeStrToSecond(time);

        long nowTime = System.currentTimeMillis();

        if ((nowTime - aLong) > (1000 * 60 * 60 * 24 * days)) {
            return true;
        }
        return false;
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少天)
     * e
     *
     * @param timeStamp
     * @return
     */
    public static String timeToDay(long timeStamp) {
        long curTime = System.currentTimeMillis();
        long time = timeStamp - curTime;
        return String.valueOf(time / (1000 * 60 * 60 * 24));
    }


    /**
     * 毫秒换成00:00:00
     *
     * @param finishTime
     * @return
     */
    public static String getCountTimeByLong(long finishTime) {

        int totalTime = (int) (finishTime / 1000);//秒
        int day = 0, hour = 0, minute = 0, second = 0;

        if (totalTime >= 86400) {
            day = totalTime / 86400;
            totalTime = totalTime - 86400 * day;
        }
        if (totalTime >= 3600) {
            hour = totalTime / 3600;
            totalTime = totalTime - 3600 * hour;
        }
        if (totalTime >= 60) {
            minute = totalTime / 60;
            totalTime = totalTime - 60 * minute;
        }
        if (totalTime >= 0) {
            second = totalTime;
        }
        StringBuilder sb = new StringBuilder();

        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (minute < 10) {
            sb.append("0").append(minute).append(":");
        } else {
            sb.append(minute).append(":");
        }
        if (second < 10) {
            sb.append("0").append(second);
        } else {
            sb.append(second);
        }
        return sb.toString();

    }


    /**
     * 秒数 乘以 1000
     *
     * @param longString
     * @return
     */
    public static long getSafeCountDownTime(String longString) {
        try {
            return Long.parseLong(longString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0L;
        }
    }


    /**
     * 判断是否是同一天
     *
     * @param millis1
     * @param millis2
     * @param timeZone
     * @return
     */
    public static boolean isSameDay(long millis1, long millis2, TimeZone timeZone) {
        long interval = millis1 - millis2;


        return interval < TimeConstants.DAY && interval > -TimeConstants.DAY && millis2Days(millis1, timeZone) == millis2Days(millis2, timeZone);
    }

    private static long millis2Days(long millis, TimeZone timeZone) {
        return (((long) timeZone.getOffset(millis)) + millis) / TimeConstants.DAY;
    }


    public static ArrayList<Long> getNext7DayTime() {
        ArrayList<Long> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            week.add(System.currentTimeMillis() + i * (TimeConstants.DAY));
        }
        return week;
    }


    /**
     * Method to extract the user's age from the entered Date of Birth.
     *
     * @return ageS String The user's age in years based on the supplied DoB.
     */
    public static String getAge(int year, int month, int day) {

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month - 1, day);
        today.setTimeInMillis(System.currentTimeMillis());

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
        int birthdayDayOfYear = dob.get(Calendar.DAY_OF_YEAR);

        if (todayDayOfYear < birthdayDayOfYear) {
            age--;
        }

//        Log.d("ageaaaa", "today " + todayDayOfYear + "   日期" + sdf.format(today.getTime()));
//        Log.d("ageaaaa", "dob " + birthdayDayOfYear + "   日期" + sdf.format(dob.getTime()));

        Integer ageInt = Integer.valueOf(age);

        return ageInt.toString();
    }

}
