package com.teamagam.gimelgimel.app.map.esri.plugins;

import android.content.Context;
import android.util.AttributeSet;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.teamagam.gimelgimel.R;
import io.reactivex.schedulers.Schedulers;
import java.text.DecimalFormat;

import static java.lang.Math.abs;

public class ScaleBar extends android.support.v7.widget.AppCompatTextView
    implements SelfUpdatingViewPlugin {

  private static final int NO_OFFSET = 0;

  private final DecimalFormat mDecimalFormatter = new DecimalFormat("#,###,###,###");
  private SceneView mSceneView;
  private UIUpdatePoller mUIUpdatePoller;

  private ScaleBar(Context context, AttributeSet attrs) {
    super(context, attrs);
    setStyle();
    mUIUpdatePoller = new ScaleBarUIUpdatePoller();
  }

  public ScaleBar(Context context, AttributeSet attrs, SceneView sceneView) {
    this(context, attrs);
    mSceneView = sceneView;
  }

  @Override
  public void start() {
    if (mSceneView != null) {
      mUIUpdatePoller.start();
    }
  }

  @Override
  public void stop() {
    mUIUpdatePoller.stop();
  }

  private void setStyle() {
    setTextSize(getResources().getDimension(R.dimen.font_size_small));
    setTextColor(getResources().getColor(R.color.white));
    setShadowLayer(getResources().getDimension(R.dimen.small_shadow_radius), NO_OFFSET, NO_OFFSET,
        getResources().getColor(R.color.black));
    setBackgroundResource(R.drawable.rounded_corners);
  }

  private String format(double scale) {
    String formattedScale = mDecimalFormatter.format((int) scale);
    return String.format("1 : %s", String.valueOf(formattedScale));
  }

  private class ScaleBarUIUpdatePoller extends UIUpdatePoller {

    private int mCurrentVisibility;

    ScaleBarUIUpdatePoller() {
      super(Schedulers.computation());
    }

    @Override
    protected void periodicalAction() {
      updateVisibility();
      if (mCurrentVisibility == VISIBLE) {
        updateScale(getScale());
      }
    }

    private void updateVisibility() {
      mCurrentVisibility = isApproximatelyOrthogonal() ? VISIBLE : INVISIBLE;
      ScaleBar.this.post(() -> setVisibility(mCurrentVisibility));
    }

    private boolean isApproximatelyOrthogonal() {
      double pitch = ScaleBar.this.mSceneView.getCurrentViewpointCamera().getPitch();
      return abs(pitch) < 10;
    }

    private double getScale() {
      Viewpoint.Type type = Viewpoint.Type.CENTER_AND_SCALE;
      return ScaleBar.this.mSceneView.getCurrentViewpoint(type).getTargetScale();
    }

    private void updateScale(double scale) {
      ScaleBar.this.post(() -> setText(format(scale)));
    }
  }
}