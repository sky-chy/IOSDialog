package com.chy.dialoglibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.databinding.DialogTextBinding;
import com.chy.dialoglibrary.listener.CHYOnRightClickListener;

/**
 * @author chenhongye
 */
public class LocalTextDialog extends Dialog {

    private DialogTextBinding mBinding = null;
    private Context mContext;

    public LocalTextDialog(@NonNull Context context) {
        this(context, R.style.CHYDialog);

    }

    private LocalTextDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        this.setCanceledOnTouchOutside(true);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_text, null, false);
        setContentView(mBinding.getRoot());
        setDialogSize();
    }

    /**
     * 设置窗口尺寸
     */
    private void setDialogSize() {
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        WindowManager.LayoutParams at = this.getWindow().getAttributes();
        at.height = (int) (height * 0.2);
        at.width = (int) (width * 0.8);
        this.getWindow().setAttributes(at);
    }

    /**
     * 创建对话框
     *
     * @param bean                 对话框数据源
     * @param onRightClickListener 确定按钮的监听
     */
    public void createDialog(ContentBean bean, final CHYOnRightClickListener onRightClickListener) {
        if (bean == null) {
            throw new NullPointerException("the second params is null,method: ContentBean(String content, String rightButton)");
        }
        //设置提示框内容
        mBinding.setContent(bean);
        //btn监听
        mBinding.tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onRightClickListener != null)
                    onRightClickListener.onRightClick(v);
            }
        });
        this.show();
    }


    /**
     * 设置文字颜色
     */

    public void setContentColor(int color) {
        mBinding.tvContent.setTextColor(color);
    }

    public void setRightButtonColor(int color) {
        mBinding.tvRight.setTextColor(color);
    }

    /**
     * 设置文字大小
     */

    public void setContentSize(float size) {
        mBinding.tvContent.setTextSize(size);
    }

    public void setRightButtonSize(float size) {
        mBinding.tvRight.setTextSize(size);
    }

}