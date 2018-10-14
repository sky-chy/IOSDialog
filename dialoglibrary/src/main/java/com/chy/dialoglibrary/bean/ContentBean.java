package com.chy.dialoglibrary.bean;

import java.io.Serializable;

public class ContentBean implements Serializable {
    public String title ;
    public String content = "内容";
    public int icon;
    public String cancelButton;
    public String rightButton = "确定";

    public ContentBean() {
    }

    public ContentBean(String content, String rightButton) {
        this.content = content;
        this.rightButton = rightButton;
    }

    public ContentBean(String title, String content, String cancelButton, String rightButton) {
        this.title = title;
        this.content = content;
        this.icon = icon;
        this.cancelButton = cancelButton;
        this.rightButton = rightButton;
    }
}
