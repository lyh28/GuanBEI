package com.lyh.guanbei.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//日期格式化
public class DateUtil {
    private static final String FIRST_DAY="-01 00:00";
    //得到当前日期 格式：2019-10-29 18:47
    public static String getNowDateTimeWithoutSecond(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(new Date());
    }
    public static String getNowDateTimeWithSecond(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }
    public static String getMonthFirstDay(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(new Date())+FIRST_DAY;
    }
    public static String getMonth(){
        Calendar cal= Calendar.getInstance();
        int month=cal.get(Calendar.MONTH)+1;
        if(month<10)
            return "0"+month+"月";
        return month+"月";
    }
    public static String getDateFromLong(long date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(date)).substring(0,16);
    }
}
