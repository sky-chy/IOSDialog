package com.chy.dialoglibrary.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.databinding.DialogGridBinding;
import com.chy.dialoglibrary.databinding.ItemGridBinding;
import com.chy.dialoglibrary.listener.CHYOnItemClickListener;

import java.util.ArrayList;

public class GlobalGridDialog extends AppCompatActivity {
    private DialogGridBinding mBinding;
    private static boolean initializeType = false;
    private static CHYOnItemClickListener chyOnItemClickListener;
    private ArrayList<ContentBean> content;
    private ItemGridBinding itemGridBinding;
    private int dialogWidth;
    private int dialogHeight;
    private Intent intent;
    private int color;
    private float size;
    private RecyclerView.Adapter adapter;

    public static GlobalGridDialog getInstance(CHYOnItemClickListener listener) {
        chyOnItemClickListener = listener;
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
        } else if (((ArrayList<ContentBean>) this.getIntent().getSerializableExtra("content")).size() > 8) {
            throw new AndroidRuntimeException("error:ArrayList<ContentBean>.size() is greater than 8");
        }
        content = (ArrayList<ContentBean>) this.getIntent().getSerializableExtra("content");
        initAdapter();
        //设置颜色
        setColor();
        //设置字体大小
        setSize();
        //设置窗口尺寸
        setDialogSize();
    }


    /**
     * 初始化适配器
     */
    private void initAdapter() {
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
                lp.width = (int) ((dialogWidth - getResources().getDimension(R.dimen.dimen_30_0dp)) / 4);
                holder.itemView.setLayoutParams(lp);
                itemGridBinding.imgIcon.setBackgroundResource(content.get(position).icon);
                itemGridBinding.tvTitle.setText(content.get(position).title);
                itemGridBinding.tvTitle.setTextColor(color);
                if (size != 0f)
                    itemGridBinding.tvTitle.setTextSize(size);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (chyOnItemClickListener != null) {
                            chyOnItemClickListener.onItemClick(v, position);
                        }
                        finish();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return content.size();
            }
        };
        mBinding.rvGrid.setAdapter(adapter);
    }

    /**
     * 设置颜色
     */
    private void setColor() {
        if (this.getIntent().getIntExtra("color", 0) != 0) {
            this.color = this.getIntent().getIntExtra("color", 0);
        } else {
            color = Color.BLACK;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置字体大小
     */
    private void setSize() {
        if (this.getIntent().getFloatExtra("size", 0f) != 0) {
            this.size = this.getIntent().getFloatExtra("size", 0f);
            adapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
        chyOnItemClickListener = null;
        content = null;
        itemGridBinding = null;
        content = null;
        intent = null;
        adapter = null;
    }
}
