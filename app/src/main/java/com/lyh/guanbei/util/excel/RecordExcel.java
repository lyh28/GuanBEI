package com.lyh.guanbei.util.excel;

import android.util.Log;

import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RecordExcel {
    private static final String TAG = "Module1_MainActivity";

    //默认值
    private final String TIMEDEFAULT="交易时间";
    private final String TOWHODEFAULT="交易对方";
    private final String PAYFORDEFAULT="商品";
    private final String TYPEDEFAULT="收/支";
    private final String MONEYDEFAULT="金额(元)";
    private final String REMARKDEFAULT="备注";

    private final String TIME;
    private final String TOWHO;
    private final String PAYFOR;
    private final String TYPE;
    private final String MONEY;        //￥$
    private final String REMARK;
    //默认索引
    private int timeIndex;
    private int toWhoIndex;
    private int payForIndex;
    private int typeIndex;
    private int moneyIndex;
    private int remarkIndex;
    private RecordExcelHeadFilter headFilter;
    public RecordExcel(){
        TIME=TIMEDEFAULT;
        TOWHO=TOWHODEFAULT;
        PAYFOR=PAYFORDEFAULT;
        TYPE=TYPEDEFAULT;
        MONEY=MONEYDEFAULT;
        REMARK=REMARKDEFAULT;
        initIndex();
        headFilter=new WXRecordExcelHeaderFilter();
    }

    public RecordExcel(String TIME, String TOWHO, String PAYFOR, String TYPE, String MONEY, String REMARK) {
        this.TIME = TIME;
        this.TOWHO = TOWHO;
        this.PAYFOR = PAYFOR;
        this.TYPE = TYPE;
        this.MONEY = MONEY;
        this.REMARK = REMARK;
        this.headFilter=new WXRecordExcelHeaderFilter();
        initIndex();
    }
    public List<Record> getRecordBean(File excel){
        try {
            return getRecordBean(new FileInputStream(excel));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<Record> getRecordBean(InputStream excel){
        List<Record> res=new ArrayList<>();
        BufferedReader bufferedReader=null;
        try {
            bufferedReader=new BufferedReader(new InputStreamReader(excel));
            String str;
            if(headFilter!=null){
                while((str=bufferedReader.readLine())!=null){
                    if(headFilter.isToHead(str))
                        break;
                }
            }
            str=bufferedReader.readLine();
            updateIndexs(str);
            Log.d(TAG, "getRecordBean: "+toWhoIndex+"  "+payForIndex+"  "+moneyIndex);
            //得到表头各标签索引
            while((str=bufferedReader.readLine())!=null&&!str.equals("")){
                res.add(getRecordFormLine(str));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufferedReader!=null)
                    bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
    private Record getRecordFormLine(String line){
        String[] value=line.split(",");
        String time=timeIndex==-1||timeIndex>=value.length?"":value[timeIndex];
        String toWho=toWhoIndex==-1||toWhoIndex>=value.length?"":value[toWhoIndex];
        String payFor=payForIndex==-1||payForIndex>=value.length?"":value[payForIndex];
        String money=moneyIndex==-1||moneyIndex>=value.length?"":value[moneyIndex];
        String type=typeIndex==-1||typeIndex>=value.length?"":value[typeIndex];
        String remark=remarkIndex==-1||remarkIndex>=value.length?"":value[remarkIndex];
        int amount_type;
        if(type.equals("收入")||type.equals("/"))  amount_type= Tag.IN;
            else if(type.equals("支出"))  amount_type=Tag.OUT;
//        return new Record(time,money,toWho,payFor,remark);
        return null;
    }
    private void initIndex(){
        timeIndex=-1;
        toWhoIndex=-1;
        payForIndex=-1;
        typeIndex=-1;
        moneyIndex=-1;
    }
    public void updateIndexs(String titles){
        String[] title=titles.split(",");
        for(int i=0;i<title.length;i++){
            updateIndex(title[i],i);
        }
    }
    public void updateIndex(String title,int index){
        if(TIME.equals(title)){
            timeIndex=index;
        }else if(TOWHO.equals(title)){
            toWhoIndex=index;
        }else if(PAYFOR.equals(title)){
            payForIndex=index;
        }else if(TYPE.equals(title)){
            typeIndex=index;
        }else if(MONEY.equals(title)){
            moneyIndex=index;
        }else if(REMARK.equals(title)){
            remarkIndex=index;
        }
    }

    public void setHeadFilter(RecordExcelHeadFilter headFilter) {
        this.headFilter = headFilter;
    }
}