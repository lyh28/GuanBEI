package com.lyh.guanbei.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.lyh.guanbei.common.Contact;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import androidx.core.content.FileProvider;
import okhttp3.ResponseBody;

public class Util {
    //从"A-B-C"格式中删除其中一个数据
    public static String deleteFormData(long data, String str) {
        String[] arr = str.split(Contact.SEPARATOR);
        String res = "";
        String datas = data + "";
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(datas)) {
                continue;
            }
            if (res.equals("")) {
                res += arr[i];
            } else {
                res = res + Contact.SEPARATOR + arr[i];
            }
        }
        return res;
    }

    //添加至"A-B-C"格式中
    public static String addToData(long data, String str) {
        str = str == null ? "" : str;
        if (str.equals("")) {
            str += data;
        } else {
            str = str + Contact.SEPARATOR + data;
        }
        return str;
    }

    public static String addToData(int data, String str) {
        if (str.equals("")) {
            str += data;
        } else {
            str = str + Contact.SEPARATOR + data;
        }
        return str;
    }

    //得到数量
    public static int getCountFormData(String data) {
        if (TextUtils.isEmpty(data))
            return 0;
        return data.split(Contact.SEPARATOR).length;
    }

    public static String[] splitData(String data) {
        return data.split(Contact.SEPARATOR);
    }

    public static String getDataFromList(List<Integer> list) {
        String data = "";
        for (int i : list)
            data = addToData(i, data);
        return data;
    }

    public static List<Long> getLongFromData(String data) {
        List<Long> list = new ArrayList<>();
        if (TextUtils.isEmpty(data))
            return list;
        String[] arr = splitData(data);
        if (arr == null || arr.length == 0)
            return list;
        for (String b : arr)
            list.add(Long.parseLong(b));
        return list;
    }

    //计算加法减法        最后一个数可能为+-符号
    public static int calculate(String amount) {
        int res = 0;
        //操作数队列
        LinkedList<Integer> numList = new LinkedList<>();
        //符号栈
        LinkedList<Character> methodList = new LinkedList<>();
        int left = 0;
        for (int i = 0; i < amount.length(); i++) {
            char c = amount.charAt(i);
            if (c == '-' || c == '+') {
                if (i == 0) {
                    left++;
                    continue;
                }
                methodList.addLast(c);
                numList.addLast(Integer.parseInt(amount.substring(left, i)));
                left = i + 1;
            }
        }
        if (left < amount.length())
            numList.addLast(Integer.parseInt(amount.substring(left)));

        res = numList.pollFirst();
        if (amount.charAt(0) == '-') res = 0 - res;
        while (numList.size() != 0) {
            res = calculate(res, numList.pollFirst(), methodList.pollFirst());
        }
        return res;
    }

    private static int calculate(int num1, int num2, char c) {
        if (c == '+')
            return num1 + num2;
        else
            return num1 - num2;
    }


}
