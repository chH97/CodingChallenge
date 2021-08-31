package de.hvrmn.codingchallenge.recycler;

import android.view.View;

public interface OnItemClickListener<T> {
    void onItemClick(T object, View view);
}
