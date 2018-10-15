package com.chy.dialoglibrary.bean;

import java.io.Serializable;

public class SizeBean implements Serializable {
    public float titleSize;
    public float rightBtnSize;
    public float cancelBtnSize;
    public float contentSize;

    public SizeBean() {
        titleSize = 0f;
        rightBtnSize = 0f;
        cancelBtnSize = 0f;
        contentSize = 0f;
    }

    public SizeBean(float rightBtnSize, float contentSize) {
        this.rightBtnSize = rightBtnSize;
        this.contentSize = contentSize;
    }

    public SizeBean(float titleSize, float contentSize, float cancelBtnSize, float rightBtnSize) {
        this.titleSize = titleSize;
        this.rightBtnSize = rightBtnSize;
        this.cancelBtnSize = cancelBtnSize;
        this.contentSize = contentSize;
    }
}
