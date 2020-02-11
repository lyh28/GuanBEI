package com.lyh.guanbei.util.excel;

import android.text.TextUtils;
import android.util.Log;

import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Model;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Tag;
import com.lyh.guanbei.util.LogUtil;

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
    private final String TIMEDEFAULT = "交易时间";
    private final String TOWHODEFAULT = "交易对方";
    private final String PAYFORDEFAULT = "商品";
    private final String TYPEDEFAULT = "收/支";
    private final String MONEYDEFAULT = "金额(元)";
    private final String REMARKDEFAULT = "备注";

    private String TIME;
    private String TOWHO;
    private String PAYFOR;
    private String TYPE;
    private String MONEY;        //￥$
    private String REMARK;
    private String CATEGORY;
    private long bookId;
    private long bookLocalId;
    private long userId;
    //默认索引
    private int timeIndex;
    private int toWhoIndex;
    private int payForIndex;
    private int typeIndex;
    private int moneyIndex;
    private int remarkIndex;

    private boolean isWeChat;       //是否微信模板
    private RecordExcelHeadFilter headFilter;

    public RecordExcel(Model model) {
        bookId = -1;
        userId = -1;
        bookLocalId = -1;
        isWeChat = model.getName().equals("微信模板");
        PAYFOR = PAYFORDEFAULT;
        this.TIME = model.getDate();
        this.TOWHO = model.getToWho();
        this.TYPE = model.getAmount_Type();
        this.MONEY = model.getAmount();
        this.REMARK = model.getRemark();
        this.CATEGORY = model.getName();
        if (isWeChat)
            this.headFilter = new WXRecordExcelHeaderFilter();
        initIndex();
    }

    public List<Record> getRecordBean(File excel) {
        try {
            return getRecordBean(new FileInputStream(excel));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Record> getRecordBean(String path) {
        return getRecordBean(new File(path));
    }

    public List<Record> getRecordBean(InputStream excel) throws Exception {
        if (bookId == -1 || userId == -1 || bookLocalId == -1)
            throw new Exception("需要提供bookId或userId");
        List<Record> res = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(excel));
            String str;
            if (headFilter != null) {
                while ((str = bufferedReader.readLine()) != null) {
                    if (headFilter.isToHead(str))
                        break;
                }
            }
            //去掉头部后
            str = bufferedReader.readLine();
            updateIndexs(str);
            //得到表头各标签索引
            while ((str = bufferedReader.readLine()) != null && !str.equals("")) {
                res.add(getRecordFormLine(str));
                LogUtil.logD(res.get(res.size() - 1).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private Record getRecordFormLine(String line) {
        String[] value = line.split(",");
        String time = timeIndex == -1 || timeIndex >= value.length ? "" : value[timeIndex];
        String toWho = toWhoIndex == -1 || toWhoIndex >= value.length ? "" : value[toWhoIndex];
        String payFor = payForIndex == -1 || payForIndex >= value.length ? "" : value[payForIndex];
        String money = moneyIndex == -1 || moneyIndex >= value.length ? "" : value[moneyIndex];
        String type = typeIndex == -1 || typeIndex >= value.length ? "" : value[typeIndex];
        String remark = remarkIndex == -1 || remarkIndex >= value.length ? "" : value[remarkIndex];
        LogUtil.logD("towho  " + toWho);
        LogUtil.logD("payfor  " + payFor);

        int amount_type = Tag.IN;
        if (money.startsWith("¥") || money.startsWith("$")) {
            money = money.substring(1);
        }
        if (type.equals("收入") || type.equals("/")) amount_type = Tag.IN;
        else if (type.equals("支出")) {
            amount_type = Tag.OUT;
            if (!money.startsWith("-"))
                money = "-" + money;
        }

        if (!money.equals("")) {
            if (money.compareTo("0") < 0) {
                amount_type = Tag.OUT;
                if (money.startsWith("-"))
                    money = money.substring(1);
            } else {
                amount_type = Tag.IN;
            }
        }
        if (isWeChat) {
            if (!TextUtils.isEmpty(payFor) && !("/").equals(payFor))
                remark = payFor + "-" + remark;
        }
        return new Record(userId, bookId, bookLocalId, time, money, amount_type, toWho, remark, CATEGORY);
    }

    public RecordExcel setBookLocalId(long bookLocalId) {
        this.bookLocalId = bookLocalId;
        this.bookId = Book.queryByLocalId(bookLocalId).getBook_id();
        return this;
    }

    public RecordExcel setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    private void initIndex() {
        timeIndex = -1;
        toWhoIndex = -1;
        payForIndex = -1;
        typeIndex = -1;
        moneyIndex = -1;
    }

    public void updateIndexs(String titles) {
        String[] title = titles.split(",");
        for (int i = 0; i < title.length; i++) {
            updateIndex(title[i], i);
        }
    }

    public void updateIndex(String title, int index) {
        if (TIME.equals(title)) {
            timeIndex = index;
        } else if (TOWHO.equals(title)) {
            toWhoIndex = index;
        } else if (TYPE.equals(title)) {
            typeIndex = index;
        } else if (MONEY.equals(title)) {
            moneyIndex = index;
        } else if (REMARK.equals(title)) {
            remarkIndex = index;
        } else if (PAYFOR.equals(title)) {
            payForIndex = index;
        }
    }

    public RecordExcel setHeadFilter(RecordExcelHeadFilter headFilter) {
        this.headFilter = headFilter;
        return this;
    }
}