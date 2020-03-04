package com.lyh.guanbei.ui.fragment;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyh.guanbei.BuildConfig;
import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseFragment;
import com.lyh.guanbei.bean.Apk;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.bean.Setting;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.jpush.PushMessageReceiver;
import com.lyh.guanbei.manager.CustomNotificationManager;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.ApkDownloadContract;
import com.lyh.guanbei.mvp.presenter.ApkDownloadPresenter;
import com.lyh.guanbei.ui.activity.IndexActivity;
import com.lyh.guanbei.ui.activity.LoginActivity;
import com.lyh.guanbei.ui.activity.MeEditActivity;
import com.lyh.guanbei.ui.activity.NoDisturbActivity;
import com.lyh.guanbei.ui.activity.NotificationListActivity;
import com.lyh.guanbei.ui.activity.PatternLockerActivity;
import com.lyh.guanbei.ui.activity.PwdActivity;
import com.lyh.guanbei.ui.activity.VerifyLockerActivity;
import com.lyh.guanbei.ui.widget.AskDialog;
import com.lyh.guanbei.util.ApkUtil;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import cn.jpush.android.api.JPushInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MePageFragment extends BaseFragment implements View.OnClickListener , ApkDownloadContract.IApkDownloadView,AskDialog.onClickListener {
    private ImageView mIcon;
    private TextView mName;
    private TextView mId;
    private View mNameView;
    private View mLoginView;
    private TextView mDays;
    private TextView mBooks;
    private TextView mRecords;

    private QMUIGroupListView mGroupListView;
    private QMUICommonListItemView mPatternLockerView;       //手势密码
    private QMUICommonListItemView mShortCutInput;              //通知栏快捷输入

    private Button mQuit;
    private User mUser;
    private Setting mSetting;

    private ApkDownloadPresenter mApkDownloadPresenter;
    private AskDialog mDialog;
    private int type;       //状态  0代表已下载，可直接安装      1代表需要下载
    private File mApkFile;
    private Apk mApk;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me_page;
    }

    @Override
    protected void initUi() {
        mIcon = findViewById(R.id.fragment_me_page_icon);
        mName = findViewById(R.id.fragment_me_page_name);
        mId = findViewById(R.id.fragment_me_page_id);
        mNameView = findViewById(R.id.fragment_me_page_nameview);
        mLoginView = findViewById(R.id.fragment_me_page_loginview);
        mDays = findViewById(R.id.fragment_me_page_days);
        mBooks = findViewById(R.id.fragment_me_page_books);
        mRecords = findViewById(R.id.fragment_me_page_records);
        mGroupListView = findViewById(R.id.fragment_me_page_grouplistview);
        mQuit = findViewById(R.id.fragment_me_page_quit);
        findViewById(R.id.fragment_me_page_me).setOnClickListener(this);
        findViewById(R.id.fragment_me_page_login_register).setOnClickListener(this);
        mQuit.setOnClickListener(this);
        initShadowLayout();
        initViewList();
    }

    private void initViewList() {
        QMUICommonListItemView recordExportView = mGroupListView.createItemView("账单导出");
        recordExportView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUICommonListItemView messageView = mGroupListView.createItemView("消息通知");
        messageView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        mPatternLockerView = mGroupListView.createItemView("手势密码");
        mPatternLockerView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);

        mShortCutInput = mGroupListView.createItemView("通知栏快捷入口");
        mShortCutInput.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        QMUICommonListItemView nodisturb = mGroupListView.createItemView("勿扰模式");
        nodisturb.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView settingView = mGroupListView.createItemView("设置");
        settingView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUICommonListItemView resetPwdView = mGroupListView.createItemView("修改密码");
        resetPwdView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUICommonListItemView helpView = mGroupListView.createItemView("帮助与反馈");
        helpView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUICommonListItemView aboutView = mGroupListView.createItemView("关于我们");
        aboutView.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        QMUICommonListItemView updateView = mGroupListView.createItemView("检查新版本");
        updateView.setTipPosition(QMUICommonListItemView.TIP_POSITION_RIGHT);
        mGroupListView.newSection(getmActivity())
                .addItemView(recordExportView, this)
                .addItemView(messageView, this)
                .addItemView(mPatternLockerView, this)
                .addItemView(mShortCutInput, this)
                .addItemView(nodisturb, this)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 20), 0)
                .addTo(mGroupListView);
        mGroupListView.newSection(getmActivity())
                .addItemView(settingView, this)
                .addItemView(resetPwdView, this)
                .addItemView(helpView, this)
                .addItemView(aboutView, this)
                .addItemView(updateView, this)
                .setMiddleSeparatorInset(QMUIDisplayHelper.dp2px(getContext(), 20), 0)
                .addTo(mGroupListView);
    }

    private void initShadowLayout() {
        QMUILinearLayout layout = getmView().findViewById(R.id.fragment_me_page_layout);
        int radius = QMUIDisplayHelper.dp2px(getContext(), 15);
        layout.setRadiusAndShadow(radius, QMUIDisplayHelper.dp2px(getmActivity(), 14), 0.25f);
        int margintop = QMUIDisplayHelper.getStatusBarHeight(getmActivity());
        int margin = QMUIDisplayHelper.dpToPx(20);
        setMargins(layout, margin, margintop + margin * 2, margin, margin);
    }

    @Override
    protected void init() {
    }

    private void initData() {
        mUser = CustomSharedPreferencesManager.getInstance().getUser();
        mSetting = mUser.getSetting();
        if (mUser == null || mUser.getUser_id() == -1) {
            //游客身份
            mLoginView.setVisibility(View.VISIBLE);
            mNameView.setVisibility(View.GONE);
            mQuit.setVisibility(View.GONE);
        } else {
            mLoginView.setVisibility(View.GONE);
            mNameView.setVisibility(View.VISIBLE);
            //设置各种数据
            Glide.with(getmActivity()).load(mUser.getUser_icon()).error(R.drawable.defaulticon).into(mIcon);
            mName.setText(mUser.getUser_name());
            mId.setText(mUser.getUser_id() + "");
        }
    }

    private void refreshData() {
        initData();
        mPatternLockerView.getSwitch().setChecked(mSetting.isLocked());
        mShortCutInput.getSwitch().setChecked(mSetting.getNotify_input());
        //更新天数、账本数、账单数
        List<Long> bookIdList = Util.getLongFromData(mUser.getLocal_book_id());
        int recordNum = Record.query(RecordDao.Properties.Book_local_id.in(bookIdList)).size();
        mDays.setText(DateUtil.differentDaysAndNowWithSecond(mUser.getCreate_time()) + "");
        mBooks.setText(bookIdList.size() + "");
        mRecords.setText(recordNum + "");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onClick(View v) {
        if (v instanceof QMUICommonListItemView) {
            QMUICommonListItemView view = (QMUICommonListItemView) v;
            switch (view.getText().toString()) {
                case "账单导出":

                    break;
                case "消息通知":
                    startActivity(NotificationListActivity.class);
                    break;
                case "通知栏快捷入口":
                    view.getSwitch().toggle();
                    if (view.getSwitch().isChecked()) {
                        mSetting.setNotify_input(true).save();
                        CustomNotificationManager.initInPutNotification(getmActivity());
                    } else {
                        mSetting.setNotify_input(false).save();
                        CustomNotificationManager.stopInPutNotification(getmActivity());
                    }
                    break;
                case "手势密码":
                    view.getSwitch().toggle();
                    if (view.getSwitch().isChecked())
                        startActivity(PatternLockerActivity.class);
                    else
                        startActivity(VerifyLockerActivity.class);
                    break;
                case "勿扰模式":
                    startActivity(NoDisturbActivity.class);
                    break;
                case "设置":
                    break;
                case "修改密码":
                    startActivity(PwdActivity.class);
                    break;
                case "帮助与反馈":
                    break;
                case "关于我们":
                    break;
                case "检查新版本":
//                    view.showRedDot(true);
                    mApkDownloadPresenter.getVersion();
                    break;
            }
            return;
        }
        switch (v.getId()) {
            case R.id.fragment_me_page_login_register:
                startActivity(LoginActivity.class);
                break;
            case R.id.fragment_me_page_quit:
                CustomSharedPreferencesManager.getInstance().saveUser(null);
                CustomSharedPreferencesManager.getInstance().clearAll();
                JPushInterface.deleteAlias(getmActivity(), PushMessageReceiver.USERALIAS);
                startActivity(IndexActivity.class);
                getmActivity().finish();
                break;
            case R.id.fragment_me_page_me:
                startActivity(MeEditActivity.class);
                break;
        }
    }

    @Override
    public void createPresenters() {
        mApkDownloadPresenter=new ApkDownloadPresenter();
        addPresenter(mApkDownloadPresenter);
    }

    @Override
    public void showLoading(String msg) {
    }

    @Override
    public void dissmisLoading() {
    }
    private void showDialog(String version){
        String content="";
        String title="最新版本为"+version;
        String ensure=type==0?"安装":"下载";
        mDialog=new AskDialog(getmActivity()).setContent(content).setTitle(title).setEnsure(ensure).setListener(this);
        mDialog.show();
    }
    @Override
    public void getVersionSuccess(Apk apk) {
        mApk=apk;
        //检查版本
        if(ApkUtil.checkIsNeedNewVersion(getmActivity(),apk.getVersion())){
            //需要新版本 弹出对话框
            //检查是否已下载
            mApkFile=ApkUtil.getApkFile(getmActivity(),apk.getVersion()+".apk");
            boolean isDone=ApkUtil.checkApkIsDownloadDone(mApkFile,apk.getSize());
            type=isDone?0:1;
            showDialog(apk.getVersion());
        }else{
            Toast.makeText(getmActivity(),"当前已是最新版本",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getVersionFailed(String msg) {

    }

    @Override
    public void downloadCallback(ResponseBody responseBody) {
        Toast.makeText(getmActivity(),"在后台进行下载",Toast.LENGTH_SHORT).show();
        mApkDownloadPresenter.downloadFromStream(responseBody.byteStream(),mApkFile);
    }

    @Override
    public void downloadFailed(String msg) {
        Toast.makeText(getmActivity(),"下载失败",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onEnsure() {
        if(type==0){
            Intent intent=ApkUtil.getInstallApkIntent(getmActivity(),mApkFile);
            getmActivity().startActivity(intent);
        }else{
            mApkDownloadPresenter.download(mApkFile,mApk.getPath());
        }
    }

    @Override
    public void dismiss() {

    }
}
