package com.chk.myalarmclock.MyAdapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chk on 17-10-18.
 */

public class DecorationTest extends RecyclerView.ItemDecoration{

    Paint paint;

    public DecorationTest() {
        paint = new Paint();
        paint.setColor(Color.BLUE);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0,0,0,40);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDraw(c, parent, state);
        drawHorizontalDivider(c,parent);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 绘制水平线
     * @param canvas
     * @param parent
     */
    private void drawHorizontalDivider(Canvas canvas,RecyclerView parent) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i=0; i<childCount; i++) {
            View view  = parent.getChildAt(i);
            int top = view.getBottom();
            int bottom = top + 40;
            canvas.drawRect(left,top,right,bottom,paint);
        }
    }
}
