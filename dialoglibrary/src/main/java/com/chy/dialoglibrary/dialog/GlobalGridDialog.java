package com.chy.dialoglibrary.dialog;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidRuntimeException;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.databinding.DialogGridBinding;
import com.chy.dialoglibrary.listener.CHYOnItemClickListener;

import java.util.ArrayList;

public class GlobalGridDialog extends AppCompatActivity {
    private DialogGridBinding mBinding;
    private static boolean initializeType = false;
    private static CHYOnItemClickListener chyOnItemClickListener;

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
        if (this.getIntent().getSerializableExtra("content") == null) {
            throw new NullPointerException("error:the \"content\" is null");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
        chyOnItemClickListener = null;
    }
}
