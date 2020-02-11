package com.lyh.guanbei.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//日期格式化
public class DateUtil {
    private static final String FIRST_DAY = "01";
    public static final String ZERO_TIME = "00:00";
    public static final String FILL_TIME = "24:00";

    //得到当前日期 格式：2019-10-29 18:47
    public static String getNowDateTimeWithoutSecond() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(new Date());
    }

    public static String getNowDateTimeWithSecond() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static String getMonthFirstDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(new Date()) + "-" + FIRST_DAY + " " + ZERO_TIME;
    }

    public static String getYearFirstDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(new Date()) + "-" + FIRST_DAY + "-" + FIRST_DAY + " " + ZERO_TIME;
    }

    public static String getMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return singleToDouble(month);
    }
    public static int getYear(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year;
    }
    public static String getDay(){
        Calendar cal = Calendar.getInstance();
        return singleToDouble(cal.get(Calendar.DAY_OF_MONTH));
    }
    //单位数转两位数
    public static String singleToDouble(int num) {
        if (num < 10)
            return "0" + num;
        return num + "";
    }

    public static String getDateFromLong(long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(date)).substring(0, 16);
    }

    //将15:00转成下午3:00格式
    public static String getDayTime(String time) {
        String[] strs = time.split(":");
        String h = strs[0];
        String m = strs[1];
        String head;
        int hInt = Integer.parseInt(h);
        int mInt = Integer.parseInt(m);
        if (h.compareTo("12") > 0) {
            hInt -= 12;
            head = "下午";
        } else {
            head = "上午";
        }
        return head + singleToDouble(hInt) + ":" + singleToDouble(mInt);
    }

    //得到两个日期之间的天数
    public static int differentDaysAndNowWithSecond(String date1) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date last = sdf.parse(date1);
            return differentDays(last, now);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDateBeforeDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days); //得到前一天
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String format = df.format(date);
        return format;
    }
    public static String getDateAfterDays(String start,int days){
        try {
            Calendar calendar=Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate=sdf.parse(start);
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE,days);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    //得到两个日期之间的天数
    public static int differentDaysWithoutTime(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = sdf.parse(date1);
            Date end = sdf.parse(date2);
            return differentDays(start, end);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
    //得到两个日期之间的天数
    public static int differentDaysWithoutSecond(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date start = sdf.parse(date1);
            Date end=sdf.parse(date2);
            return differentDays(start,end);
        }catch (ParseException e){
            e.printStackTrace();
            return 0;
        }
    }
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //不同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //同一年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }
    public static String getDayByDate(String date){
        return date.split(" ")[0];
    }
    //得到一个月的日期区间
    public static void getDateRangeOfMonth(int year,int month,String[] resDate){

        resDate[0]=year+"-"+singleToDouble(month)+"-01";
        resDate[1]=year+"-"+singleToDouble(month)+"-"+singleToDouble(getDaysOfMonth(year,month));
    }
    public static int getDaysOfMonth(int year,int month){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month-1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    //得到一个月有多少周
    public static int getWeeksOfMonth(int year,int month){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month-1);
        return calendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }
    //得到一个月的头和尾属于一年中的第几周
    public static void getWeeksRangeOfMonth(int year,int month,int[] weeksRange){
        String[] resDate=new String[2];
        getDateRangeOfMonth(year,month,resDate);
        weeksRange[0]=getWeeksByDate(resDate[0]);
        weeksRange[1]=getWeeksByDate(resDate[1]);
    }
    //得到该天在一年中第几周
    public static int getWeeksByDate(String dateStr){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date=sdf.parse(dateStr);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.WEEK_OF_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
    public static int getWeeksByNowDate(){
        Calendar calendar=Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
    //得到某个星期的头和尾区间
    public static void getDatesRangeByWeek(int year,int weeks,String[] resRanges){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.WEEK_OF_YEAR,weeks);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        resRanges[0]=sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE,6);
        resRanges[1]=sdf.format(calendar.getTime());
    }
    public static void getDatesRangeByYear(int year,String[] resRanges){
        resRanges[0]=year+"-"+FIRST_DAY+"-"+FIRST_DAY;
        resRanges[1]=year+"-12-31";
    }
}
