package com.example.inhacsecapstone.drugs;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewDecorator extends RecyclerView.ItemDecoration {

    private final int divWidth;

    public RecyclerViewDecorator(int divWidth) {
        this.divWidth = divWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = divWidth;
        outRect.left = divWidth;
        outRect.top = divWidth/2;
        outRect.bottom = divWidth/2;
    }
}