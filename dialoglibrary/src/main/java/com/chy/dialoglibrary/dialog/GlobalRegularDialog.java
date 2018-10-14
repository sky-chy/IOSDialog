package com.chy.dialoglibrary.dialog;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.databinding.DialogRegularBinding;
import com.chy.dialoglibrary.listener.CHYOnCancelClickListener;
import com.chy.dialoglibrary.listener.CHYOnRightClickListener;

public class GlobalRegularDialog extends AppCompatActivity implements View.OnClickListener {
    private DialogRegularBinding mBinding;
    private static CHYOnRightClickListener onRightClickListener;
    private static CHYOnCancelClickListener onCancelClickListener;
    private static DIALOG_TYPE dialog_type;
    private GlobalRegularDialog instance;
    private static boolean initializeType = false;

    /**
     * 窗口类型枚举
     */
    public enum DIALOG_TYPE {
        RIGHT_DIALOG, ERROR_DIALOG, WARNING_DIALOG, INFORMATION_DIALOG
    }

    public static GlobalRegularDialog getInstance(CHYOnRightClickListener rightClickListener, CHYOnCancelClickListener cancelClickListener, DIALOG_TYPE type) {
        onRightClickListener = rightClickListener;
        onCancelClickListener = cancelClickListener;
        dialog_type = type;
        initializeType = true;
        return new GlobalRegularDialog();
    }

    public GlobalRegularDialog() {
        if (!initializeType)
            throw new AndroidRuntimeException("error:Please use GlobalRegularDialog.getInstance(CHYOnRightClickListener rightClickListener, CHYOnCancelClickListener cancelClickListener, DIALOG_TYPE type) to initialize");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_regular);
        if (this.getIntent().getSerializableExtra("content") == null) {
            throw new AndroidRuntimeException("error:intent.putExtra(key,value),Please write \"content\" at the key");
        } else if (!(this.getIntent().getSerializableExtra("content") instanceof ContentBean)) {
            throw new AndroidRuntimeException("the params is not instanceof com.chy.dialoglibrary.bean.ContentBean,method:ContentBean(String title, String content,  String cancelButton, String rightButton) or ContentBean(String content, String rightButton)");
        }
        ContentBean contentBean = (ContentBean) this.getIntent().getSerializableExtra("content");
        mBinding.setContent(contentBean);
        setDialogSize();
        mBinding.tvRight.setOnClickListener(this);
        mBinding.tvCancel.setOnClickListener(this);
        initIcon();
    }

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
    }
}
