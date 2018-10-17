package com.chy.dialoglibrary.dialog;

import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.adapter.PagerAdapter;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.databinding.DialogGridBinding;
import com.chy.dialoglibrary.fragment.PagerFragment;
import com.chy.dialoglibrary.listener.CHYOnGridClickListener;

import java.util.ArrayList;

public class LocalGridDialog extends AppCompatDialogFragment implements ViewPager.OnPageChangeListener {
    private DialogGridBinding mBinding;
    private static CHYOnGridClickListener chyOnGridClickListener;
    private int dialogWidth;
    private int dialogHeight;
    private ArrayList<PagerFragment> fragments = new ArrayList<>();
    private View[] view;
    public static LocalGridDialog getInstance(@ColorInt int color, float size, ArrayList<ContentBean> content, CHYOnGridClickListener listener) {
        chyOnGridClickListener = listener;
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", content);
        bundle.putInt("color", color);
        bundle.putFloat("size", size);
        LocalGridDialog fragment = new LocalGridDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static LocalGridDialog getInstance(ArrayList<ContentBean> content, CHYOnGridClickListener listener) {
        chyOnGridClickListener = listener;
        Bundle bundle = new Bundle();
        bundle.putSerializable("content", content);
        LocalGridDialog fragment = new LocalGridDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static LocalGridDialog getInstance(Bundle bundle, CHYOnGridClickListener listener) {
        chyOnGridClickListener = listener;
        LocalGridDialog fragment = new LocalGridDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CHYDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_grid, null, false);
        init();
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        setDialogSize();
    }

    private void init() {
        Bundle bundle = this.getArguments();
        ArrayList<ContentBean> content = (ArrayList<ContentBean>) bundle.getSerializable("content");
        initPager(content, bundle);
        mBinding.pagerGrid.addOnPageChangeListener(this);

    }

    private void initPager(ArrayList<ContentBean> content, Bundle oldBundle) {
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
            bundle.putInt("color", oldBundle.getInt("color", 0));
            bundle.putFloat("size", oldBundle.getFloat("size", 0f));
            bundle.putSerializable("content", list);
            //初始化监听事件
            fragments.add(PagerFragment.getInstance(bundle, new CHYOnGridClickListener() {
                @Override
                public void onGridClick(View view, String title) {
                    if (chyOnGridClickListener != null)
                        chyOnGridClickListener.onGridClick(view, title);
                    dismiss();
                }
            }));
        }
        //初始化页面
        mBinding.pagerGrid.setAdapter(new PagerAdapter(fragments, getChildFragmentManager()));
        initIndicator( pagerCount);
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
            view[i] = new View(getActivity());
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

        WindowManager.LayoutParams at = getDialog().getWindow().getAttributes();
        if (height * 0.45 < (1920 * 0.3)) {
            dialogHeight = (int) (1920 * 0.3);
        } else
            dialogHeight = (int) (height * 0.45);
        dialogWidth = width;
        at.height = dialogHeight;
        at.width = dialogWidth;
        at.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes(at);
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
