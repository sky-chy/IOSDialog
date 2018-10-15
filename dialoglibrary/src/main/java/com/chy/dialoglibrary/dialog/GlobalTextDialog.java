package com.chy.dialoglibrary.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.databinding.DialogTextBinding;
import com.chy.dialoglibrary.listener.CHYOnRightClickListener;

public class GlobalTextDialog extends AppCompatActivity implements View.OnClickListener {
    private DialogTextBinding mBinding = null;
    private static CHYOnRightClickListener chyOnRightClickListener = null;
    private static boolean initializeType = false;

    public static GlobalTextDialog getInstance(CHYOnRightClickListener listener) {
        chyOnRightClickListener = listener;
        initializeType = true;
        return new GlobalTextDialog();
    }


    public GlobalTextDialog() {
        if (!initializeType)
            throw new AndroidRuntimeException("error:Please use GlobalTextDialog.getInstance(CHYOnRightClickListener listener) to initialize");
    }

    public Intent show(Context context, ContentBean bean) {
        Intent intent = new Intent(context, this.getClass());
        intent.putExtra("content", bean);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_text);
        init();
        setDialogSize();
        mBinding.tvRight.setOnClickListener(this);
    }

    private void init() {
    if (!(this.getIntent().getSerializableExtra("content") instanceof ContentBean)) {
            throw new AndroidRuntimeException("error:the params is not instanceof com.chy.dialoglibrary.bean.ContentBean,method: ContentBean(String content, String rightButton)");
        }
        ContentBean contentBean = (ContentBean) this.getIntent().getSerializableExtra("content");
        mBinding.setContent(contentBean);
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
        at.height = (int) (height * 0.2);
        at.width = (int) (width * 0.8);
        this.getWindow().setAttributes(at);
    }

    @Override
    public void onClick(View v) {
        if (chyOnRightClickListener != null)
            chyOnRightClickListener.onRightClick(mBinding.getRoot());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chyOnRightClickListener = null;
    }

}
