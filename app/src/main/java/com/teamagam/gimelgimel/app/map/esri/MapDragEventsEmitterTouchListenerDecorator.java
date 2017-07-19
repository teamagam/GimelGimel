package com.teamagam.gimelgimel.app.map.esri;

import android.view.MotionEvent;
import android.view.View;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.teamagam.gimelgimel.app.map.MapDragEvent;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.Subject;

class MapDragEventsEmitterTouchListenerDecorator implements View.OnTouchListener {

  private MapOnTouchListener mDecorated;
  private MapDragEventsEmitter mMapDragEventsEmitter;

  public MapDragEventsEmitterTouchListenerDecorator(MapOnTouchListener decorated,
      EsriGGMapView esriGGMapView,
      Subject<MapDragEvent> subject,
      BiFunction<Float, Float, PointGeometry> screenToGround) {
    mDecorated = decorated;
    mMapDragEventsEmitter = new MapDragEventsEmitter(esriGGMapView, subject, screenToGround);
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    mMapDragEventsEmitter.onTouch(v, event);
    return mDecorated.onTouch(v, event);
  }

  private class MapDragEventsEmitter extends MapOnTouchListener {

    private final Subject<MapDragEvent> mMapDragEventSubject;
    private final BiFunction<Float, Float, PointGeometry> mScreenToGrounder;

    public MapDragEventsEmitter(MapView view,
        Subject<MapDragEvent> mapDragEventSubject,
        BiFunction<Float, Float, PointGeometry> screenToGrounder) {
      super(view.getContext(), view);
      mMapDragEventSubject = mapDragEventSubject;
      mScreenToGrounder = screenToGrounder;
    }

    @Override
    public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
      return true;
    }

    @Override
    public boolean onPinchPointersDown(MotionEvent event) {
      return true;
    }

    @Override
    public boolean onPinchPointersMove(MotionEvent event) {
      return true;
    }

    @Override
    public boolean onPinchPointersUp(MotionEvent event) {
      return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent point) {
      return true;
    }

    @Override
    public boolean onLongPressUp(MotionEvent point) {
      return true;
    }

    @Override
    public boolean onSingleTap(MotionEvent point) {
      return true;
    }

    @Override
    public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
      return true;
    }

    @Override
    public boolean onDoubleTapDrag(MotionEvent from, MotionEvent to) {
      return true;
    }

    @Override
    public boolean onDoubleTapDragUp(MotionEvent up) {
      return true;
    }

    @Override
    public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
      mMapDragEventSubject.onNext(createDragEvent(from, to));
      return true;
    }

    private MapDragEvent createDragEvent(MotionEvent from, MotionEvent to) {
      return new MapDragEvent(motionEventToGround(from), motionEventToGround(to));
    }

    private PointGeometry motionEventToGround(MotionEvent motionEvent) {
      try {
        return mScreenToGrounder.apply(motionEvent.getX(), motionEvent.getY());
      } catch (Exception e) {
        throw new RuntimeException("Couldn't calculate screen to ground", e);
      }
    }
  }
}
