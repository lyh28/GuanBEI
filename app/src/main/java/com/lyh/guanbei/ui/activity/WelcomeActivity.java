package com.lyh.guanbei.ui.activity;

import android.os.Handler;
import android.os.Message;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.QueryBookContract;
import com.lyh.guanbei.mvp.presenter.QueryBookPresenter;
import com.lyh.guanbei.util.Util;

import java.util.List;

public class WelcomeActivity extends BaseActivity implements QueryBookContract.IQueryBookView {
    private static final int LOGIN_CODE = 1;
    private static final int MAIN_CODE = 2;
    private int status=0;
    private final int FINISH_STATUS=2;
    private QueryBookPresenter mQueryBookPresenter;
    private long currId;        //服务端ID
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MAIN_CODE) {
                if(++status==FINISH_STATUS) {
                    startActivity(MainActivity.class);
                    finish();
                }
            } else if (msg.what == LOGIN_CODE) {
                startActivity(IndexActivity.class);
                finish();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initUi() {
    }

    @Override
    protected void init() {
        //得到登录账户
        User user=CustomSharedPreferencesManager.getInstance().getUser();
        long localId=CustomSharedPreferencesManager.getInstance().getCurrBookId();
        Book book=Book.queryByLocalId(localId);
        if(book==null)
            currId=-1;
        else
            currId=book.getBook_id();
        if ( user!= null) {
            //更新book数据
            mQueryBookPresenter.queryBookService(Util.getLongFromData(user.getBook_id()));
            mHandler.sendEmptyMessageDelayed(MAIN_CODE, 500);
        } else {
            mHandler.sendEmptyMessageDelayed(LOGIN_CODE, 500);
        }
    }

    @Override
    public void queryBookFailed() {
        if(++status==FINISH_STATUS) {
            startActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    public void queryBookSuccess(List<Book> list) {
        User.updateBookLocalIdToUser(list);
        Book book=null;
        if(currId!=-1)  book=Book.queryByBookId(currId);
        else if(list.size()!=0)   book=list.get(0);
        if(book!=null)  CustomSharedPreferencesManager.getInstance().saveCurrBookId(book.getLocal_id());
        else
            CustomSharedPreferencesManager.getInstance().saveCurrBookId(-1);
        if(++status==FINISH_STATUS) {
            startActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    public void createPresenters() {
        mQueryBookPresenter=new QueryBookPresenter();
        addPresenter(mQueryBookPresenter);
    }
    @Override
    public boolean isLocked() {
        return false;
    }
}
