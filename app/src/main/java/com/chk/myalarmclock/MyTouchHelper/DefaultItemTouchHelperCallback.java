package com.chk.myalarmclock.MyTouchHelper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by chk on 17-10-19.
 */

public class DefaultItemTouchHelperCallback extends ItemTouchHelper.Callback{

    private OnItemTouchCallbackListener mOnItemTouchCallbackListener;
    private boolean isCanSwipe;

    public DefaultItemTouchHelperCallback(OnItemTouchCallbackListener mOnItemTouchCallbackListener) {
        this.mOnItemTouchCallbackListener = mOnItemTouchCallbackListener;
    }

    public void setmOnItemTouchCallbackListener(OnItemTouchCallbackListener mOnItemTouchCallbackListener) {
        this.mOnItemTouchCallbackListener = mOnItemTouchCallbackListener;
    }

    public void setSwipeEnable(boolean isCanSwipe) {
        this.isCanSwipe = isCanSwipe;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isCanSwipe;
    }



    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
            int orientation = linearLayoutManager.getOrientation();
            if (orientation == linearLayoutManager.VERTICAL) {
                int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(0,swipeFlag);
            }
        }


        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (mOnItemTouchCallbackListener != null) {
            mOnItemTouchCallbackListener.onSwipe(viewHolder.getAdapterPosition());
        }
    }

    public interface OnItemTouchCallbackListener{
        void onSwipe(int position);
    }
}
