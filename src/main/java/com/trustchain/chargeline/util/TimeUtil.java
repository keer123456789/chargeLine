package com.trustchain.chargeline.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {


    /**
     * 判断时间是否为昨天，当前时间算
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean getCurrentTime(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.set(Calendar.HOUR, beforeTime.get(Calendar.HOUR) - 24);// 24小时之前的时间
        Date beforeD = beforeTime.getTime();
        Date date1 = sdf.parse(date);
        if (beforeD.before(date1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回周几
     *
     * @return
     */
    public static int getLastTimeInterval(String date) throws ParseException {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // System.out.println(sdf.format(calendar1.getTime()));// last Monday
        Date lastBeginDate = calendar1.getTime();
        // System.out.println(sdf.format(calendar2.getTime()));// last Sunday
        Date lastEndDate = calendar2.getTime();
        Date date1 = sdf.parse(date);

        int dayForWeek = 0;
        if (lastBeginDate.before(date1) && lastEndDate.after(date1)) {
            Calendar c = Calendar.getInstance();
            c.setTime(date1);

            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            return dayForWeek;
        } else {
            return 0;
        }
    }

    /**
     * 得到月份
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得年份
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getYear(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 比较两个时间先后
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static boolean compaireTime(String date1,String date2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = sdf.parse(date1);
        Date d2 = sdf.parse(date2);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return cal1.before(cal2);
    }

    public static void main(String[] args) throws ParseException {
        System.out.print(compaireTime("2019-02-28 11:10:11","2019-02-27 10:10:10"));
    }
}
