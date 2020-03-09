package com.lyh.guanbei.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.SparseArray;

import com.lyh.guanbei.bean.SMS;

import java.util.ArrayList;
import java.util.List;

public class SMSUtil {
    private static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");

    public static List<SMS> getSmsFromPhone(Context context) {
        //查询联系人员
//        SparseArray<String> friendMap=new SparseArray<>();
//        getNameFromPhone(context,friendMap);
        List<SMS> list=new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[] {"_id", "address", "person","body", "date", "type","subject" };
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            return list;
        }
        while(cur.moveToNext()) {
            String name = cur.getString(cur.getColumnIndex("address"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            long date=cur.getLong(cur.getColumnIndex("date"));
//            String name=getName(nameId,friendMap);
//            if(name.equals("陌生人"))
//                continue;
            SMS sms=new SMS(DateUtil.getDateFromLong(date),name,body);
            LogUtil.logD("sms  "+sms);
            list.add(sms);
        }
        cur.close();
        return list;
    }
    private static String getName(int id,SparseArray<String> friendMap) {
        if(friendMap.get(id)!=null) {
            return friendMap.get(id);
        } else {
            return "陌生人";
        }
    }

    private static void getNameFromPhone(Context context,SparseArray<String> friendMap) {
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri,new String[] {  ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},null,null,null);
        while (cursor.moveToNext()) {
            int id=cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String num=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            friendMap.put(id,name);
        }
        cursor.close();
    }
}
