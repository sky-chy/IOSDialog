package com.chy.chydialog;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.chy.chydialog.databinding.ActivityMainBinding;
import com.chy.dialoglibrary.bean.ContentBean;
import com.chy.dialoglibrary.dialog.GlobalItemDialog;
import com.chy.dialoglibrary.dialog.GlobalRegularDialog;
import com.chy.dialoglibrary.dialog.GlobalTextDialog;
import com.chy.dialoglibrary.dialog.LocalItemDialog;
import com.chy.dialoglibrary.dialog.LocalRegularDialog;
import com.chy.dialoglibrary.dialog.LocalTextDialog;
import com.chy.dialoglibrary.listener.CHYOnCancelClickListener;
import com.chy.dialoglibrary.listener.CHYOnItemClickListener;
import com.chy.dialoglibrary.listener.CHYOnRightClickListener;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private Intent intent;
    private String[] strs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    private void init() {
        strs = new String[9];
        for (int i = 0; i < 9; i++) {
            strs[i] = "菜单" + i;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            /**
             * 全局对话框
             */
            case R.id.btn_text_dialog1:
                GlobalTextDialog globalTextDialog = GlobalTextDialog.getInstance(new CHYOnRightClickListener() {
                    @Override
                    public void onRightClick(View view) {
                        Toast.makeText(MainActivity.this, "点击了rightButton", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(globalTextDialog.show(this, new ContentBean("我是内容区", "好的")));
                break;
            case R.id.btn_right_dialog1:
                startGlobalDialog(GlobalRegularDialog.DIALOG_TYPE.RIGHT_DIALOG);
                break;
            case R.id.btn_error_dialog1:
                startGlobalDialog(GlobalRegularDialog.DIALOG_TYPE.ERROR_DIALOG);
                break;
            case R.id.btn_warning_dialog1:
                startGlobalDialog(GlobalRegularDialog.DIALOG_TYPE.WARNING_DIALOG);
                break;
            case R.id.btn_information_dialog1:
                startGlobalDialog(GlobalRegularDialog.DIALOG_TYPE.INFORMATION_DIALOG);
                break;
            case R.id.btn_item_dialog1:
                GlobalItemDialog globalItemDialog = GlobalItemDialog.getInstance(null, new CHYOnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(globalItemDialog.show(this, strs));
                break;
            case R.id.btn_grid_dialog1:

                break;
            /**
             * 局部对话框
             */
            case R.id.btn_text_dialog2:
                final LocalTextDialog localTextDialog = new LocalTextDialog(this);
                localTextDialog.createDialog(new ContentBean("我是内容", "好的"), new CHYOnRightClickListener() {
                    @Override
                    public void onRightClick(View view) {
                        localTextDialog.dismiss();
                    }
                });
                break;
            case R.id.btn_right_dialog2:
                startLocalDialog(LocalRegularDialog.DIALOG_TYPE.RIGHT_DIALOG);
                break;
            case R.id.btn_error_dialog2:
                startLocalDialog(LocalRegularDialog.DIALOG_TYPE.ERROR_DIALOG);
                break;
            case R.id.btn_warning_dialog2:
                startLocalDialog(LocalRegularDialog.DIALOG_TYPE.WARNING_DIALOG);
                break;
            case R.id.btn_information_dialog2:
                startLocalDialog(LocalRegularDialog.DIALOG_TYPE.INFORMATION_DIALOG);
                break;
            case R.id.btn_item_dialog2:
                LocalItemDialog localItemDialog = new LocalItemDialog(this);
                localItemDialog.createDialog(strs, "取消", new CHYOnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    /**
     * 启动全局对话框
     *
     * @param dialogType 对话框类型
     */
    private void startGlobalDialog(final GlobalRegularDialog.DIALOG_TYPE dialogType) {
        GlobalRegularDialog globalRegularDialog = GlobalRegularDialog.getInstance(dialogType, new CHYOnRightClickListener() {
            @Override
            public void onRightClick(View view) {
                Toast.makeText(MainActivity.this, "点击了全局对话框的" + dialogType + "的rightButton", Toast.LENGTH_SHORT).show();
            }
        }, new CHYOnCancelClickListener() {
            @Override
            public void onCancelClick(View view) {
                Toast.makeText(MainActivity.this, "点击了全局对话框的" + dialogType + "的cancelButton", Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(globalRegularDialog.show(this, new ContentBean("我是标题", "我是内容区", "取消", "好的")));
//        startActivity(globalRegularDialog.show(this, new ContentBean("我是内容区", "好的")));
    }

    /**
     * 启动局部对话框
     *
     * @param type 对话框类型
     */
    private void startLocalDialog(final LocalRegularDialog.DIALOG_TYPE type) {
        final LocalRegularDialog localRegularDialog = new LocalRegularDialog(this);
        ContentBean contentBean = new ContentBean("标题", "我是内容", "取消", "确定");
        //ContentBean contentBean = new ContentBean( "我是内容",  "确定");
        localRegularDialog.createDialog(type, contentBean, new CHYOnCancelClickListener() {
            @Override
            public void onCancelClick(View view) {
                Toast.makeText(MainActivity.this, "点击了局部对话框的" + type + "的cancelButton", Toast.LENGTH_SHORT).show();
            }
        }, new CHYOnRightClickListener() {
            @Override
            public void onRightClick(View view) {
                Toast.makeText(MainActivity.this, "点击了局部对话框的" + type + "的rightButton", Toast.LENGTH_SHORT).show();
                localRegularDialog.dismiss();
            }
        });
    }
}