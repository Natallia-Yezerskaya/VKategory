package com.natallia.vkategory;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.natallia.vkategory.UI.HeightListener;

/**
 * Created by Natallia on 12.04.2016.
 */
public class CardViewWithMaxHeight extends CardView {

    public static int WITHOUT_MAX_HEIGHT_VALUE = -1;
    static final String TAG = "MAX HEIGHT";

    public static int maxHeightMy = WITHOUT_MAX_HEIGHT_VALUE;
    private View mView;

    public void setMoreVisible(View view){
        this.mView = view;
    }

    public CardViewWithMaxHeight(Context context) {
        super(context);
        init();
    }

    public CardViewWithMaxHeight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CardViewWithMaxHeight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        maxHeightMy = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (maxHeightMy > 0){
            int hSize = MeasureSpec.getSize(heightMeasureSpec);
            int hMode = MeasureSpec.getMode(heightMeasureSpec);

            switch (hMode){
                case MeasureSpec.AT_MOST:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeightMy), MeasureSpec.AT_MOST);
                    Log.d(TAG, "onMeasure:  at most");

                    break;
                case MeasureSpec.UNSPECIFIED:
//                    if (hSize>maxHeightMy) {
//                        post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mView.setVisibility(VISIBLE);
//                            }
//                        });
//                    }
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeightMy, MeasureSpec.AT_MOST);

                    Log.d(TAG, "onMeasure: unspec");
                    break;
                case MeasureSpec.EXACTLY:
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(hSize, maxHeightMy), MeasureSpec.EXACTLY);
                    Log.d(TAG, "onMeasure:  exactly");
                    break;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override
    public void setMaxCardElevation(float radius) {
        super.setMaxCardElevation(radius);
    }

    @Override
    public void setCardElevation(float radius) {
        super.setCardElevation(radius);
    }
}
