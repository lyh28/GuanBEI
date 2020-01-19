package com.lyh.guanbei.ui.activity;

import android.os.Handler;
import android.os.Message;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.BookManager;
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
        if ( user!= null) {
            //更新book数据
            mQueryBookPresenter.queryBookServer(Util.getLongFromData(user.getBook_id()));
            mHandler.sendEmptyMessageDelayed(MAIN_CODE, 1500);
        } else {
            mHandler.sendEmptyMessageDelayed(LOGIN_CODE, 1500);
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
    public void showBook(List<Book> list) {
        BookManager.getInstance().init();
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
}
