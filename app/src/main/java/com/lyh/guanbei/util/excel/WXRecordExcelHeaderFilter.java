package com.lyh.guanbei.util.excel;

public class WXRecordExcelHeaderFilter extends RecordExcelHeadFilter {
    @Override
    public boolean isToHead(String str) {
        if(str.contains("微信支付账单明细列表"))
            return true;
        else return false;
    }
}
