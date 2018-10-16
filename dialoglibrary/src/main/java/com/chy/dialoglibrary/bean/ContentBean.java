package com.chy.dialoglibrary.bean;

import java.io.Serializable;

public class ContentBean implements Serializable {
    public String title;
    public String content = "内容";
    public int icon;
    public String cancelButton;
    public String rightButton = "确定";

    public ContentBean() {
    }

    /**
     * 文本对话框或者常规对话框
     *
     * @param content     内容
     * @param rightButton 正确button
     */
    public ContentBean(String content, String rightButton) {
        this.content = content;
        this.rightButton = rightButton;
    }

    /**
     * 常规对话框
     *
     * @param title        标题
     * @param content      内容
     * @param cancelButton 取消对话框
     * @param rightButton  正确对话框
     */
    public ContentBean(String title, String content, String cancelButton, String rightButton) {
        this.title = title;
        this.content = content;
        this.cancelButton = cancelButton;
        this.rightButton = rightButton;
    }

    /**
     * grid对话框
     *
     * @param title 标题
     * @param icon  图标
     */
    public ContentBean(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }
}
