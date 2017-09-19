package com.teamagam.gimelgimel.app.mainActivity.drawer.layers;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.teamagam.gimelgimel.app.mainActivity.drawer.LayersNodeDisplayer;
import com.teamagam.gimelgimel.domain.base.repository.IdentifiedData;
import com.teamagam.gimelgimel.domain.base.subscribers.ErrorLoggingObserver;
import io.reactivex.Observable;

public abstract class DrawerCategoryPresenter<T extends IdentifiedData> {

  private final LayersNodeDisplayer mLayersNodeDisplayer;
  private LayersNodeDisplayer.Node mCategoryNode;

  protected DrawerCategoryPresenter(LayersNodeDisplayer layersNodeDisplayer) {
    mLayersNodeDisplayer = layersNodeDisplayer;
  }

  public void presentCategory() {
    mCategoryNode =
        new LayersNodeDisplayer.NodeBuilder(getCategoryTitle()).setIcon(getCategoryIcon())
            .setOnIconClickListener(getCategoryIconClickListener())

            .createNode();
    mLayersNodeDisplayer.addNode(mCategoryNode);
  }

  public void presentCategoryItems() {
    getItemObservable().subscribe(new ErrorLoggingObserver<T>() {
      @Override
      public void onNext(T item) {
        getNodeDisplayer().display(item, mCategoryNode);
      }
    });
  }

  public abstract void start();

  public abstract void stop();

  protected abstract String getCategoryTitle();

  protected abstract Observable<T> getItemObservable();

  protected abstract NodeSelectionDisplayer<T> getNodeDisplayer();

  protected Drawable getCategoryIcon() {
    return null;
  }

  protected View.OnClickListener getCategoryIconClickListener() {
    return null;
  }
}
