package com.chy.dialoglibrary.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ColorBean;
import com.chy.dialoglibrary.bean.SizeBean;
import com.chy.dialoglibrary.databinding.DialogItemBinding;
import com.chy.dialoglibrary.databinding.ItemTextviewBinding;
import com.chy.dialoglibrary.listener.CHYOnItemClickListener;

public class GlobalItemDialog extends AppCompatActivity {
    private DialogItemBinding mBingding;
    private ItemTextviewBinding itemTextviewBinding;
    private static boolean initializeType = false;
    private static CHYOnItemClickListener chyOnItemClickListener;
    private String[] stritems;
    private int position;
    private static String cancel;
    private Intent intent;
    private ColorBean colorBean = new ColorBean();
    private SizeBean sizeBean = new SizeBean();
    private RecyclerView.Adapter adapter;

    public static GlobalItemDialog getInstance(String BtnCancel, CHYOnItemClickListener listener) {
        initializeType = true;
        chyOnItemClickListener = listener;
        cancel = BtnCancel;
        return new GlobalItemDialog();
    }

    public GlobalItemDialog() {
        if (!initializeType)
            throw new AndroidRuntimeException("error:Please use GlobalItemDialog.getInstance(String BtnCancel, CHYOnItemClickListener listener) to initialize");
    }

    /**
     * 原始状态的设置内容
     *
     * @param context 上下文
     * @param content 内容
     * @return intent
     */
    public Intent show(Context context, String[] content) {
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
    public Intent show(Context context, String[] content, ColorBean color) {
        initIntent(context, content, color, null);
        return intent;
    }

    /**
     * 改变文字大小设置内容
     *
     * @param context  上下文
     * @param content  文字内容
     * @param sizeBean 文字内容字体大小
     * @return intent
     */
    public Intent show(Context context, String[] content, SizeBean sizeBean) {
        initIntent(context, content, null, sizeBean);
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
    public Intent show(Context context, String[] content, ColorBean color, SizeBean size) {
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
    private void initIntent(Context context, String[] content, ColorBean color, SizeBean size) {
        intent = new Intent(context, this.getClass());
        intent.putExtra("content", content);
        intent.putExtra("color", color);
        intent.putExtra("size", size);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBingding = DataBindingUtil.setContentView(this, R.layout.dialog_item);
        init();
        initEvent();

    }

    /**
     * 初始化
     */
    private void init() {
        if (this.getIntent().getSerializableExtra("content") == null) {
            throw new NullPointerException("error:the \"content\" is null");
        }
        stritems = this.getIntent().getStringArrayExtra("content");
        if (stritems.length > 9) {
            throw new AndroidRuntimeException("error:The length of the string array is greater than 10");
        }
        if (!TextUtils.isEmpty(cancel))
            mBingding.tvCancel.setText(cancel);
        initAdapter();
        //设置颜色
        setColor();
        //设置字体大小
        setSize();
        //设置窗口尺寸
        setDialogSize();
    }

    private void initAdapter() {
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                itemTextviewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_textview, parent, false);
                return new RecyclerView.ViewHolder(itemTextviewBinding.getRoot()) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {
                position = i;
                if (position == 0) {
                    itemTextviewBinding.getRoot().setBackgroundResource(R.drawable.ic_top_radius);
                } else if (position == getItemCount() - 1) {
                    itemTextviewBinding.getRoot().setBackgroundResource(R.drawable.ic_bottom_radius);
                } else {
                    itemTextviewBinding.getRoot().setBackgroundResource(R.drawable.ic_content_noradius);
                }
                itemTextviewBinding.tvItem.setText(stritems[position]);
                if (colorBean.contentColor != 0) {
                    itemTextviewBinding.tvItem.setTextColor(colorBean.contentColor);
                }
                if (sizeBean.contentSize != 0f) {
                    itemTextviewBinding.tvItem.setTextSize(sizeBean.contentSize);
                }
                itemTextviewBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position = i;
                        finish();
                        if (chyOnItemClickListener != null) {
                            chyOnItemClickListener.onItemClick(itemTextviewBinding.getRoot(), position);
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return stritems.length;
            }
        };
        mBingding.rvItem.setAdapter(adapter);
    }

    /**
     * 设置颜色
     */
    private void setColor() {
        if (this.getIntent().getSerializableExtra("color") != null) {
            this.colorBean = (ColorBean) this.getIntent().getSerializableExtra("color");
            mBingding.tvCancel.setTextColor(colorBean.cancelBtnColor);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置字体大小
     */
    private void setSize() {
        if (this.getIntent().getSerializableExtra("size") != null) {
            this.sizeBean = (SizeBean) this.getIntent().getSerializableExtra("size");
        }
        if (sizeBean.cancelBtnSize != 0f)
            mBingding.tvCancel.setTextSize(sizeBean.cancelBtnSize);
        adapter.notifyDataSetChanged();

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
        at.width = (int) (width * 0.95);
        at.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(at);
    }

    private void initEvent() {
        mBingding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                position = -1;
                if (chyOnItemClickListener != null) {
                    chyOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chyOnItemClickListener = null;
        stritems = null;
        cancel = null;
        intent = null;
        adapter = null;
        colorBean = null;
        sizeBean = null;
    }

}
