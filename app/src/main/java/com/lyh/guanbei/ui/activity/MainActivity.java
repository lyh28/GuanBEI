package com.lyh.guanbei.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.Repository.BookRepository;
import com.lyh.guanbei.adapter.HomePageAdapter;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.common.NetRestartService;
import com.lyh.guanbei.manager.CustomNotificationManager;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.Repository.RecordRepository;
import com.lyh.guanbei.mvp.contract.NetListenerContract;
import com.lyh.guanbei.mvp.presenter.NetListenerPresenter;
import com.lyh.guanbei.ui.fragment.BookPageFragment;
import com.lyh.guanbei.ui.fragment.ChartPageFragment;
import com.lyh.guanbei.ui.fragment.MePageFragment;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements View.OnClickListener, NetListenerContract.INetListenerView, EasyPermissions.PermissionCallbacks {
    private NetListenerPresenter mNetListenerPresenter;


    private RadioGroup radioGroup;
    private ImageView mAddImg;
    private RadioButton mBookBtn;
    private RadioButton mChartBtn;
    private RadioButton mMeBtn;
    private ViewPager mViewPager;

    private List<Fragment> mFragments;
    private static final int SMS_PERMISSION = 100;
    private static final int STORAGE_PERMISSION = 101;

    private boolean isOpenFirst;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUi() {
        radioGroup = findViewById(R.id.activity_main_radiogroup);
        mAddImg = findViewById(R.id.activity_main_add);
        mViewPager = findViewById(R.id.activity_main_viewpager);
        mBookBtn = findViewById(R.id.activity_main_book);
        mChartBtn = findViewById(R.id.activity_main_chart);
        mMeBtn = findViewById(R.id.activity_main_me);
        mAddImg.setOnClickListener(this);
    }

    @Override
    protected void init() {
        isOpenFirst=true;
        mNetListenerPresenter.startNetListener();
        mFragments = new ArrayList<>(3);
        mFragments.add(new ChartPageFragment());
        mFragments.add(new BookPageFragment());
        mFragments.add(new MePageFragment());
        mViewPager.setAdapter(new HomePageAdapter(getSupportFragmentManager(), mFragments));
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        closeBookPage();
                        mChartBtn.setChecked(true);
                        break;
                    case 1:
                        showBookPage();
                        mBookBtn.setChecked(true);
                        break;
                    case 2:
                        closeBookPage();
                        mMeBtn.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(1, false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = -1;
                switch (checkedId) {
                    case R.id.activity_main_chart:
                        index = 0;
                        break;
                    case R.id.activity_main_book:
                        index = 1;
                        break;
                    case R.id.activity_main_me:
                        index = 2;
                        break;
                }
                mViewPager.setCurrentItem(index, true);
            }
        });
        //启动前台服务
        if (CustomSharedPreferencesManager.getInstance().getUser().getSetting().getNotify_input())
            CustomNotificationManager.initInPutNotification(this);
        //更新上次使用时间
        User.updateLastTime();
        BookRepository.getSingleton().init();
        RecordRepository.getSingleton().init();
    }

    private void showBookPage() {
        mAddImg.setVisibility(View.VISIBLE);
        mBookBtn.setVisibility(View.INVISIBLE);
    }

    private void closeBookPage() {
        mAddImg.setVisibility(View.INVISIBLE);
        mBookBtn.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        mNetListenerPresenter.closeNetListener();
        super.onDestroy();
    }

    @Override
    public void createPresenters() {
        mNetListenerPresenter = new NetListenerPresenter();
        addPresenter(mNetListenerPresenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_add:
                showAddButton();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isOpenFirst&&CustomSharedPreferencesManager.getInstance().getUser().getSetting().isLocked()&&isLocked()){
            //手势密码解锁页
            startActivity(UnLockActivity.class);
        }
        if(isOpenFirst){
            isOpenFirst=false;
        }
    }

    @Override
    public void onNetAvailable() {
        LogUtil.logD("有网了");
        startService(new Intent(this, NetRestartService.class));
    }

    @Override
    public void onNetUnavailable() {
        LogUtil.logD("断网了");
    }

    private void showAddButton() {
        /*
            添加方式：1.手工输入 2.读取短信  3.excel导入  4.图片识别
         */
        final int TAG_FROM_MYSELF = 0;
        final int TAG_FROM_MESSAGE = 1;
        final int TAG_FROM_EXCEL = 2;
        final int TAG_FROM_PIC = 3;

        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(this);
        builder.addItem(R.drawable.addmyself, "手工输入", TAG_FROM_MYSELF, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.msg, "读取短信", TAG_FROM_MESSAGE, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.drawable.excel, "表格导入", TAG_FROM_EXCEL, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
//                .addItem(R.mipmap.icon_more_operation_share_friend, "图片识别", TAG_FROM_PIC, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        Intent intent = null;
                        switch (tag) {
                            case TAG_FROM_MYSELF:
                                intent = new Intent(MainActivity.this, AddByMyselfActivity.class);
                                break;
                            case TAG_FROM_MESSAGE:
                                checkSMSPermission();
                                break;
                            case TAG_FROM_EXCEL:
                                checkStoragePermission();
                                break;
                            case TAG_FROM_PIC:
                                Toast.makeText(MainActivity.this, "图片识别", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        if (intent != null)
                            startActivity(intent);
                    }
                }).build().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public boolean isHasPermission(String permission) {
        if (EasyPermissions.hasPermissions(this, permission)) {
            return true;
        } else {
            return false;
        }
    }

    public void checkSMSPermission() {
        if (isHasPermission(Manifest.permission.READ_SMS)) {
            // 已经申请过权限，做想做的事
            startSMSActivity();
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, "短信权限，用于读取短信，快速添加账单",
                    SMS_PERMISSION, Manifest.permission.READ_SMS);
        }
    }

    public void checkStoragePermission() {
        if (isHasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // 已经申请过权限，做想做的事
            startActivity(AddByExcelActivity.class);
        } else {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(this, "读取权限",
                    STORAGE_PERMISSION, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == SMS_PERMISSION)
            startSMSActivity();
        else if (requestCode == STORAGE_PERMISSION)
            startActivity(AddByExcelActivity.class);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "关闭权限后无法使用该功能，请开启相应权限后再使用", Toast.LENGTH_SHORT).show();
    }

    private void startSMSActivity() {
        startActivity(AddBySMSActivity.class);
    }

}
