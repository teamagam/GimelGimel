package com.teamagam.gimelgimel.app.view.drawable;

import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.view.animation.DecelerateInterpolator;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.teamagam.gimelgimel.app.view.drawable.gestures.TransformGestureDetector;

/**
 * ZoomableController that adds animation capabilities to DefaultZoomableController using
 * nineoldandroid library
 */
public class AnimatedZoomableControllerSupport extends AbstractAnimatedZoomableController {

    private static final Class<?> TAG = AnimatedZoomableControllerSupport.class;

    private final ValueAnimator mValueAnimator;

    public AnimatedZoomableControllerSupport(TransformGestureDetector transformGestureDetector) {
        super(transformGestureDetector);
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
    }

    public static AnimatedZoomableControllerSupport newInstance() {
        return new AnimatedZoomableControllerSupport(TransformGestureDetector.newInstance());
    }

    public void setTransformAnimated(
            final Matrix newTransform,
            long durationMs,
            @Nullable final Runnable onAnimationComplete) {
        FLog.v(getLogTag(), "setTransformAnimated: duration %d ms", durationMs);
        stopAnimation();
        Preconditions.checkArgument(durationMs > 0);
        Preconditions.checkState(!isAnimating());
        setAnimating(true);
        mValueAnimator.setDuration(durationMs);
        getTransform().getValues(getStartValues());
        newTransform.getValues(getStopValues());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                calculateInterpolation(getWorkingTransform(), (float) valueAnimator.getAnimatedValue());
                AnimatedZoomableControllerSupport.super.setTransform(getWorkingTransform());
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                FLog.v(getLogTag(), "setTransformAnimated: animation cancelled");
                onAnimationStopped();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                FLog.v(getLogTag(), "setTransformAnimated: animation finished");
                onAnimationStopped();
            }

            private void onAnimationStopped() {
                if (onAnimationComplete != null) {
                    onAnimationComplete.run();
                }
                setAnimating(false);
                getDetector().restartGesture();
            }
        });
        mValueAnimator.start();
    }

    public void stopAnimation() {
        if (!isAnimating()) {
            return;
        }
        FLog.v(getLogTag(), "stopAnimation");
        mValueAnimator.cancel();
        mValueAnimator.removeAllUpdateListeners();
        mValueAnimator.removeAllListeners();
    }

    @Override
    protected Class<?> getLogTag() {
        return TAG;
    }

}
