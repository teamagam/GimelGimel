package com.teamagam.gimelgimel.app.common.base.adapters;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Represents an abstract view holder with auto-binding functions
 * NOTE: If subclassed from an Internal class, it must be a static class
 */
public abstract class BaseRecyclerViewHolder<DATA> extends RecyclerView.ViewHolder {

    public final Context mAppContext;

    private AnimatorSet mAnimatorSet;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        mAppContext = itemView.getContext().getApplicationContext();
        ButterKnife.bind(this, itemView);
    }

    public void setBackgroundColor(int backgroundColorId) {
        this.itemView.setBackgroundColor(
                ContextCompat.getColor(itemView.getContext(), backgroundColorId));
    }

    public void setAnimatorSet(AnimatorSet animatorSet) {
        mAnimatorSet = animatorSet;
    }

    public void startAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    public void stopAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.removeAllListeners();
            mAnimatorSet.end();
            mAnimatorSet.cancel();
        }
    }
}
