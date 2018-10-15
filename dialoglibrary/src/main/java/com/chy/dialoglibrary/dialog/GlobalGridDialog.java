package com.chy.dialoglibrary.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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

    public static GlobalGridDialog getInstance(CHYOnItemClickListener listener) {
        chyOnItemClickListener = listener;
        initializeType = true;
        return new GlobalGridDialog();
    }

    public GlobalGridDialog() {
        if (!initializeType)
            throw new AndroidRuntimeException("error:Please use GlobalGridDialog.getInstance() to initialize");
    }

    public Intent show(Context context, ArrayList<ContentBean> content) {
        Intent intent = new Intent(context, this.getClass());
        intent.putExtra("content", content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_grid);

        init();
        setDialogSize();
        initAdapter();

    }

    private void init() {
        if (this.getIntent().getSerializableExtra("content") == null) {
            throw new NullPointerException("error:the \"content\" is null, Please use ArrayList<ContentBean> to initialize");
        } else if (((ArrayList<ContentBean>) this.getIntent().getSerializableExtra("content")).size() > 8) {
            throw new AndroidRuntimeException("error:ArrayList<ContentBean>.size() is greater than 8");
        }
        content = (ArrayList<ContentBean>) this.getIntent().getSerializableExtra("content");
    }

    private void initAdapter() {
        mBinding.rvGrid.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                itemGridBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_grid, parent, false);
                return new RecyclerView.ViewHolder(itemGridBinding.getRoot()) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                lp.width = (int) ((dialogWidth - getResources().getDimension(R.dimen.dimen_30_0dp)) / 4);
                holder.itemView.setLayoutParams(lp);
                itemGridBinding.imgIcon.setBackgroundResource(content.get(position).icon);
                itemGridBinding.tvTitle.setText(content.get(position).title);
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
        if (content != null)
            content.clear();
        content = null;
    }
}
