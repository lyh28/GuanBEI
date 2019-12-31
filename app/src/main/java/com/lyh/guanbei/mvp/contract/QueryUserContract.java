package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.User;

import java.util.List;

public interface QueryUserContract {
    interface IQueryUserView extends IView {
        void onQueryUserSuccess(User user);
        void onQueryUserSuccess(List<User> userList);
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
        void queryLocal(List<Long> idList, ICallbackListener<List<User>> iCallbackListener);
        void queryServer(List<Long> idList,ICallbackListener<List<User>> iCallbackListener);
        void queryLocal(String phone, ICallbackListener<User> iCallbackListener);
        void queryServer(String phone,ICallbackListener<User> iCallbackListener);

        void saveData(User user);
        void saveData(List<User> userList);
    }
}
