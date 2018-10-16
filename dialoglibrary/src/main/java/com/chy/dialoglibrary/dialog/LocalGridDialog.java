package com.chy.dialoglibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.databinding.DialogGridBinding;
import com.chy.dialoglibrary.databinding.ItemGridBinding;
import com.chy.dialoglibrary.listener.CHYOnItemClickListener;

import java.util.ArrayList;

public class LocalGridDialog extends Dialog {
    private Context mContext;
    private DialogGridBinding mBinding;
    private ItemGridBinding itemGridBinding;
    private int dialogWidth;
    private int dialogHeight;
    private RecyclerView.Adapter adapter = null;
    private int color;
    private float size;

    private LocalGridDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    public LocalGridDialog(Context context) {
        this(context, R.style.CHYDialog);
    }

    /**
     * 初始化
     */
    private void init() {
        this.setCanceledOnTouchOutside(true);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_grid, null, false);
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
        if (height * 0.35 < (1280 * 0.3)) {
            dialogHeight= (int) (1280 * 0.3);
        } else
            dialogHeight = (int) (height * 0.35);
        dialogWidth = width;
        at.height = dialogHeight;
        at.width = dialogWidth;
        at.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(at);
    }

    /**
     * 创建item对话框
     *
     * @param item              数据源
     * @param itemClickListener item监听器
     */
    public void createDialog(final ArrayList<ContentBean> item, final CHYOnItemClickListener itemClickListener) {
        //初始化数据源
        initAdapter(item, itemClickListener);
        this.show();
    }

    /**
     * 初始化数据源
     *
     * @param item              数据源
     * @param itemClickListener item的监听器
     */
    private void initAdapter(final ArrayList<ContentBean> item, final CHYOnItemClickListener itemClickListener) {
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                itemGridBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_grid, parent, false);
                return new RecyclerView.ViewHolder(itemGridBinding.getRoot()) {

                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                lp.width = (int) ((dialogWidth - mContext.getResources().getDimension(R.dimen.dimen_30_0dp)) / 4);
                holder.itemView.setLayoutParams(lp);
                itemGridBinding.imgIcon.setBackgroundResource(item.get(position).icon);
                itemGridBinding.tvTitle.setText(item.get(position).title);
                if (color != 0)
                    itemGridBinding.tvTitle.setTextColor(color);
                if (size != 0f)
                    itemGridBinding.tvTitle.setTextSize(size);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(v, position);
                        }
                        dismiss();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return item.size();
            }
        };

        mBinding.rvGrid.setAdapter(adapter);
    }

    /**
     * 设置文字颜色
     *
     * @param color 颜色
     */
    public void setTextColor(@ColorInt int color) {
        this.color = color;
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置文字大小
     *
     * @param size 字体大小
     */
    public void setTextSize(float size) {
        this.size = size;
        adapter.notifyDataSetChanged();
    }
}
