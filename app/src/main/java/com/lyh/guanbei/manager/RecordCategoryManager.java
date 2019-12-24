package com.lyh.guanbei.manager;

import android.content.Context;

import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.CategoryBean;
import com.lyh.guanbei.common.GuanBeiApplication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
    管理分类的icon和字段
*/
public class RecordCategoryManager {
    private static final String EAT = "吃喝";
    private static final String TRAFFIC = "交通";
    private static final String GAME = "娱乐";
    private static final String DAILY = "日常开销";
    private static final String HOSPITAL = "医疗";
    private static final String REDPACKAGE = "红包";
    private static final String CLOTHES = "服饰鞋包";

    //支出
    private List<CategoryBean> outList;
    //收入
    private List<CategoryBean> inList;

    public List<CategoryBean> getInList() {
        if (inList == null) {
//            return GuanBeiApplication.getDaoSession().get
            //从数据库中读取
        }
        return inList;
    }

    public List<CategoryBean> getOutList() {
        if (outList == null) {
            //从数据库中读取
        }
        return outList;
    }

    public static CategoryBean getDefaultCategory(int type) {
        //暂定使用该图标
        return new CategoryBean(EAT, R.mipmap.icon_more_operation_share_friend, type);
    }

    //返回预置的收入List
    public static List<CategoryBean> getPresetInList() {
        LinkedList<CategoryBean> list = new LinkedList<>();
        list.add(new CategoryBean(EAT, R.mipmap.icon_more_operation_share_friend, CategoryBean.IN));
        list.add(new CategoryBean(TRAFFIC, R.mipmap.icon_more_operation_share_friend, CategoryBean.IN));
        list.add(new CategoryBean(GAME, R.mipmap.icon_more_operation_share_friend, CategoryBean.IN));
        list.add(new CategoryBean(DAILY, R.mipmap.icon_more_operation_share_friend, CategoryBean.IN));
        list.add(new CategoryBean(HOSPITAL, R.mipmap.icon_more_operation_share_friend, CategoryBean.IN));
        list.add(new CategoryBean(REDPACKAGE, R.mipmap.icon_more_operation_share_friend, CategoryBean.IN));
        list.add(new CategoryBean(CLOTHES, R.mipmap.icon_more_operation_share_friend, CategoryBean.IN));
        return list;
    }

    //返回预置的收入List
    public static List<CategoryBean> getPresetOutList() {
        LinkedList<CategoryBean> list = new LinkedList<>();
        list.add(new CategoryBean(EAT, R.mipmap.icon_more_operation_share_friend, CategoryBean.OUT));
        list.add(new CategoryBean(TRAFFIC, R.mipmap.icon_more_operation_share_friend, CategoryBean.OUT));
        list.add(new CategoryBean(GAME, R.mipmap.icon_more_operation_share_friend, CategoryBean.OUT));
        list.add(new CategoryBean(DAILY, R.mipmap.icon_more_operation_share_friend, CategoryBean.OUT));
        return list;
    }

    public static int getIconByCategory(String name, int type) {
        CategoryBean categoryBean = CategoryBean.getCategoryByName(name, type);
        if (categoryBean == null)
            return R.mipmap.icon_more_operation_share_friend;
        else
            return categoryBean.getIconId();
    }
}
