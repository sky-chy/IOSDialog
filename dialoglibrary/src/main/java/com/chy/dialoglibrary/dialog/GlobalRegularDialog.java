package com.chy.dialoglibrary.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ColorBean;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.bean.SizeBean;
import com.chy.dialoglibrary.databinding.DialogRegularBinding;
import com.chy.dialoglibrary.listener.CHYOnCancelClickListener;
import com.chy.dialoglibrary.listener.CHYOnRightClickListener;

public class GlobalRegularDialog extends AppCompatActivity implements View.OnClickListener {
    private DialogRegularBinding mBinding;
    private static CHYOnRightClickListener onRightClickListener;
    private static CHYOnCancelClickListener onCancelClickListener;
    private static DIALOG_TYPE dialog_type;
    private static boolean initializeType = false;
    private ColorBean colors = new ColorBean();
    private SizeBean size;
    private Intent intent;

    /**
     * 窗口类型枚举
     */
    public enum DIALOG_TYPE {
        RIGHT_DIALOG, ERROR_DIALOG, WARNING_DIALOG, INFORMATION_DIALOG
    }

    public static GlobalRegularDialog getInstance(DIALOG_TYPE type, CHYOnRightClickListener rightClickListener, CHYOnCancelClickListener cancelClickListener) {
        onRightClickListener = rightClickListener;
        onCancelClickListener = cancelClickListener;
        dialog_type = type;
        initializeType = true;
        return new GlobalRegularDialog();
    }

    public GlobalRegularDialog() {
        if (!initializeType)
            throw new AndroidRuntimeException("error:Please use GlobalRegularDialog.getInstance(DIALOG_TYPE type, CHYOnRightClickListener rightClickListener, CHYOnCancelClickListener cancelClickListener) to initialize");
    }

    /**
     * 原始状态的设置内容
     *
     * @param context 上下文
     * @param content 内容
     * @return intent
     */
    public Intent show(Context context, ContentBean content) {
        if (content == null) {
            throw new NullPointerException("error:the param \"content\" is null");
        } else if (!(content instanceof ContentBean)) {
            throw new AndroidRuntimeException("error:the param \"content\" is not instanceof com.chy.dialoglibrary.bean.ContentBean");
        }
        initIntent(context, content, null, null);
        return intent;
    }

    /**
     * 改变文字颜色设置内容
     *
     * @param context 上下文
     * @param content 文字内容
     * @param color   文字内容颜色
     * @return intent
     */
    public Intent show(Context context, ContentBean content, ColorBean color) {
        if (color == null) {
            throw new NullPointerException("error:the param \"color\" is null");
        } else if (!(color instanceof ColorBean)) {
            throw new AndroidRuntimeException("error:the param \"color\" is not instanceof com.chy.dialoglibrary.bean.ColorBean");
        }
        initIntent(context, content, color, null);
        return intent;
    }

    /**
     * 改变文字大小设置内容
     *
     * @param context 上下文
     * @param content 文字内容
     * @param size    文字内容字体大小
     * @return intent
     */
    public Intent show(Context context, ContentBean content, SizeBean size) {
        if (size == null) {
            throw new NullPointerException("error:the param \"size\" is null");
        } else if (!(size instanceof SizeBean)) {
            throw new AndroidRuntimeException("error:the param \"size\" is not instanceof com.chy.dialoglibrary.bean.SizeBean");
        }
        initIntent(context, content, null, size);
        return intent;
    }

    /**
     * 同时改变文字大小和颜色设置内容
     *
     * @param context 上下文
     * @param content 文字内容
     * @param color   文字内容颜色
     * @param size    文字内容字体大小
     * @return intent
     */
    public Intent show(Context context, ContentBean content, ColorBean color, SizeBean size) {
        if (color == null) {
            throw new NullPointerException("error:the param \"color\" is null");
        } else if (!(color instanceof ColorBean)) {
            throw new AndroidRuntimeException("error:the param \"color\" is not instanceof com.chy.dialoglibrary.bean.ColorBean");
        }
        if (size == null) {
            throw new NullPointerException("error:the param \"size\" is null");
        } else if (!(size instanceof SizeBean)) {
            throw new AndroidRuntimeException("error:the param \"size\" is not instanceof com.chy.dialoglibrary.bean.SizeBean");
        }
        initIntent(context, content, color, size);
        return intent;
    }

    /**
     * 初始化intent
     *
     * @param context 上下文
     * @param content 文字内容
     * @param color   文字内容文字颜色
     * @param size    文字内容文字大小
     */
    private void initIntent(Context context, ContentBean content, ColorBean color, SizeBean size) {
        intent = new Intent(context, this.getClass());
        intent.putExtra("content", content);
        if (color != null)
            intent.putExtra("color", color);
        if (size != null)
            intent.putExtra("size", size);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_regular);
        init();
        mBinding.tvRight.setOnClickListener(this);
        mBinding.tvCancel.setOnClickListener(this);
        //初始化icon
        initIcon();
    }

    private void init() {
        //设置内容
        setContent();
        //设置颜色
        setColor();
        //设置字体大小
        setSize();
        //设置窗口尺寸
        setDialogSize();
    }

    /**
     * 设置内容
     */
    private void setContent() {
        if (this.getIntent().getSerializableExtra("content") == null) {
            throw new AndroidRuntimeException("the \"content\" is null,method:ContentBean(String title, String content,  String cancelButton, String rightButton) or ContentBean(String content, String rightButton)");
        }
        ContentBean contentBean = (ContentBean) this.getIntent().getSerializableExtra("content");
        mBinding.setContent(contentBean);
    }

    /**
     * 设置颜色
     */
    private void setColor() {
        if (this.getIntent().getSerializableExtra("color") != null)
            colors = (ColorBean) this.getIntent().getSerializableExtra("color");
        mBinding.setColor(colors);
    }

    /**
     * 设置字体大小
     */
    private void setSize() {
        if (this.getIntent().getSerializableExtra("size") != null)
            size = (SizeBean) this.getIntent().getSerializableExtra("size");
        mBinding.setSize(size);
    }

    /**
     * 初始化icon
     */
    private void initIcon() {
        switch (dialog_type) {
            case ERROR_DIALOG:
                mBinding.imgIcon.setImageResource(R.mipmap.ic_error);
                break;
            case RIGHT_DIALOG:
                mBinding.imgIcon.setImageResource(R.mipmap.ic_right);
                break;
            case WARNING_DIALOG:
                mBinding.imgIcon.setImageResource(R.mipmap.ic_warning);
                break;
            case INFORMATION_DIALOG:
                mBinding.imgIcon.setImageResource(R.mipmap.ic_infomation);
                break;
        }
    }

    /**
     * 设置窗口尺寸
     */
    private void setDialogSize() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        WindowManager.LayoutParams at = this.getWindow().getAttributes();
        at.height = (int) (height * 0.4);
        at.width = (int) (width * 0.8);
        this.getWindow().setAttributes(at);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_right) {
            if (onRightClickListener != null) {
                onRightClickListener.onRightClick(mBinding.getRoot());
            }
        } else if (i == R.id.tv_cancel) {
            if (onCancelClickListener != null) {
                onCancelClickListener.onCancelClick(mBinding.getRoot());
            }
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onRightClickListener = null;
        onCancelClickListener = null;
        dialog_type = null;
        colors = null;
        size = null;
        intent = null;
    }
}
