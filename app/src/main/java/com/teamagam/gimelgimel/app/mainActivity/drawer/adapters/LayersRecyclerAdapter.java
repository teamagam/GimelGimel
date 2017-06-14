package com.teamagam.gimelgimel.app.mainActivity.drawer.adapters;

import android.view.View;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerArrayAdapter;
import com.teamagam.gimelgimel.app.common.base.adapters.IdentifiedData;
import com.teamagam.gimelgimel.app.mainActivity.drawer.SimpleDrawerViewHolder;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import java.util.Comparator;

public class LayersRecyclerAdapter extends
    BaseRecyclerArrayAdapter<SimpleDrawerViewHolder, LayersRecyclerAdapter.IdentifiedVectorLayerAdapter> {

  public LayersRecyclerAdapter(OnItemClickListener<IdentifiedVectorLayerAdapter> onClickListener) {
    super(IdentifiedVectorLayerAdapter.class, new VectorLayerComparator(), onClickListener);
  }

  @Override
  protected SimpleDrawerViewHolder createNewViewHolder(View v, int viewType) {
    return new SimpleDrawerViewHolder(v);
  }

  @Override
  protected void bindItemToView(SimpleDrawerViewHolder holder, IdentifiedVectorLayerAdapter vlp) {
    holder.setText(vlp.getName());
    holder.setTextColor(getTextColor(vlp));
  }

  @Override
  protected int getListItemLayout(int viewType) {
    return R.layout.drawer_list_item;
  }

  private int getTextColor(IdentifiedVectorLayerAdapter vlp) {
    if (vlp.isShown()) {
      return R.color.colorPrimaryDark;
    }
    return R.color.secondaryText;
  }

  public static class IdentifiedVectorLayerAdapter extends VectorLayerPresentation
      implements IdentifiedData {

    public IdentifiedVectorLayerAdapter(VectorLayerPresentation vectorLayer) {
      super(vectorLayer, vectorLayer.getLocalURI(), vectorLayer.isShown());
    }
  }

  private static class VectorLayerComparator implements Comparator<IdentifiedVectorLayerAdapter> {
    @Override
    public int compare(IdentifiedVectorLayerAdapter lhs, IdentifiedVectorLayerAdapter rhs) {
      return lhs.getName().compareTo(rhs.getName());
    }
  }
}
