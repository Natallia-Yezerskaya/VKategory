package com.natallia.vkategory.UI;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;

import com.natallia.vkategory.R;

import java.util.ArrayList;


public class MyColor {

    private int color;

    public static ArrayList<MyColor> myColors = new ArrayList<MyColor>();

    public MyColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static void initialize(Context context){
        int[] colors = context.getResources().getIntArray(R.array.colorPalette);
        for (int i = 0; i < colors.length; i++) {
            int res = (int) new ArgbEvaluator().evaluate(0.3f ,colors[i], Color.WHITE);
            myColors.add(new MyColor(res));
        }
    }
}
