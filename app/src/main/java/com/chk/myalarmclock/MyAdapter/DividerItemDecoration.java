package com.chk.myalarmclock.MyAdapter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chk on 17-10-18.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private int mColor;
    private int mDividerSize;
    private int mOrientation;

    //分割线与两边的距离
    private float mStartMargin;
    private float mEndMargin;

    public DividerItemDecoration(int color, int dividerSize, int orientation) {
        this(color, dividerSize, orientation, 0, 0);
    }

    public DividerItemDecoration(int color, int dividerSize, int orientation, float startMargin, float endMargin) {
        mColor = color;
        mDividerSize = dividerSize;
        mOrientation = orientation;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        mStartMargin = startMargin;
        mEndMargin = endMargin;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVerticalDivider(c, parent);
        } else {
            drawHorizontalDivider(c, parent);
        }
    }

    //水平的list，画竖直的分割线
    private void drawHorizontalDivider(Canvas c, RecyclerView parent) {
        int top = 0;
        int bottom = parent.getMeasuredHeight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() + layoutParams.leftMargin;
            int right = top + mDividerSize;
            c.drawRect(left, top + mStartMargin
                    , right, bottom - mEndMargin, mPaint);
        }
    }

    //竖直的list，画水平的分割线
    private void drawVerticalDivider(Canvas c, RecyclerView parent) {
        final int left = 0;
        final int right = parent.getMeasuredWidth();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() + layoutParams.topMargin;
            final int bottom = top + mDividerSize;
            c.drawRect(left + mStartMargin, top
                    , right - mEndMargin, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDividerSize);
        } else {
            outRect.set(0, 0, mDividerSize, 0);
        }
    }

}