package com.lyh.guanbei.bean;

public class CheckCode {
    private String phone;
    private int checkCode;      //检验码
    private String date;

    public CheckCode(String phone, int checkCode, String date) {
        this.phone = phone;
        this.checkCode = checkCode;
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(int checkCode) {
        this.checkCode = checkCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
