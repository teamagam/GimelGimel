package com.teamagam.gimelgimel.app.map.esri;

import android.animation.LayoutTransition;
import android.animation.StateListAnimator;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.Size;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Display;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.ViewStructure;
import android.view.ViewTreeObserver;
import android.view.WindowId;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import java.util.ArrayList;

public class ViewGroupDelegator extends ViewGroup {

  private ViewGroup mDelegated;

  public ViewGroupDelegator(ViewGroup delegated) {
    super(delegated.getContext());
    mDelegated = delegated;
  }

  @Override
  public int getDescendantFocusability() {
    return mDelegated.getDescendantFocusability();
  }

  @Override
  public void setDescendantFocusability(int focusability) {
    mDelegated.setDescendantFocusability(focusability);
  }

  @Override
  public void requestChildFocus(View child, View focused) {
    mDelegated.requestChildFocus(child, focused);
  }

  @Override
  public void focusableViewAvailable(View v) {
    mDelegated.focusableViewAvailable(v);
  }

  @Override
  public boolean showContextMenuForChild(View originalView) {
    return mDelegated.showContextMenuForChild(originalView);
  }

  @Override
  public boolean showContextMenuForChild(View originalView, float x, float y) {
    return mDelegated.showContextMenuForChild(originalView, x, y);
  }

  @Override
  public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
    return mDelegated.startActionModeForChild(originalView, callback);
  }

  @Override
  public ActionMode startActionModeForChild(View originalView,
      ActionMode.Callback callback,
      int type) {
    return mDelegated.startActionModeForChild(originalView, callback, type);
  }

  @Override
  public View focusSearch(View focused, int direction) {
    return mDelegated.focusSearch(focused, direction);
  }

  @Override
  public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
    return mDelegated.requestChildRectangleOnScreen(child, rectangle, immediate);
  }

  @Override
  public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
    return mDelegated.requestSendAccessibilityEvent(child, event);
  }

  @Override
  public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
    return mDelegated.onRequestSendAccessibilityEvent(child, event);
  }

  @Override
  public void childHasTransientStateChanged(View child, boolean childHasTransientState) {
    mDelegated.childHasTransientStateChanged(child, childHasTransientState);
  }

  @Override
  public boolean hasTransientState() {
    return mDelegated.hasTransientState();
  }

  @Override
  public boolean dispatchUnhandledMove(View focused, int direction) {
    return mDelegated.dispatchUnhandledMove(focused, direction);
  }

  @Override
  public void clearChildFocus(View child) {
    mDelegated.clearChildFocus(child);
  }

  @Override
  public void clearFocus() {
    mDelegated.clearFocus();
  }

  @Override
  public View getFocusedChild() {
    return mDelegated.getFocusedChild();
  }

  @Override
  public boolean hasFocus() {
    return mDelegated.hasFocus();
  }

  @Override
  public View findFocus() {
    return mDelegated.findFocus();
  }

  @Override
  public boolean hasFocusable() {
    return mDelegated.hasFocusable();
  }

  @Override
  public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
    mDelegated.addFocusables(views, direction, focusableMode);
  }

  @Override
  public boolean getTouchscreenBlocksFocus() {
    return mDelegated.getTouchscreenBlocksFocus();
  }

  @Override
  public void setTouchscreenBlocksFocus(boolean touchscreenBlocksFocus) {
    mDelegated.setTouchscreenBlocksFocus(touchscreenBlocksFocus);
  }

  @Override
  public void findViewsWithText(ArrayList<View> outViews, CharSequence text, int flags) {
    mDelegated.findViewsWithText(outViews, text, flags);
  }

  @Override
  public void dispatchWindowFocusChanged(boolean hasFocus) {
    mDelegated.dispatchWindowFocusChanged(hasFocus);
  }

  @Override
  public void addTouchables(ArrayList<View> views) {
    mDelegated.addTouchables(views);
  }

  @Override
  public void dispatchDisplayHint(int hint) {
    mDelegated.dispatchDisplayHint(hint);
  }

  @Override
  public void dispatchWindowVisibilityChanged(int visibility) {
    mDelegated.dispatchWindowVisibilityChanged(visibility);
  }

  @Override
  public void dispatchConfigurationChanged(Configuration newConfig) {
    mDelegated.dispatchConfigurationChanged(newConfig);
  }

  @Override
  public void recomputeViewAttributes(View child) {
    mDelegated.recomputeViewAttributes(child);
  }

  @Override
  public void bringChildToFront(View child) {
    mDelegated.bringChildToFront(child);
  }

  @Override
  public boolean dispatchDragEvent(DragEvent event) {
    return mDelegated.dispatchDragEvent(event);
  }

  @Override
  public void dispatchWindowSystemUiVisiblityChanged(int visible) {
    mDelegated.dispatchWindowSystemUiVisiblityChanged(visible);
  }

  @Override
  public void dispatchSystemUiVisibilityChanged(int visible) {
    mDelegated.dispatchSystemUiVisibilityChanged(visible);
  }

  @Override
  public boolean dispatchKeyEventPreIme(KeyEvent event) {
    return mDelegated.dispatchKeyEventPreIme(event);
  }

  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    return mDelegated.dispatchKeyEvent(event);
  }

  @Override
  public boolean dispatchKeyShortcutEvent(KeyEvent event) {
    return mDelegated.dispatchKeyShortcutEvent(event);
  }

  @Override
  public boolean dispatchTrackballEvent(MotionEvent event) {
    return mDelegated.dispatchTrackballEvent(event);
  }

  @Override
  public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
    return mDelegated.onResolvePointerIcon(event, pointerIndex);
  }

  @Override
  public void addChildrenForAccessibility(ArrayList<View> outChildren) {
    mDelegated.addChildrenForAccessibility(outChildren);
  }

  @Override
  public boolean onInterceptHoverEvent(MotionEvent event) {
    return mDelegated.onInterceptHoverEvent(event);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return mDelegated.dispatchTouchEvent(ev);
  }

  @Override
  public boolean isMotionEventSplittingEnabled() {
    return mDelegated.isMotionEventSplittingEnabled();
  }

  @Override
  public void setMotionEventSplittingEnabled(boolean split) {
    mDelegated.setMotionEventSplittingEnabled(split);
  }

  @Override
  public boolean isTransitionGroup() {
    return mDelegated.isTransitionGroup();
  }

  @Override
  public void setTransitionGroup(boolean isTransitionGroup) {
    mDelegated.setTransitionGroup(isTransitionGroup);
  }

  @Override
  public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    mDelegated.requestDisallowInterceptTouchEvent(disallowIntercept);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return mDelegated.onInterceptTouchEvent(ev);
  }

  @Override
  public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
    return mDelegated.requestFocus(direction, previouslyFocusedRect);
  }

  @Override
  public void dispatchProvideStructure(ViewStructure structure) {
    mDelegated.dispatchProvideStructure(structure);
  }

  @Override
  public CharSequence getAccessibilityClassName() {
    return mDelegated.getAccessibilityClassName();
  }

  @Override
  public void notifySubtreeAccessibilityStateChanged(View child, View source, int changeType) {
    mDelegated.notifySubtreeAccessibilityStateChanged(child, source, changeType);
  }

  @Override
  public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
    return mDelegated.onNestedPrePerformAccessibilityAction(target, action, args);
  }

  @Override
  public ViewGroupOverlay getOverlay() {
    return mDelegated.getOverlay();
  }

  @Override
  public boolean getClipChildren() {
    return mDelegated.getClipChildren();
  }

  @Override
  public void setClipChildren(boolean clipChildren) {
    mDelegated.setClipChildren(clipChildren);
  }

  @Override
  public boolean getClipToPadding() {
    return mDelegated.getClipToPadding();
  }

  @Override
  public void setClipToPadding(boolean clipToPadding) {
    mDelegated.setClipToPadding(clipToPadding);
  }

  @Override
  public void dispatchSetSelected(boolean selected) {
    mDelegated.dispatchSetSelected(selected);
  }

  @Override
  public void dispatchSetActivated(boolean activated) {
    mDelegated.dispatchSetActivated(activated);
  }

  @Override
  public void dispatchDrawableHotspotChanged(float x, float y) {
    mDelegated.dispatchDrawableHotspotChanged(x, y);
  }

  @Override
  public void addView(View child) {
    mDelegated.addView(child);
  }

  @Override
  public void addView(View child, int index) {
    mDelegated.addView(child, index);
  }

  @Override
  public void addView(View child, int width, int height) {
    mDelegated.addView(child, width, height);
  }

  @Override
  public void addView(View child, LayoutParams params) {
    mDelegated.addView(child, params);
  }

  @Override
  public void addView(View child, int index, LayoutParams params) {
    mDelegated.addView(child, index, params);
  }

  @Override
  public void updateViewLayout(View view, LayoutParams params) {
    mDelegated.updateViewLayout(view, params);
  }

  @Override
  public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
    mDelegated.setOnHierarchyChangeListener(listener);
  }

  @Override
  public void onViewAdded(View child) {
    mDelegated.onViewAdded(child);
  }

  @Override
  public void onViewRemoved(View child) {
    mDelegated.onViewRemoved(child);
  }

  @Override
  public void removeView(View view) {
    mDelegated.removeView(view);
  }

  @Override
  public void removeViewInLayout(View view) {
    mDelegated.removeViewInLayout(view);
  }

  @Override
  public void removeViewsInLayout(int start, int count) {
    mDelegated.removeViewsInLayout(start, count);
  }

  @Override
  public void removeViewAt(int index) {
    mDelegated.removeViewAt(index);
  }

  @Override
  public void removeViews(int start, int count) {
    mDelegated.removeViews(start, count);
  }

  @Override
  public LayoutTransition getLayoutTransition() {
    return mDelegated.getLayoutTransition();
  }

  @Override
  public void setLayoutTransition(LayoutTransition transition) {
    mDelegated.setLayoutTransition(transition);
  }

  @Override
  public void removeAllViews() {
    mDelegated.removeAllViews();
  }

  @Override
  public void removeAllViewsInLayout() {
    mDelegated.removeAllViewsInLayout();
  }

  @Override
  public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
    return mDelegated.invalidateChildInParent(location, dirty);
  }

  @Override
  public boolean getChildVisibleRect(View child, Rect r, Point offset) {
    return mDelegated.getChildVisibleRect(child, r, offset);
  }

  @Override
  protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

  }

  @Override
  public void startLayoutAnimation() {
    mDelegated.startLayoutAnimation();
  }

  @Override
  public void scheduleLayoutAnimation() {
    mDelegated.scheduleLayoutAnimation();
  }

  @Override
  public LayoutAnimationController getLayoutAnimation() {
    return mDelegated.getLayoutAnimation();
  }

  @Override
  public void setLayoutAnimation(LayoutAnimationController controller) {
    mDelegated.setLayoutAnimation(controller);
  }

  @Override
  public boolean isAnimationCacheEnabled() {
    return mDelegated.isAnimationCacheEnabled();
  }

  @Override
  public void setAnimationCacheEnabled(boolean enabled) {
    mDelegated.setAnimationCacheEnabled(enabled);
  }

  @Override
  public boolean isAlwaysDrawnWithCacheEnabled() {
    return mDelegated.isAlwaysDrawnWithCacheEnabled();
  }

  @Override
  public void setAlwaysDrawnWithCacheEnabled(boolean always) {
    mDelegated.setAlwaysDrawnWithCacheEnabled(always);
  }

  @Override
  public int getPersistentDrawingCache() {
    return mDelegated.getPersistentDrawingCache();
  }

  @Override
  public void setPersistentDrawingCache(int drawingCacheToKeep) {
    mDelegated.setPersistentDrawingCache(drawingCacheToKeep);
  }

  @Override
  public int getLayoutMode() {
    return mDelegated.getLayoutMode();
  }

  @Override
  public void setLayoutMode(int layoutMode) {
    mDelegated.setLayoutMode(layoutMode);
  }

  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return mDelegated.generateLayoutParams(attrs);
  }

  @Override
  public int indexOfChild(View child) {
    return mDelegated.indexOfChild(child);
  }

  @Override
  public int getChildCount() {
    return mDelegated.getChildCount();
  }

  @Override
  public View getChildAt(int index) {
    return mDelegated.getChildAt(index);
  }

  @Override
  public void clearDisappearingChildren() {
    mDelegated.clearDisappearingChildren();
  }

  @Override
  public void startViewTransition(View view) {
    mDelegated.startViewTransition(view);
  }

  @Override
  public void endViewTransition(View view) {
    mDelegated.endViewTransition(view);
  }

  @Override
  public boolean gatherTransparentRegion(Region region) {
    return mDelegated.gatherTransparentRegion(region);
  }

  @Override
  public void requestTransparentRegion(View child) {
    mDelegated.requestTransparentRegion(child);
  }

  @Override
  public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
    return mDelegated.dispatchApplyWindowInsets(insets);
  }

  @Override
  public Animation.AnimationListener getLayoutAnimationListener() {
    return mDelegated.getLayoutAnimationListener();
  }

  @Override
  public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
    mDelegated.setLayoutAnimationListener(animationListener);
  }

  @Override
  public void jumpDrawablesToCurrentState() {
    mDelegated.jumpDrawablesToCurrentState();
  }

  @Override
  public void setAddStatesFromChildren(boolean addsStates) {
    mDelegated.setAddStatesFromChildren(addsStates);
  }

  @Override
  public boolean addStatesFromChildren() {
    return mDelegated.addStatesFromChildren();
  }

  @Override
  public void childDrawableStateChanged(View child) {
    mDelegated.childDrawableStateChanged(child);
  }

  @Override
  public boolean shouldDelayChildPressedState() {
    return mDelegated.shouldDelayChildPressedState();
  }

  @Override
  public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
    return mDelegated.onStartNestedScroll(child, target, nestedScrollAxes);
  }

  @Override
  public void onNestedScrollAccepted(View child, View target, int axes) {
    mDelegated.onNestedScrollAccepted(child, target, axes);
  }

  @Override
  public void onStopNestedScroll(View child) {
    mDelegated.onStopNestedScroll(child);
  }

  @Override
  public void onNestedScroll(View target,
      int dxConsumed,
      int dyConsumed,
      int dxUnconsumed,
      int dyUnconsumed) {
    mDelegated.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
  }

  @Override
  public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    mDelegated.onNestedPreScroll(target, dx, dy, consumed);
  }

  @Override
  public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
    return mDelegated.onNestedFling(target, velocityX, velocityY, consumed);
  }

  @Override
  public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
    return mDelegated.onNestedPreFling(target, velocityX, velocityY);
  }

  @Override
  public int getNestedScrollAxes() {
    return mDelegated.getNestedScrollAxes();
  }

  @Override
  public String toString() {
    return mDelegated.toString();
  }

  @Override
  public int getVerticalFadingEdgeLength() {
    return mDelegated.getVerticalFadingEdgeLength();
  }

  @Override
  public void setFadingEdgeLength(@Px int length) {
    mDelegated.setFadingEdgeLength(length);
  }

  @Override
  public int getHorizontalFadingEdgeLength() {
    return mDelegated.getHorizontalFadingEdgeLength();
  }

  @Override
  public int getVerticalScrollbarWidth() {
    return mDelegated.getVerticalScrollbarWidth();
  }

  @Override
  public int getVerticalScrollbarPosition() {
    return mDelegated.getVerticalScrollbarPosition();
  }

  @Override
  public void setVerticalScrollbarPosition(int position) {
    mDelegated.setVerticalScrollbarPosition(position);
  }

  @Override
  public void setScrollIndicators(int indicators, int mask) {
    mDelegated.setScrollIndicators(indicators, mask);
  }

  @Override
  public int getScrollIndicators() {
    return mDelegated.getScrollIndicators();
  }

  @Override
  public void setScrollIndicators(int indicators) {
    mDelegated.setScrollIndicators(indicators);
  }

  @Override
  public void setOnScrollChangeListener(OnScrollChangeListener l) {
    mDelegated.setOnScrollChangeListener(l);
  }

  @Override
  public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
    mDelegated.addOnLayoutChangeListener(listener);
  }

  @Override
  public void removeOnLayoutChangeListener(OnLayoutChangeListener listener) {
    mDelegated.removeOnLayoutChangeListener(listener);
  }

  @Override
  public void addOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
    mDelegated.addOnAttachStateChangeListener(listener);
  }

  @Override
  public void removeOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
    mDelegated.removeOnAttachStateChangeListener(listener);
  }

  @Override
  public OnFocusChangeListener getOnFocusChangeListener() {
    return mDelegated.getOnFocusChangeListener();
  }

  @Override
  public void setOnFocusChangeListener(OnFocusChangeListener l) {
    mDelegated.setOnFocusChangeListener(l);
  }

  @Override
  public void setOnClickListener(@Nullable OnClickListener l) {
    mDelegated.setOnClickListener(l);
  }

  @Override
  public boolean hasOnClickListeners() {
    return mDelegated.hasOnClickListeners();
  }

  @Override
  public void setOnLongClickListener(@Nullable OnLongClickListener l) {
    mDelegated.setOnLongClickListener(l);
  }

  @Override
  public void setOnContextClickListener(@Nullable OnContextClickListener l) {
    mDelegated.setOnContextClickListener(l);
  }

  @Override
  public void setOnCreateContextMenuListener(OnCreateContextMenuListener l) {
    mDelegated.setOnCreateContextMenuListener(l);
  }

  @Override
  public boolean performClick() {
    return mDelegated.performClick();
  }

  @Override
  public boolean callOnClick() {
    return mDelegated.callOnClick();
  }

  @Override
  public boolean performLongClick() {
    return mDelegated.performLongClick();
  }

  @Override
  public boolean performLongClick(float x, float y) {
    return mDelegated.performLongClick(x, y);
  }

  @Override
  public boolean performContextClick(float x, float y) {
    return mDelegated.performContextClick(x, y);
  }

  @Override
  public boolean performContextClick() {
    return mDelegated.performContextClick();
  }

  @Override
  public boolean showContextMenu() {
    return mDelegated.showContextMenu();
  }

  @Override
  public boolean showContextMenu(float x, float y) {
    return mDelegated.showContextMenu(x, y);
  }

  @Override
  public ActionMode startActionMode(ActionMode.Callback callback) {
    return mDelegated.startActionMode(callback);
  }

  @Override
  public ActionMode startActionMode(ActionMode.Callback callback, int type) {
    return mDelegated.startActionMode(callback, type);
  }

  @Override
  public void setOnKeyListener(OnKeyListener l) {
    mDelegated.setOnKeyListener(l);
  }

  @Override
  public void setOnTouchListener(OnTouchListener l) {
    mDelegated.setOnTouchListener(l);
  }

  @Override
  public void setOnGenericMotionListener(OnGenericMotionListener l) {
    mDelegated.setOnGenericMotionListener(l);
  }

  @Override
  public void setOnHoverListener(OnHoverListener l) {
    mDelegated.setOnHoverListener(l);
  }

  @Override
  public void setOnDragListener(OnDragListener l) {
    mDelegated.setOnDragListener(l);
  }

  @Override
  public boolean requestRectangleOnScreen(Rect rectangle) {
    return mDelegated.requestRectangleOnScreen(rectangle);
  }

  @Override
  public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
    return mDelegated.requestRectangleOnScreen(rectangle, immediate);
  }

  @Override
  public void sendAccessibilityEvent(int eventType) {
    mDelegated.sendAccessibilityEvent(eventType);
  }

  @Override
  public void announceForAccessibility(CharSequence text) {
    mDelegated.announceForAccessibility(text);
  }

  @Override
  public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
    mDelegated.sendAccessibilityEventUnchecked(event);
  }

  @Override
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
    return mDelegated.dispatchPopulateAccessibilityEvent(event);
  }

  @Override
  public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
    mDelegated.onPopulateAccessibilityEvent(event);
  }

  @Override
  public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
    mDelegated.onInitializeAccessibilityEvent(event);
  }

  @Override
  public AccessibilityNodeInfo createAccessibilityNodeInfo() {
    return mDelegated.createAccessibilityNodeInfo();
  }

  @Override
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
    mDelegated.onInitializeAccessibilityNodeInfo(info);
  }

  @Override
  public void onProvideStructure(ViewStructure structure) {
    mDelegated.onProvideStructure(structure);
  }

  @Override
  public void onProvideVirtualStructure(ViewStructure structure) {
    mDelegated.onProvideVirtualStructure(structure);
  }

  @Override
  public void setAccessibilityDelegate(@Nullable AccessibilityDelegate delegate) {
    mDelegated.setAccessibilityDelegate(delegate);
  }

  @Override
  public AccessibilityNodeProvider getAccessibilityNodeProvider() {
    return mDelegated.getAccessibilityNodeProvider();
  }

  @Override
  public CharSequence getContentDescription() {
    return mDelegated.getContentDescription();
  }

  @Override
  public void setContentDescription(CharSequence contentDescription) {
    mDelegated.setContentDescription(contentDescription);
  }

  @Override
  public int getAccessibilityTraversalBefore() {
    return mDelegated.getAccessibilityTraversalBefore();
  }

  @Override
  public void setAccessibilityTraversalBefore(int beforeId) {
    mDelegated.setAccessibilityTraversalBefore(beforeId);
  }

  @Override
  public int getAccessibilityTraversalAfter() {
    return mDelegated.getAccessibilityTraversalAfter();
  }

  @Override
  public void setAccessibilityTraversalAfter(int afterId) {
    mDelegated.setAccessibilityTraversalAfter(afterId);
  }

  @Override
  public int getLabelFor() {
    return mDelegated.getLabelFor();
  }

  @Override
  public void setLabelFor(@IdRes int id) {
    mDelegated.setLabelFor(id);
  }

  @Override
  public boolean isFocused() {
    return mDelegated.isFocused();
  }

  @Override
  public boolean isScrollContainer() {
    return mDelegated.isScrollContainer();
  }

  @Override
  public void setScrollContainer(boolean isScrollContainer) {
    mDelegated.setScrollContainer(isScrollContainer);
  }

  @Override
  public int getDrawingCacheQuality() {
    return mDelegated.getDrawingCacheQuality();
  }

  @Override
  public void setDrawingCacheQuality(int quality) {
    mDelegated.setDrawingCacheQuality(quality);
  }

  @Override
  public boolean getKeepScreenOn() {
    return mDelegated.getKeepScreenOn();
  }

  @Override
  public void setKeepScreenOn(boolean keepScreenOn) {
    mDelegated.setKeepScreenOn(keepScreenOn);
  }

  @Override
  public int getNextFocusLeftId() {
    return mDelegated.getNextFocusLeftId();
  }

  @Override
  public void setNextFocusLeftId(int nextFocusLeftId) {
    mDelegated.setNextFocusLeftId(nextFocusLeftId);
  }

  @Override
  public int getNextFocusRightId() {
    return mDelegated.getNextFocusRightId();
  }

  @Override
  public void setNextFocusRightId(int nextFocusRightId) {
    mDelegated.setNextFocusRightId(nextFocusRightId);
  }

  @Override
  public int getNextFocusUpId() {
    return mDelegated.getNextFocusUpId();
  }

  @Override
  public void setNextFocusUpId(int nextFocusUpId) {
    mDelegated.setNextFocusUpId(nextFocusUpId);
  }

  @Override
  public int getNextFocusDownId() {
    return mDelegated.getNextFocusDownId();
  }

  @Override
  public void setNextFocusDownId(int nextFocusDownId) {
    mDelegated.setNextFocusDownId(nextFocusDownId);
  }

  @Override
  public int getNextFocusForwardId() {
    return mDelegated.getNextFocusForwardId();
  }

  @Override
  public void setNextFocusForwardId(int nextFocusForwardId) {
    mDelegated.setNextFocusForwardId(nextFocusForwardId);
  }

  @Override
  public boolean isShown() {
    return mDelegated.isShown();
  }

  @Override
  public WindowInsets onApplyWindowInsets(WindowInsets insets) {
    return mDelegated.onApplyWindowInsets(insets);
  }

  @Override
  public void setOnApplyWindowInsetsListener(OnApplyWindowInsetsListener listener) {
    mDelegated.setOnApplyWindowInsetsListener(listener);
  }

  @Override
  public WindowInsets getRootWindowInsets() {
    return mDelegated.getRootWindowInsets();
  }

  @Override
  public WindowInsets computeSystemWindowInsets(WindowInsets in, Rect outLocalInsets) {
    return mDelegated.computeSystemWindowInsets(in, outLocalInsets);
  }

  @Override
  public boolean getFitsSystemWindows() {
    return mDelegated.getFitsSystemWindows();
  }

  @Override
  public void setFitsSystemWindows(boolean fitSystemWindows) {
    mDelegated.setFitsSystemWindows(fitSystemWindows);
  }

  @Override
  public void requestFitSystemWindows() {
    mDelegated.requestFitSystemWindows();
  }

  @Override
  public void requestApplyInsets() {
    mDelegated.requestApplyInsets();
  }

  @Override
  public int getVisibility() {
    return mDelegated.getVisibility();
  }

  @Override
  public void setVisibility(int visibility) {
    mDelegated.setVisibility(visibility);
  }

  @Override
  public boolean isEnabled() {
    return mDelegated.isEnabled();
  }

  @Override
  public void setEnabled(boolean enabled) {
    mDelegated.setEnabled(enabled);
  }

  @Override
  public void setFocusable(boolean focusable) {
    mDelegated.setFocusable(focusable);
  }

  @Override
  public void setFocusableInTouchMode(boolean focusableInTouchMode) {
    mDelegated.setFocusableInTouchMode(focusableInTouchMode);
  }

  @Override
  public boolean isSoundEffectsEnabled() {
    return mDelegated.isSoundEffectsEnabled();
  }

  @Override
  public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
    mDelegated.setSoundEffectsEnabled(soundEffectsEnabled);
  }

  @Override
  public boolean isHapticFeedbackEnabled() {
    return mDelegated.isHapticFeedbackEnabled();
  }

  @Override
  public void setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
    mDelegated.setHapticFeedbackEnabled(hapticFeedbackEnabled);
  }

  @Override
  public int getLayoutDirection() {
    return mDelegated.getLayoutDirection();
  }

  @Override
  public void setLayoutDirection(int layoutDirection) {
    mDelegated.setLayoutDirection(layoutDirection);
  }

  @Override
  public void setHasTransientState(boolean hasTransientState) {
    mDelegated.setHasTransientState(hasTransientState);
  }

  @Override
  public boolean isAttachedToWindow() {
    return mDelegated.isAttachedToWindow();
  }

  @Override
  public boolean isLaidOut() {
    return mDelegated.isLaidOut();
  }

  @Override
  public void setWillNotDraw(boolean willNotDraw) {
    mDelegated.setWillNotDraw(willNotDraw);
  }

  @Override
  public boolean willNotDraw() {
    return mDelegated.willNotDraw();
  }

  @Override
  public void setWillNotCacheDrawing(boolean willNotCacheDrawing) {
    mDelegated.setWillNotCacheDrawing(willNotCacheDrawing);
  }

  @Override
  public boolean willNotCacheDrawing() {
    return mDelegated.willNotCacheDrawing();
  }

  @Override
  public boolean isClickable() {
    return mDelegated.isClickable();
  }

  @Override
  public void setClickable(boolean clickable) {
    mDelegated.setClickable(clickable);
  }

  @Override
  public boolean isLongClickable() {
    return mDelegated.isLongClickable();
  }

  @Override
  public void setLongClickable(boolean longClickable) {
    mDelegated.setLongClickable(longClickable);
  }

  @Override
  public boolean isContextClickable() {
    return mDelegated.isContextClickable();
  }

  @Override
  public void setContextClickable(boolean contextClickable) {
    mDelegated.setContextClickable(contextClickable);
  }

  @Override
  public boolean isPressed() {
    return mDelegated.isPressed();
  }

  @Override
  public void setPressed(boolean pressed) {
    mDelegated.setPressed(pressed);
  }

  @Override
  public boolean isSaveEnabled() {
    return mDelegated.isSaveEnabled();
  }

  @Override
  public void setSaveEnabled(boolean enabled) {
    mDelegated.setSaveEnabled(enabled);
  }

  @Override
  public boolean getFilterTouchesWhenObscured() {
    return mDelegated.getFilterTouchesWhenObscured();
  }

  @Override
  public void setFilterTouchesWhenObscured(boolean enabled) {
    mDelegated.setFilterTouchesWhenObscured(enabled);
  }

  @Override
  public boolean isSaveFromParentEnabled() {
    return mDelegated.isSaveFromParentEnabled();
  }

  @Override
  public void setSaveFromParentEnabled(boolean enabled) {
    mDelegated.setSaveFromParentEnabled(enabled);
  }

  @Override
  public View focusSearch(int direction) {
    return mDelegated.focusSearch(direction);
  }

  @Override
  public ArrayList<View> getFocusables(int direction) {
    return mDelegated.getFocusables(direction);
  }

  @Override
  public void addFocusables(ArrayList<View> views, int direction) {
    mDelegated.addFocusables(views, direction);
  }

  @Override
  public ArrayList<View> getTouchables() {
    return mDelegated.getTouchables();
  }

  @Override
  public boolean isAccessibilityFocused() {
    return mDelegated.isAccessibilityFocused();
  }

  @Override
  public int getImportantForAccessibility() {
    return mDelegated.getImportantForAccessibility();
  }

  @Override
  public int getAccessibilityLiveRegion() {
    return mDelegated.getAccessibilityLiveRegion();
  }

  @Override
  public void setAccessibilityLiveRegion(int mode) {
    mDelegated.setAccessibilityLiveRegion(mode);
  }

  @Override
  public boolean isImportantForAccessibility() {
    return mDelegated.isImportantForAccessibility();
  }

  @Override
  public void setImportantForAccessibility(int mode) {
    mDelegated.setImportantForAccessibility(mode);
  }

  @Override
  public ViewParent getParentForAccessibility() {
    return mDelegated.getParentForAccessibility();
  }

  @Override
  public boolean dispatchNestedPrePerformAccessibilityAction(int action, Bundle arguments) {
    return mDelegated.dispatchNestedPrePerformAccessibilityAction(action, arguments);
  }

  @Override
  public boolean performAccessibilityAction(int action, Bundle arguments) {
    return mDelegated.performAccessibilityAction(action, arguments);
  }

  @Override
  public void dispatchStartTemporaryDetach() {
    mDelegated.dispatchStartTemporaryDetach();
  }

  @Override
  public void onStartTemporaryDetach() {
    mDelegated.onStartTemporaryDetach();
  }

  @Override
  public void dispatchFinishTemporaryDetach() {
    mDelegated.dispatchFinishTemporaryDetach();
  }

  @Override
  public void onFinishTemporaryDetach() {
    mDelegated.onFinishTemporaryDetach();
  }

  @Override
  public KeyEvent.DispatcherState getKeyDispatcherState() {
    return mDelegated.getKeyDispatcherState();
  }

  @Override
  public boolean onFilterTouchEventForSecurity(MotionEvent event) {
    return mDelegated.onFilterTouchEventForSecurity(event);
  }

  @Override
  public boolean dispatchGenericMotionEvent(MotionEvent event) {
    return mDelegated.dispatchGenericMotionEvent(event);
  }

  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus) {
    mDelegated.onWindowFocusChanged(hasWindowFocus);
  }

  @Override
  public boolean hasWindowFocus() {
    return mDelegated.hasWindowFocus();
  }

  @Override
  public void onVisibilityAggregated(boolean isVisible) {
    mDelegated.onVisibilityAggregated(isVisible);
  }

  @Override
  public int getWindowVisibility() {
    return mDelegated.getWindowVisibility();
  }

  @Override
  public void getWindowVisibleDisplayFrame(Rect outRect) {
    mDelegated.getWindowVisibleDisplayFrame(outRect);
  }

  @Override
  public boolean isInTouchMode() {
    return mDelegated.isInTouchMode();
  }

  @Override
  public boolean onKeyPreIme(int keyCode, KeyEvent event) {
    return mDelegated.onKeyPreIme(keyCode, event);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return mDelegated.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onKeyLongPress(int keyCode, KeyEvent event) {
    return mDelegated.onKeyLongPress(keyCode, event);
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    return mDelegated.onKeyUp(keyCode, event);
  }

  @Override
  public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
    return mDelegated.onKeyMultiple(keyCode, repeatCount, event);
  }

  @Override
  public boolean onKeyShortcut(int keyCode, KeyEvent event) {
    return mDelegated.onKeyShortcut(keyCode, event);
  }

  @Override
  public boolean onCheckIsTextEditor() {
    return mDelegated.onCheckIsTextEditor();
  }

  @Override
  public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    return mDelegated.onCreateInputConnection(outAttrs);
  }

  @Override
  public boolean checkInputConnectionProxy(View view) {
    return mDelegated.checkInputConnectionProxy(view);
  }

  @Override
  public void createContextMenu(ContextMenu menu) {
    mDelegated.createContextMenu(menu);
  }

  @Override
  public boolean onTrackballEvent(MotionEvent event) {
    return mDelegated.onTrackballEvent(event);
  }

  @Override
  public boolean onGenericMotionEvent(MotionEvent event) {
    return mDelegated.onGenericMotionEvent(event);
  }

  @Override
  public boolean onHoverEvent(MotionEvent event) {
    return mDelegated.onHoverEvent(event);
  }

  @Override
  public boolean isHovered() {
    return mDelegated.isHovered();
  }

  @Override
  public void setHovered(boolean hovered) {
    mDelegated.setHovered(hovered);
  }

  @Override
  public void onHoverChanged(boolean hovered) {
    mDelegated.onHoverChanged(hovered);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return mDelegated.onTouchEvent(event);
  }

  @Override
  public void cancelLongPress() {
    mDelegated.cancelLongPress();
  }

  @Override
  public TouchDelegate getTouchDelegate() {
    return mDelegated.getTouchDelegate();
  }

  @Override
  public void setTouchDelegate(TouchDelegate delegate) {
    mDelegated.setTouchDelegate(delegate);
  }

  @Override
  public void bringToFront() {
    mDelegated.bringToFront();
  }

  @Override
  public void setScrollX(@Px int value) {
    mDelegated.setScrollX(value);
  }

  @Override
  public void setScrollY(@Px int value) {
    mDelegated.setScrollY(value);
  }

  @Override
  public void getDrawingRect(Rect outRect) {
    mDelegated.getDrawingRect(outRect);
  }

  @Override
  public Matrix getMatrix() {
    return mDelegated.getMatrix();
  }

  @Override
  public float getCameraDistance() {
    return mDelegated.getCameraDistance();
  }

  @Override
  public void setCameraDistance(float distance) {
    mDelegated.setCameraDistance(distance);
  }

  @Override
  public float getRotation() {
    return mDelegated.getRotation();
  }

  @Override
  public void setRotation(float rotation) {
    mDelegated.setRotation(rotation);
  }

  @Override
  public float getRotationY() {
    return mDelegated.getRotationY();
  }

  @Override
  public void setRotationY(float rotationY) {
    mDelegated.setRotationY(rotationY);
  }

  @Override
  public float getRotationX() {
    return mDelegated.getRotationX();
  }

  @Override
  public void setRotationX(float rotationX) {
    mDelegated.setRotationX(rotationX);
  }

  @Override
  public float getScaleX() {
    return mDelegated.getScaleX();
  }

  @Override
  public void setScaleX(float scaleX) {
    mDelegated.setScaleX(scaleX);
  }

  @Override
  public float getScaleY() {
    return mDelegated.getScaleY();
  }

  @Override
  public void setScaleY(float scaleY) {
    mDelegated.setScaleY(scaleY);
  }

  @Override
  public float getPivotX() {
    return mDelegated.getPivotX();
  }

  @Override
  public void setPivotX(float pivotX) {
    mDelegated.setPivotX(pivotX);
  }

  @Override
  public float getPivotY() {
    return mDelegated.getPivotY();
  }

  @Override
  public void setPivotY(float pivotY) {
    mDelegated.setPivotY(pivotY);
  }

  @Override
  public float getAlpha() {
    return mDelegated.getAlpha();
  }

  @Override
  public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
    mDelegated.setAlpha(alpha);
  }

  @Override
  public void forceHasOverlappingRendering(boolean hasOverlappingRendering) {
    mDelegated.forceHasOverlappingRendering(hasOverlappingRendering);
  }

  @Override
  public boolean hasOverlappingRendering() {
    return mDelegated.hasOverlappingRendering();
  }

  @Override
  public boolean isDirty() {
    return mDelegated.isDirty();
  }

  @Override
  public float getX() {
    return mDelegated.getX();
  }

  @Override
  public void setX(@Px float x) {
    mDelegated.setX(x);
  }

  @Override
  public float getY() {
    return mDelegated.getY();
  }

  @Override
  public void setY(@Px float y) {
    mDelegated.setY(y);
  }

  @Override
  public float getZ() {
    return mDelegated.getZ();
  }

  @Override
  public void setZ(@Px float z) {
    mDelegated.setZ(z);
  }

  @Override
  public float getElevation() {
    return mDelegated.getElevation();
  }

  @Override
  public void setElevation(@Px float elevation) {
    mDelegated.setElevation(elevation);
  }

  @Override
  public float getTranslationX() {
    return mDelegated.getTranslationX();
  }

  @Override
  public void setTranslationX(@Px float translationX) {
    mDelegated.setTranslationX(translationX);
  }

  @Override
  public float getTranslationY() {
    return mDelegated.getTranslationY();
  }

  @Override
  public void setTranslationY(@Px float translationY) {
    mDelegated.setTranslationY(translationY);
  }

  @Override
  public float getTranslationZ() {
    return mDelegated.getTranslationZ();
  }

  @Override
  public void setTranslationZ(float translationZ) {
    mDelegated.setTranslationZ(translationZ);
  }

  @Override
  public StateListAnimator getStateListAnimator() {
    return mDelegated.getStateListAnimator();
  }

  @Override
  public void setStateListAnimator(StateListAnimator stateListAnimator) {
    mDelegated.setStateListAnimator(stateListAnimator);
  }

  @Override
  public void setClipToOutline(boolean clipToOutline) {
    mDelegated.setClipToOutline(clipToOutline);
  }

  @Override
  public ViewOutlineProvider getOutlineProvider() {
    return mDelegated.getOutlineProvider();
  }

  @Override
  public void setOutlineProvider(ViewOutlineProvider provider) {
    mDelegated.setOutlineProvider(provider);
  }

  @Override
  public void invalidateOutline() {
    mDelegated.invalidateOutline();
  }

  @Override
  public void getHitRect(Rect outRect) {
    mDelegated.getHitRect(outRect);
  }

  @Override
  public void getFocusedRect(Rect r) {
    mDelegated.getFocusedRect(r);
  }

  @Override
  public boolean getGlobalVisibleRect(Rect r, Point globalOffset) {
    return mDelegated.getGlobalVisibleRect(r, globalOffset);
  }

  @Override
  public void offsetTopAndBottom(@Px int offset) {
    mDelegated.offsetTopAndBottom(offset);
  }

  @Override
  public void offsetLeftAndRight(@Px int offset) {
    mDelegated.offsetLeftAndRight(offset);
  }

  @Override
  public LayoutParams getLayoutParams() {
    return mDelegated.getLayoutParams();
  }

  @Override
  public void setLayoutParams(LayoutParams params) {
    mDelegated.setLayoutParams(params);
  }

  @Override
  public void scrollTo(@Px int x, @Px int y) {
    mDelegated.scrollTo(x, y);
  }

  @Override
  public void scrollBy(@Px int x, @Px int y) {
    mDelegated.scrollBy(x, y);
  }

  @Override
  public void invalidate(Rect dirty) {
    mDelegated.invalidate(dirty);
  }

  @Override
  public void invalidate(int l, int t, int r, int b) {
    mDelegated.invalidate(l, t, r, b);
  }

  @Override
  public void invalidate() {
    mDelegated.invalidate();
  }

  @Override
  public boolean isOpaque() {
    return mDelegated.isOpaque();
  }

  @Override
  public Handler getHandler() {
    return mDelegated.getHandler();
  }

  @Override
  public boolean post(Runnable action) {
    return mDelegated.post(action);
  }

  @Override
  public boolean postDelayed(Runnable action, long delayMillis) {
    return mDelegated.postDelayed(action, delayMillis);
  }

  @Override
  public void postOnAnimation(Runnable action) {
    mDelegated.postOnAnimation(action);
  }

  @Override
  public void postOnAnimationDelayed(Runnable action, long delayMillis) {
    mDelegated.postOnAnimationDelayed(action, delayMillis);
  }

  @Override
  public boolean removeCallbacks(Runnable action) {
    return mDelegated.removeCallbacks(action);
  }

  @Override
  public void postInvalidate() {
    mDelegated.postInvalidate();
  }

  @Override
  public void postInvalidate(int left, int top, int right, int bottom) {
    mDelegated.postInvalidate(left, top, right, bottom);
  }

  @Override
  public void postInvalidateDelayed(long delayMilliseconds) {
    mDelegated.postInvalidateDelayed(delayMilliseconds);
  }

  @Override
  public void postInvalidateDelayed(long delayMilliseconds,
      int left,
      int top,
      int right,
      int bottom) {
    mDelegated.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
  }

  @Override
  public void postInvalidateOnAnimation() {
    mDelegated.postInvalidateOnAnimation();
  }

  @Override
  public void postInvalidateOnAnimation(int left, int top, int right, int bottom) {
    mDelegated.postInvalidateOnAnimation(left, top, right, bottom);
  }

  @Override
  public void computeScroll() {
    mDelegated.computeScroll();
  }

  @Override
  public boolean isHorizontalFadingEdgeEnabled() {
    return mDelegated.isHorizontalFadingEdgeEnabled();
  }

  @Override
  public void setHorizontalFadingEdgeEnabled(boolean horizontalFadingEdgeEnabled) {
    mDelegated.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
  }

  @Override
  public boolean isVerticalFadingEdgeEnabled() {
    return mDelegated.isVerticalFadingEdgeEnabled();
  }

  @Override
  public void setVerticalFadingEdgeEnabled(boolean verticalFadingEdgeEnabled) {
    mDelegated.setVerticalFadingEdgeEnabled(verticalFadingEdgeEnabled);
  }

  @Override
  public boolean isHorizontalScrollBarEnabled() {
    return mDelegated.isHorizontalScrollBarEnabled();
  }

  @Override
  public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
    mDelegated.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
  }

  @Override
  public boolean isVerticalScrollBarEnabled() {
    return mDelegated.isVerticalScrollBarEnabled();
  }

  @Override
  public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
    mDelegated.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
  }

  @Override
  public boolean isScrollbarFadingEnabled() {
    return mDelegated.isScrollbarFadingEnabled();
  }

  @Override
  public void setScrollbarFadingEnabled(boolean fadeScrollbars) {
    mDelegated.setScrollbarFadingEnabled(fadeScrollbars);
  }

  @Override
  public int getScrollBarDefaultDelayBeforeFade() {
    return mDelegated.getScrollBarDefaultDelayBeforeFade();
  }

  @Override
  public void setScrollBarDefaultDelayBeforeFade(int scrollBarDefaultDelayBeforeFade) {
    mDelegated.setScrollBarDefaultDelayBeforeFade(scrollBarDefaultDelayBeforeFade);
  }

  @Override
  public int getScrollBarFadeDuration() {
    return mDelegated.getScrollBarFadeDuration();
  }

  @Override
  public void setScrollBarFadeDuration(int scrollBarFadeDuration) {
    mDelegated.setScrollBarFadeDuration(scrollBarFadeDuration);
  }

  @Override
  public int getScrollBarSize() {
    return mDelegated.getScrollBarSize();
  }

  @Override
  public void setScrollBarSize(@Px int scrollBarSize) {
    mDelegated.setScrollBarSize(scrollBarSize);
  }

  @Override
  public int getScrollBarStyle() {
    return mDelegated.getScrollBarStyle();
  }

  @Override
  public void setScrollBarStyle(int style) {
    mDelegated.setScrollBarStyle(style);
  }

  @Override
  public boolean canScrollHorizontally(int direction) {
    return mDelegated.canScrollHorizontally(direction);
  }

  @Override
  public boolean canScrollVertically(int direction) {
    return mDelegated.canScrollVertically(direction);
  }

  @Override
  public void onScreenStateChanged(int screenState) {
    mDelegated.onScreenStateChanged(screenState);
  }

  @Override
  public void onRtlPropertiesChanged(int layoutDirection) {
    mDelegated.onRtlPropertiesChanged(layoutDirection);
  }

  @Override
  public boolean canResolveLayoutDirection() {
    return mDelegated.canResolveLayoutDirection();
  }

  @Override
  public boolean isLayoutDirectionResolved() {
    return mDelegated.isLayoutDirectionResolved();
  }

  @Override
  public IBinder getWindowToken() {
    return mDelegated.getWindowToken();
  }

  @Override
  public WindowId getWindowId() {
    return mDelegated.getWindowId();
  }

  @Override
  public IBinder getApplicationWindowToken() {
    return mDelegated.getApplicationWindowToken();
  }

  @Override
  public Display getDisplay() {
    return mDelegated.getDisplay();
  }

  @Override
  public void onCancelPendingInputEvents() {
    mDelegated.onCancelPendingInputEvents();
  }

  @Override
  public void saveHierarchyState(SparseArray<Parcelable> container) {
    mDelegated.saveHierarchyState(container);
  }

  @Override
  public void restoreHierarchyState(SparseArray<Parcelable> container) {
    mDelegated.restoreHierarchyState(container);
  }

  @Override
  public long getDrawingTime() {
    return mDelegated.getDrawingTime();
  }

  @Override
  public boolean isDuplicateParentStateEnabled() {
    return mDelegated.isDuplicateParentStateEnabled();
  }

  @Override
  public void setDuplicateParentStateEnabled(boolean enabled) {
    mDelegated.setDuplicateParentStateEnabled(enabled);
  }

  @Override
  public void setLayerType(int layerType, @Nullable Paint paint) {
    mDelegated.setLayerType(layerType, paint);
  }

  @Override
  public void setLayerPaint(@Nullable Paint paint) {
    mDelegated.setLayerPaint(paint);
  }

  @Override
  public int getLayerType() {
    return mDelegated.getLayerType();
  }

  @Override
  public void buildLayer() {
    mDelegated.buildLayer();
  }

  @Override
  public boolean isDrawingCacheEnabled() {
    return mDelegated.isDrawingCacheEnabled();
  }

  @Override
  public void setDrawingCacheEnabled(boolean enabled) {
    mDelegated.setDrawingCacheEnabled(enabled);
  }

  @Override
  public Bitmap getDrawingCache() {
    return mDelegated.getDrawingCache();
  }

  @Override
  public Bitmap getDrawingCache(boolean autoScale) {
    return mDelegated.getDrawingCache(autoScale);
  }

  @Override
  public void destroyDrawingCache() {
    mDelegated.destroyDrawingCache();
  }

  @Override
  public int getDrawingCacheBackgroundColor() {
    return mDelegated.getDrawingCacheBackgroundColor();
  }

  @Override
  public void setDrawingCacheBackgroundColor(@ColorInt int color) {
    mDelegated.setDrawingCacheBackgroundColor(color);
  }

  @Override
  public void buildDrawingCache() {
    mDelegated.buildDrawingCache();
  }

  @Override
  public void buildDrawingCache(boolean autoScale) {
    mDelegated.buildDrawingCache(autoScale);
  }

  @Override
  public boolean isInEditMode() {
    return mDelegated.isInEditMode();
  }

  @Override
  public boolean isHardwareAccelerated() {
    return mDelegated.isHardwareAccelerated();
  }

  @Override
  public Rect getClipBounds() {
    return mDelegated.getClipBounds();
  }

  @Override
  public void setClipBounds(Rect clipBounds) {
    mDelegated.setClipBounds(clipBounds);
  }

  @Override
  public boolean getClipBounds(Rect outRect) {
    return mDelegated.getClipBounds(outRect);
  }

  @Override
  public void draw(Canvas canvas) {
    mDelegated.draw(canvas);
  }

  @Override
  public int getSolidColor() {
    return mDelegated.getSolidColor();
  }

  @Override
  public boolean isLayoutRequested() {
    return mDelegated.isLayoutRequested();
  }

  @Override
  public Resources getResources() {
    return mDelegated.getResources();
  }

  @Override
  public void invalidateDrawable(@NonNull Drawable drawable) {
    mDelegated.invalidateDrawable(drawable);
  }

  @Override
  public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
    mDelegated.scheduleDrawable(who, what, when);
  }

  @Override
  public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
    mDelegated.unscheduleDrawable(who, what);
  }

  @Override
  public void unscheduleDrawable(Drawable who) {
    mDelegated.unscheduleDrawable(who);
  }

  @Override
  public void drawableHotspotChanged(float x, float y) {
    mDelegated.drawableHotspotChanged(x, y);
  }

  @Override
  public void refreshDrawableState() {
    mDelegated.refreshDrawableState();
  }

  @Override
  public void setBackgroundColor(@ColorInt int color) {
    mDelegated.setBackgroundColor(color);
  }

  @Override
  public void setBackgroundResource(@DrawableRes int resid) {
    mDelegated.setBackgroundResource(resid);
  }

  @Override
  public void setBackgroundDrawable(Drawable background) {
    mDelegated.setBackgroundDrawable(background);
  }

  @Override
  public Drawable getBackground() {
    return mDelegated.getBackground();
  }

  @Override
  public void setBackground(Drawable background) {
    mDelegated.setBackground(background);
  }

  @Nullable
  @Override
  public ColorStateList getBackgroundTintList() {
    return mDelegated.getBackgroundTintList();
  }

  @Override
  public void setBackgroundTintList(@Nullable ColorStateList tint) {
    mDelegated.setBackgroundTintList(tint);
  }

  @Nullable
  @Override
  public PorterDuff.Mode getBackgroundTintMode() {
    return mDelegated.getBackgroundTintMode();
  }

  @Override
  public void setBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
    mDelegated.setBackgroundTintMode(tintMode);
  }

  @Override
  public Drawable getForeground() {
    return mDelegated.getForeground();
  }

  @Override
  public void setForeground(Drawable foreground) {
    mDelegated.setForeground(foreground);
  }

  @Override
  public int getForegroundGravity() {
    return mDelegated.getForegroundGravity();
  }

  @Override
  public void setForegroundGravity(int gravity) {
    mDelegated.setForegroundGravity(gravity);
  }

  @Nullable
  @Override
  public ColorStateList getForegroundTintList() {
    return mDelegated.getForegroundTintList();
  }

  @Override
  public void setForegroundTintList(@Nullable ColorStateList tint) {
    mDelegated.setForegroundTintList(tint);
  }

  @Nullable
  @Override
  public PorterDuff.Mode getForegroundTintMode() {
    return mDelegated.getForegroundTintMode();
  }

  @Override
  public void setForegroundTintMode(@Nullable PorterDuff.Mode tintMode) {
    mDelegated.setForegroundTintMode(tintMode);
  }

  @Override
  public void onDrawForeground(Canvas canvas) {
    mDelegated.onDrawForeground(canvas);
  }

  @Override
  public void setPadding(@Px int left, @Px int top, @Px int right, @Px int bottom) {
    mDelegated.setPadding(left, top, right, bottom);
  }

  @Override
  public void setPaddingRelative(@Px int start, @Px int top, @Px int end, @Px int bottom) {
    mDelegated.setPaddingRelative(start, top, end, bottom);
  }

  @Override
  public int getPaddingTop() {
    return mDelegated.getPaddingTop();
  }

  @Override
  public int getPaddingBottom() {
    return mDelegated.getPaddingBottom();
  }

  @Override
  public int getPaddingLeft() {
    return mDelegated.getPaddingLeft();
  }

  @Override
  public int getPaddingStart() {
    return mDelegated.getPaddingStart();
  }

  @Override
  public int getPaddingRight() {
    return mDelegated.getPaddingRight();
  }

  @Override
  public int getPaddingEnd() {
    return mDelegated.getPaddingEnd();
  }

  @Override
  public boolean isPaddingRelative() {
    return mDelegated.isPaddingRelative();
  }

  @Override
  public boolean isSelected() {
    return mDelegated.isSelected();
  }

  @Override
  public void setSelected(boolean selected) {
    mDelegated.setSelected(selected);
  }

  @Override
  public boolean isActivated() {
    return mDelegated.isActivated();
  }

  @Override
  public void setActivated(boolean activated) {
    mDelegated.setActivated(activated);
  }

  @Override
  public ViewTreeObserver getViewTreeObserver() {
    return mDelegated.getViewTreeObserver();
  }

  @Override
  public View getRootView() {
    return mDelegated.getRootView();
  }

  @Override
  public void getLocationOnScreen(@Size(value = 2) int[] outLocation) {
    mDelegated.getLocationOnScreen(outLocation);
  }

  @Override
  public void getLocationInWindow(@Size(value = 2) int[] outLocation) {
    mDelegated.getLocationInWindow(outLocation);
  }

  @Override
  public int getId() {
    return mDelegated.getId();
  }

  @Override
  public void setId(@IdRes int id) {
    mDelegated.setId(id);
  }

  @Override
  public Object getTag() {
    return mDelegated.getTag();
  }

  @Override
  public void setTag(Object tag) {
    mDelegated.setTag(tag);
  }

  @Override
  public Object getTag(int key) {
    return mDelegated.getTag(key);
  }

  @Override
  public void setTag(int key, Object tag) {
    mDelegated.setTag(key, tag);
  }

  @Override
  public int getBaseline() {
    return mDelegated.getBaseline();
  }

  @Override
  public boolean isInLayout() {
    return mDelegated.isInLayout();
  }

  @Override
  public void requestLayout() {
    mDelegated.requestLayout();
  }

  @Override
  public void forceLayout() {
    mDelegated.forceLayout();
  }

  @Override
  public int getMinimumHeight() {
    return mDelegated.getMinimumHeight();
  }

  @Override
  public void setMinimumHeight(int minHeight) {
    mDelegated.setMinimumHeight(minHeight);
  }

  @Override
  public int getMinimumWidth() {
    return mDelegated.getMinimumWidth();
  }

  @Override
  public void setMinimumWidth(int minWidth) {
    mDelegated.setMinimumWidth(minWidth);
  }

  @Override
  public Animation getAnimation() {
    return mDelegated.getAnimation();
  }

  @Override
  public void setAnimation(Animation animation) {
    mDelegated.setAnimation(animation);
  }

  @Override
  public void startAnimation(Animation animation) {
    mDelegated.startAnimation(animation);
  }

  @Override
  public void clearAnimation() {
    mDelegated.clearAnimation();
  }

  @Override
  public void playSoundEffect(int soundConstant) {
    mDelegated.playSoundEffect(soundConstant);
  }

  @Override
  public boolean performHapticFeedback(int feedbackConstant) {
    return mDelegated.performHapticFeedback(feedbackConstant);
  }

  @Override
  public boolean performHapticFeedback(int feedbackConstant, int flags) {
    return mDelegated.performHapticFeedback(feedbackConstant, flags);
  }

  @Override
  public int getSystemUiVisibility() {
    return mDelegated.getSystemUiVisibility();
  }

  @Override
  public void setSystemUiVisibility(int visibility) {
    mDelegated.setSystemUiVisibility(visibility);
  }

  @Override
  public int getWindowSystemUiVisibility() {
    return mDelegated.getWindowSystemUiVisibility();
  }

  @Override
  public void onWindowSystemUiVisibilityChanged(int visible) {
    mDelegated.onWindowSystemUiVisibilityChanged(visible);
  }

  @Override
  public void setOnSystemUiVisibilityChangeListener(OnSystemUiVisibilityChangeListener l) {
    mDelegated.setOnSystemUiVisibilityChangeListener(l);
  }

  @Override
  public boolean onDragEvent(DragEvent event) {
    return mDelegated.onDragEvent(event);
  }

  @Override
  public int getOverScrollMode() {
    return mDelegated.getOverScrollMode();
  }

  @Override
  public void setOverScrollMode(int overScrollMode) {
    mDelegated.setOverScrollMode(overScrollMode);
  }

  @Override
  public boolean isNestedScrollingEnabled() {
    return mDelegated.isNestedScrollingEnabled();
  }

  @Override
  public void setNestedScrollingEnabled(boolean enabled) {
    mDelegated.setNestedScrollingEnabled(enabled);
  }

  @Override
  public boolean startNestedScroll(int axes) {
    return mDelegated.startNestedScroll(axes);
  }

  @Override
  public void stopNestedScroll() {
    mDelegated.stopNestedScroll();
  }

  @Override
  public boolean hasNestedScrollingParent() {
    return mDelegated.hasNestedScrollingParent();
  }

  @Override
  public boolean dispatchNestedScroll(int dxConsumed,
      int dyConsumed,
      int dxUnconsumed,
      int dyUnconsumed,
      @Nullable @Size(value = 2) int[] offsetInWindow) {
    return mDelegated.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
        offsetInWindow);
  }

  @Override
  public boolean dispatchNestedPreScroll(int dx,
      int dy,
      @Nullable @Size(value = 2) int[] consumed,
      @Nullable @Size(value = 2) int[] offsetInWindow) {
    return mDelegated.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
  }

  @Override
  public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
    return mDelegated.dispatchNestedFling(velocityX, velocityY, consumed);
  }

  @Override
  public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
    return mDelegated.dispatchNestedPreFling(velocityX, velocityY);
  }

  @Override
  public int getTextDirection() {
    return mDelegated.getTextDirection();
  }

  @Override
  public void setTextDirection(int textDirection) {
    mDelegated.setTextDirection(textDirection);
  }

  @Override
  public boolean canResolveTextDirection() {
    return mDelegated.canResolveTextDirection();
  }

  @Override
  public boolean isTextDirectionResolved() {
    return mDelegated.isTextDirectionResolved();
  }

  @Override
  public int getTextAlignment() {
    return mDelegated.getTextAlignment();
  }

  @Override
  public void setTextAlignment(int textAlignment) {
    mDelegated.setTextAlignment(textAlignment);
  }

  @Override
  public boolean canResolveTextAlignment() {
    return mDelegated.canResolveTextAlignment();
  }

  @Override
  public boolean isTextAlignmentResolved() {
    return mDelegated.isTextAlignmentResolved();
  }

  @Override
  public PointerIcon getPointerIcon() {
    return mDelegated.getPointerIcon();
  }

  @Override
  public void setPointerIcon(PointerIcon pointerIcon) {
    mDelegated.setPointerIcon(pointerIcon);
  }

  @Override
  public ViewPropertyAnimator animate() {
    return mDelegated.animate();
  }

  @Override
  public String getTransitionName() {
    return mDelegated.getTransitionName();
  }
}
