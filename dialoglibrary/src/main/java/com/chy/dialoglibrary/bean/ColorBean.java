package com.chy.dialoglibrary.bean;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import java.io.Serializable;

public class ColorBean implements Serializable {
    public int titleColor;
    public int rightBtnColor;
    public int cancelBtnColor;
    public int contentColor;

    public ColorBean() {
        titleColor = Color.BLACK;
        rightBtnColor = Color.BLUE;
        cancelBtnColor = Color.RED;
        contentColor = Color.parseColor("#4d4d4d");
    }

    public ColorBean(@ColorInt int rightBtnColor, @ColorInt int contentColor) {
        this.rightBtnColor = rightBtnColor;
        this.contentColor = contentColor;
    }

    public ColorBean(@ColorInt int titleColor, @ColorInt int contentColor, @ColorInt int cancelBtnColor, @ColorInt int rightBtnColor) {
        this.titleColor = titleColor;
        this.rightBtnColor = rightBtnColor;
        this.cancelBtnColor = cancelBtnColor;
        this.contentColor = contentColor;
    }
}
