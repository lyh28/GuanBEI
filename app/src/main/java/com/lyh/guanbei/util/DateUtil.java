package com.lyh.guanbei.util;

import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.db.RecordBeanDao;

import java.text.SimpleDateFormat;
import java.util.Date;

//日期格式化
public class DateUtil {
    private static final String FIRST_DAY="-01 00:00:00";
    //得到当前日期 格式：2019-10-29 18:47:00
    public static String getNowDateTime(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(new Date());
    }
    public static String getMonthFirstDay(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(new Date())+FIRST_DAY;
    }
    public static String getDateFromLong(long date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(date));
    }
}
