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
import com.chy.dialoglibrary.bean.ContentBean;
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

    public static GlobalItemDialog getInstance(String BtnCancel, CHYOnItemClickListener listener) {
        initializeType = true;
        chyOnItemClickListener = listener;
        cancel = BtnCancel;
        return new GlobalItemDialog();
    }

    public GlobalItemDialog() {
        if (!initializeType)
            throw new AndroidRuntimeException("error:Please use GlobalItemDialog.getInstance(CHYOnRightClickListener listener, String BtnCancel) to initialize");
    }

    public Intent show(Context context, String[] strs) {
        Intent intent = new Intent(context, this.getClass());
        intent.putExtra("content", strs);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBingding = DataBindingUtil.setContentView(this, R.layout.dialog_item);
        if (!(this.getIntent().getSerializableExtra("content") instanceof String[])) {
            throw new AndroidRuntimeException("error:the params is not instanceof String[])");
        }
        stritems = this.getIntent().getStringArrayExtra("content");
        if (stritems.length > 9) {
            throw new AndroidRuntimeException("error:The length of the string array is greater than 10");
        }
        if (!TextUtils.isEmpty(cancel))
            mBingding.tvCancel.setText(cancel);
        initData();
        initAdapter();
        setDialogSize();
    }

    private void initData() {
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

    private void initAdapter() {
        mBingding.rvItem.setAdapter(new RecyclerView.Adapter() {
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
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chyOnItemClickListener = null;
        stritems = null;
        cancel = null;
    }

}
