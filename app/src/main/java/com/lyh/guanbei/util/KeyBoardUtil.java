package com.lyh.guanbei.util;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lyh.guanbei.R;

import java.util.List;

public class KeyBoardUtil implements KeyboardView.OnKeyboardActionListener {
    private Keyboard.Key mKey;  //特殊按钮  完成/=
    private KeyboardView mKeyboardView;
    private EditText mEditText;
    private Context mContext;
    private boolean isDeal;       //是否处于加减状态  是的话显示=  否的话显示完成
    private Listener mListener;
    private static final String DONW = "完成";
    private static final String FINAL = "=";

    public KeyBoardUtil(Context context, final KeyboardView mKeyboardView, EditText mEditText, Listener mListener) {
        this.mKeyboardView = mKeyboardView;
        this.mEditText = mEditText;
        mContext = context;
        this.mListener = mListener;
        isDeal = false;

        Keyboard keyboard = new Keyboard(mContext, R.xml.customer_keyboard);
        List<Keyboard.Key> list = keyboard.getModifierKeys();
        if (list.size() == 1)
            mKey = list.get(0);
        mKeyboardView.setKeyboard(keyboard);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setOnKeyboardActionListener(this);
//        mEditText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction()==MotionEvent.ACTION_UP){
////                    v.setFocusable(true);
////                    v.setFocusableInTouchMode(true);
////                    v.requestFocus();
//                    hideSystemKeyboard((EditText)v);
//                    mKeyboardView.setVisibility(View.VISIBLE);
//                }
//                return true;
//            }
//        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    LogUtil.logD("得到焦点");
                    hideSystemKeyboard((EditText)v);
                    showKeyBoard();
                } else {
                    LogUtil.logD("失去焦点");
                    hideKeyBoard();
                }
            }
        });
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public void hideSystemKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive())
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    public void hideSystemKeyboard(EditText v) {
        if (v == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setShowSoftInputOnFocus(false);
        } else {
            v.setInputType(0);
        }
    }
//    public void hideSystemKeyboard(EditText v) {
//        this.mEditText = v;
//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm == null) {
//            return;
//        }
//        boolean isOpen = imm.isActive();
//        if (isOpen) {
//            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//        }
//    }

    public boolean isKeyBoardShow() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    public void hideKeyBoard() {
        mKeyboardView.setVisibility(View.GONE);
    }

    public void showKeyBoard() {
        mKeyboardView.setVisibility(View.VISIBLE);
    }

    /*
        以下都是接口方法
     */
    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = mEditText.getText();
        int start = mEditText.getSelectionStart();
        if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            //特殊按键
            if (isDeal) {
                //此时为=，需要进行求解
                int num = Util.calculate(editable.toString());
                LogUtil.logD("和  " + num);
                editable.replace(0, editable.length(), num + "");
                mKey.label = DONW;
                mKeyboardView.invalidateAllKeys();
                isDeal = false;
            } else {
                hideKeyBoard();
            }
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            if (editable != null && editable.length() > 0 && start > 0)
                editable.delete(start - 1, start);
        } else if (primaryCode == 60) {
            mListener.addMore();
            //清空
            editable.clear();
        } else {
            //当+ -时进入
            if (primaryCode == 43 || primaryCode == 45) {
                if (editable.length() <= 0)
                    return;
                char c = editable.charAt(editable.length() - 1);
                if (c == '-' || c == '+')
                    return;
                if (!isDeal) {
                    //处于显示完成的状态时
                    mKey.label = FINAL;
                    mKeyboardView.invalidateAllKeys();
                    isDeal = true;
                }
            }
            if (editable.toString().equals("0"))
                editable.clear();
            start = mEditText.getSelectionStart();
            editable.insert(start, Character.toString((char) primaryCode));
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public interface Listener {
        void addMore();
    }
}
