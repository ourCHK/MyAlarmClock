package com.chk.myalarmclock.MyAdapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by chk on 17-10-19.
 */

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback{

    private ItemTouchHelperAdapter itemTouchHelperAdapter;
    private AlarmClockAdapter alarmClockAdapter;

    public MyItemTouchHelperCallback(ItemTouchHelperAdapter itemTouchHelperAdapter) {
        this.itemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    public MyItemTouchHelperCallback(ItemTouchHelperAdapter itemTouchHelperAdapter,AlarmClockAdapter alarmClockAdapter) {
        this.alarmClockAdapter = alarmClockAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        if (viewHolder instanceof AlarmClockAdapter.EmptyHolder) {
            return 0;
        } else {
            return makeMovementFlags(dragFlag,swipeFlag);
        }
    }

    @Override
    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
        return super.canDropOver(recyclerView, current, target);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (itemTouchHelperAdapter != null)
            itemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
