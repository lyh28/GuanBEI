package com.lyh.guanbei.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.UpdateUserContract;
import com.lyh.guanbei.mvp.presenter.UpdateUserPresenter;
import com.lyh.guanbei.util.FileUtil;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import androidx.annotation.Nullable;

public class MeEditActivity extends BaseActivity implements View.OnClickListener, UpdateUserContract.IUpdateUserView {
    private ImageView mIcon;
    private TextView mName;
    private TextView mId;

    private UpdateUserPresenter mUpdateUserPresenter;
    private static final int CHOOSE_CODE = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_me_edit;
    }

    @Override
    protected void initUi() {
        mIcon = findViewById(R.id.activity_me_icon);
        mName = findViewById(R.id.activity_me_name);
        mId = findViewById(R.id.activity_me_id);
        findViewById(R.id.activity_me_nameview).setOnClickListener(this);
        findViewById(R.id.activity_me_iconview).setOnClickListener(this);
        findViewById(R.id.activity_me_back).setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshData();
    }
    private void RefreshData(){
        User user=CustomSharedPreferencesManager.getInstance().getUser();
        mId.setText(user.getUser_id()+"");
        mName.setText(user.getUser_name());
        Glide.with(this).load(user.getUser_icon()).error(R.drawable.defaulticon).into(mIcon);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_me_back:
                finish();
                break;
            case R.id.activity_me_iconview:
                Intent intent = new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, CHOOSE_CODE);
                break;
            case R.id.activity_me_nameview:
                startActivity(UserNameEditActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_CODE:
                if (resultCode == RESULT_OK) {
                    String path = FileUtil.getFilePathByUri(this, data.getData());
                    String fileName = System.currentTimeMillis() + "_" + CustomSharedPreferencesManager.getInstance().getUser().getUser_id() + path.substring(path.lastIndexOf("."));
                    UCrop.Options options = new UCrop.Options();
                    options.setHideBottomControls(true);
                    options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
                    options.setToolbarTitle("裁剪图片");
                    UCrop.of(data.getData(), Uri.fromFile(FileUtil.getTmpFile(this, fileName)))
                            .withAspectRatio(1, 1)
                            .withOptions(options)
                            .start(this);
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                    mUpdateUserPresenter.updateIcon(UCrop.getOutput(data));
                }
                break;
        }
    }

    @Override
    public void onUpdateFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateSuccess(User user) {

    }

    @Override
    public void createPresenters() {
        mUpdateUserPresenter = new UpdateUserPresenter();
        addPresenter(mUpdateUserPresenter);
    }
}
