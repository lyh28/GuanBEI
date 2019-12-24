package com.lyh.guanbei.bean;

public class SMSBean {
    private String date;
    private String name;
    private String content;
    private boolean isChoose;
    public SMSBean(String date, String name, String content) {
        this.date = date;
        this.name = name;
        this.content = content;
        isChoose=false;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SMSBean{" +
                "date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", isChoose=" + isChoose +
                '}';
    }
}
