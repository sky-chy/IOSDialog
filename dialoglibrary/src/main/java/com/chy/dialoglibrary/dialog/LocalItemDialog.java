package com.chy.dialoglibrary.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.databinding.DialogItemBinding;
import com.chy.dialoglibrary.databinding.ItemTextviewBinding;
import com.chy.dialoglibrary.listener.CHYOnItemClickListener;

public class LocalItemDialog extends Dialog {
    private Context mContext;
    private DialogItemBinding mBinding;
    private ItemTextviewBinding mItemBinding;
    private float size;
    private int color;
    private int position;
    private RecyclerView.Adapter adapter;

    private LocalItemDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    public LocalItemDialog(Context context) {
        this(context, R.style.CHYDialog);
    }

    /**
     * 初始化
     */
    private void init() {
        this.setCanceledOnTouchOutside(true);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_item, null, false);
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
        at.width = (int) (width * 0.95);
        at.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(at);
    }

    /**
     * 创建item对话框
     *
     * @param item              数据源
     * @param btn               取消按钮
     * @param itemClickListener item监听器
     */
    public void createDialog(final String[] item, String btn, final CHYOnItemClickListener itemClickListener) {
        //初始化数据源
        initAdapter(item, itemClickListener);

        if (TextUtils.isEmpty(btn)) {
            mBinding.tvCancel.setText("取消");
        } else {
            mBinding.tvCancel.setText(btn);
        }
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                position = -1;
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, position);
                }
            }
        });
        this.show();
    }

    /**
     * 初始化数据源
     *
     * @param item              数据源
     * @param itemClickListener item的监听器
     */
    private void initAdapter(final String[] item, final CHYOnItemClickListener itemClickListener) {
        final int arrayLeght;
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            arrayLeght = 5;
        else
            arrayLeght = item.length;
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                mItemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_textview, parent, false);
                return new RecyclerView.ViewHolder(mItemBinding.getRoot()) {

                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int i) {
                if (i == 0) {
                    mItemBinding.tvItem.setBackgroundResource(R.drawable.ic_top_radius);
                } else if (i == getItemCount() - 1) {
                    mItemBinding.tvItem.setBackgroundResource(R.drawable.ic_bottom_radius);
                } else {
                    mItemBinding.tvItem.setBackgroundResource(R.drawable.ic_content_noradius);
                }
                if (size != 0f)
                    mItemBinding.tvItem.setTextSize(size);
                if (color != 0)
                    mItemBinding.tvItem.setTextColor(color);
                mItemBinding.tvItem.setText(item[i]);
                mItemBinding.tvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position = i;
                        if (itemClickListener != null)
                            itemClickListener.onItemClick(v, position);
                        dismiss();

                    }
                });
            }

            @Override
            public int getItemCount() {
                return arrayLeght;
            }
        };
        mBinding.rvItem.setAdapter(adapter);
    }

    /**
     * 设置文字大小
     */
    public void setCancelButtonColor(@ColorInt int color) {
        mBinding.tvCancel.setTextColor(color);
    }

    public void setContentColor(@ColorInt int color) {
        this.color = color;
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置文字颜色
     */
    public void setCancelButtonSize(float size) {
        mBinding.tvCancel.setTextSize(size);
    }

    public void setContentSize(float size) {
        this.size = size;
        adapter.notifyDataSetChanged();
    }

}
