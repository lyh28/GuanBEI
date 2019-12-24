package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.UserBean;

import java.util.List;

public interface QueryUserContract {
    interface IQueryUserView extends IView {
        void onQueryUserSuccess(UserBean user);
        void onQueryUserSuccess(List<UserBean> userList);
        void onQueryUserError(String msg);
    }
    interface IQueryUserPresenter extends IPresenter<IQueryUserView,IQueryUserModel>{
        void query(long id);
        void queryServer(long id);
        void query(List<Long>id);
        void queryServer(List<Long> id);
        void query(String phone);
        void queryServer(String phone);
    }
    interface IQueryUserModel extends IModel{
        void queryLocal(List<Long> idList, ICallbackListener<List<UserBean>> iCallbackListener);
        void queryServer(List<Long> idList,ICallbackListener<List<UserBean>> iCallbackListener);
        void queryLocal(String phone, ICallbackListener<UserBean> iCallbackListener);
        void queryServer(String phone,ICallbackListener<UserBean> iCallbackListener);

        void saveData(UserBean user);
        void saveData(List<UserBean> userList);
    }
}
