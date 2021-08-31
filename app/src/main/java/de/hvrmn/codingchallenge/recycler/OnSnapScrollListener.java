package de.hvrmn.codingchallenge.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class OnSnapScrollListener extends RecyclerView.OnScrollListener {

    private int lastSnapPosition;
    private SnapHelper snapHelper;
    private RecyclerView.LayoutManager layoutManager;
    private OnSnapPositionChangeListener listener;

    public OnSnapScrollListener(SnapHelper snapHelper, RecyclerView.LayoutManager layoutManager, OnSnapPositionChangeListener listener) {
        this.snapHelper = snapHelper;
        this.layoutManager = layoutManager;
        this.listener = listener;
        lastSnapPosition = -1;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            View snapView = snapHelper.findSnapView(layoutManager);
            int position = layoutManager.getPosition(snapView);

            if (position != lastSnapPosition) {
                listener.onSnapPositionChanged(position);
                lastSnapPosition = position;
            }
        }
    }
}
