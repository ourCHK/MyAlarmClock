package com.chk.myalarmclock.MyAdapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chk on 17-10-17.
 */

public class MyDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Drawable mDivider;

    public static final int[] ATRRS = new int[] {
      android.R.attr.listDivider

    };

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    public MyDecoration() {
        super();

    }

    public MyDecoration(Context context) {
        mContext = context;
        final TypedArray ta = context.obtainStyledAttributes(ATRRS);
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
    }

    public void drawHorizontalLine(Canvas canvas,RecyclerView parent,RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i=0; i<childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams childParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + childParams.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
    }
}
