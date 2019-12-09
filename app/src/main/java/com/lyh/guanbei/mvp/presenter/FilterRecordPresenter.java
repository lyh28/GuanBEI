package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.RecordBeanDao;
import com.lyh.guanbei.mvp.contract.FilterRecordContract;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.Util;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterRecordPresenter extends BasePresenter<FilterRecordContract.IFilterRecordView, IModel> implements FilterRecordContract.IFilterRecordPresenter {
    private Map<String, WhereCondition> mFilterMap;
    private WhereCondition mDateCondition;
    public FilterRecordPresenter(){
        mFilterMap=new HashMap<>();
    }
    public void setmDateCondition(String start,String end){
        mDateCondition=RecordBeanDao.Properties.Time.between(start,end);
    }
    public void clearContidion(String type){
        mFilterMap.remove(type);
    }
    @Override
    public void addContidion(String contidionType, Object condition) {
        WhereCondition whereCondition=FilterRecord.createWhereCondition(contidionType,condition);
        mFilterMap.put(contidionType,whereCondition);
    }

    @Override
    public void filterRecord() {
        List<RecordBean> recordList;
        if(mFilterMap.size()==0){
            recordList=RecordBean.query(mDateCondition);
        }else {
            WhereCondition[] arr = mFilterMap.values().toArray(new WhereCondition[mFilterMap.size()]);
            recordList=RecordBean.query(mDateCondition,arr);
        }
        getmView().filterRecordShow(recordList);
    }

    @Override
    public IModel createModel() {
        return null;
    }
    public static class FilterRecord{
        public static final String CATEGORY_FILTER="Category";
        public static final String PAYTO_FILTER="PayTo";
        public static WhereCondition createWhereCondition(String type,Object condition){
            WhereCondition whereCondition=null;
            switch (type){
                case CATEGORY_FILTER:
                    whereCondition= RecordBeanDao.Properties.Category.eq(condition);
                    break;
                case PAYTO_FILTER:
                    whereCondition=RecordBeanDao.Properties.Payto.eq(condition);
                    break;
                    default:
                        break;
            }
            return whereCondition;
        }
    }
}
