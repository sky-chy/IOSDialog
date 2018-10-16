package com.chy.dialoglibrary.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.adapter.PagerAdapter;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.databinding.DialogGridBinding;
import com.chy.dialoglibrary.fragment.PagerFragment;
import com.chy.dialoglibrary.listener.CHYOnGridClickListener;

import java.util.ArrayList;

public class GlobalGridDialog extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private DialogGridBinding mBinding;
    private static boolean initializeType = false;
    private static CHYOnGridClickListener chyOnGridClickListener;
    private ArrayList<ContentBean> content;
    private int dialogWidth;
    private int dialogHeight;
    private Intent intent;
    private ArrayList<PagerFragment> fragments = new ArrayList<>();
    private View[] view;

    public static GlobalGridDialog getInstance(CHYOnGridClickListener listener) {
        chyOnGridClickListener = listener;
        initializeType = true;
        return new GlobalGridDialog();
    }

    public GlobalGridDialog() {
        if (!initializeType)
            throw new AndroidRuntimeException("error:Please use GlobalGridDialog.getInstance() to initialize");
    }

    /**
     * 原始状态的设置内容
     *
     * @param context 上下文
     * @param content 内容
     * @return intent
     */
    public Intent show(Context context, ArrayList<ContentBean> content) {
        initIntent(context, content, 0, 0);
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
    public Intent show(Context context, ArrayList<ContentBean> content, @ColorInt int color) {
        initIntent(context, content, color, 0);
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
    public Intent show(Context context, ArrayList<ContentBean> content, float size) {
        initIntent(context, content, 0, size);
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
    public Intent show(Context context, ArrayList<ContentBean> content, @ColorInt int color, float size) {
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
    private void initIntent(Context context, ArrayList<ContentBean> content, @ColorInt int color, float size) {
        intent = new Intent(context, this.getClass());
        intent.putExtra("content", content);
        intent.putExtra("color", color);
        intent.putExtra("size", size);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_grid);
        init();

    }

    @SuppressWarnings("unchecked")
    private void init() {
        if (this.getIntent().getSerializableExtra("content") == null) {
            throw new NullPointerException("error:the \"content\" is null, Please use ArrayList<ContentBean> to initialize");
        }
        content = (ArrayList<ContentBean>) this.getIntent().getSerializableExtra("content");
        initPager();
        //设置窗口尺寸
        setDialogSize();
        mBinding.pagerGrid.addOnPageChangeListener(this);
    }

    private void initPager() {
        //以8为基数向上取整作为页数
        int pagerCount = (int) Math.ceil((content.size() / 8f));
        //item集合游标
        int indexPager = 0;
        //item集合
        for (int i = 0; i < pagerCount; i++) {
            ArrayList<ContentBean> list = new ArrayList<>();
            while (list.size() != 8) {
                //第一页
                if (indexPager == 0) {
                    for (int j = 0; j < 8; j++) {
                        if (indexPager < pagerCount) {
                            list.add(content.get(indexPager));
                            indexPager++;
                        }
                    }
                    //其他页
                } else if (list.size() != 8 && indexPager < content.size()) {
                    list.add(content.get(indexPager));
                    indexPager++;
                } else {
                    break;
                }
            }

            Bundle bundle = new Bundle();
            bundle.putInt("color", this.getIntent().getIntExtra("color", 0));
            bundle.putFloat("size", this.getIntent().getFloatExtra("size", 0f));
            bundle.putSerializable("content", list);
            //初始化监听事件
            fragments.add(PagerFragment.getInstance(bundle, new CHYOnGridClickListener() {
                @Override
                public void onGridClick(View view, String title) {
                    chyOnGridClickListener.onGridClick(view, title);
                    finish();
                }
            }));
        }
        //初始化页面
        mBinding.pagerGrid.setAdapter(new PagerAdapter(fragments, getSupportFragmentManager()));
        initIndicator(pagerCount);
    }

    /**
     * 初始化指示器
     *
     * @param pagerCount 总页数
     */
    private void initIndicator(int pagerCount) {
        view = new View[pagerCount];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(25, 25);
        layoutParams.setMargins(15, 0, 15, 0);
        for (int i = 0; i < pagerCount; i++) {
            view[i] = new View(this);
            if (i == 0)
                view[i].setBackgroundResource(R.drawable.ic_solid_circle);
            else
                view[i].setBackgroundResource(R.drawable.ic_hollow_circle);
            view[i].setLayoutParams(layoutParams);
            mBinding.indicator.addView(view[i]);
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
        if (height * 0.45 < (1920 * 0.3)) {
            dialogHeight = (int) (1920 * 0.3);
        } else
            dialogHeight = (int) (height * 0.45);
        dialogWidth = width;
        at.height = dialogHeight;
        at.width = dialogWidth;
        at.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(at);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
        chyOnGridClickListener = null;
        content = null;
        content = null;
        intent = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        resetIndicator();
        view[position].setBackgroundResource(R.drawable.ic_solid_circle);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 重置指示器
     */
    private void resetIndicator() {
        for (int i = 0; i < view.length; i++) {
            view[i].setBackgroundResource(R.drawable.ic_hollow_circle);
        }
    }
}
