package com.chy.dialoglibrary.dialog;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidRuntimeException;

import com.chy.dialoglibrary.R;
import com.chy.dialoglibrary.databinding.DialogGridBinding;

public class GlobalGridDialog extends AppCompatActivity {
    private DialogGridBinding mBinding;
    private static boolean initializeType = false;

    public static GlobalGridDialog getInstance() {
        initializeType = true;
        return new GlobalGridDialog();
    }

    public GlobalGridDialog() {
        if (!initializeType)
            throw new AndroidRuntimeException("error:Please use GlobalGridDialog.getInstance() to initialize");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_grid);
    }
}
