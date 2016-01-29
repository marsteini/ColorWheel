package com.moonbytes.colorwheel.Helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Code ressource from http://sapandiwakar.in/recycler-view-item-click-handler/
 *
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mClickListener;
    private GestureDetector mGestureDetector;

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public RecyclerItemClickListener(Context context, OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View cView = rv.findChildViewUnder(e.getX(), e.getY());
        if(cView != null && mClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mClickListener.onItemClick(cView, rv.getChildAdapterPosition(cView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
