package com.teamagam.gimelgimel.app.mainActivity.drawer.adapters;

import android.view.View;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerArrayAdapter;
import com.teamagam.gimelgimel.app.mainActivity.drawer.SimpleDrawerViewHolder;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterPresentation;
import com.teamagam.gimelgimel.domain.messages.IdentifiedData;
import java.util.Comparator;

public class RastersRecyclerAdapter extends
    BaseRecyclerArrayAdapter<SimpleDrawerViewHolder, RastersRecyclerAdapter.IdentifiedRasterAdapter> {

  public RastersRecyclerAdapter(OnItemClickListener<IdentifiedRasterAdapter> onItemClickListener) {
    super(IdentifiedRasterAdapter.class, new RastersComparator(), onItemClickListener);
  }

  @Override
  protected SimpleDrawerViewHolder createNewViewHolder(View v, int viewType) {
    return new SimpleDrawerViewHolder(v);
  }

  @Override
  protected void bindItemToView(SimpleDrawerViewHolder holder, IdentifiedRasterAdapter data) {
    holder.setText(data.getName());
    holder.setTextColor(getTextColor(data));
  }

  @Override
  protected int getListItemLayout(int viewType) {
    return R.layout.drawer_list_item;
  }

  private int getTextColor(IdentifiedRasterAdapter data) {
    if (data.isShown()) {
      return R.color.colorPrimaryDark;
    }
    return R.color.secondaryText;
  }

  public static class IdentifiedRasterAdapter extends IntermediateRasterPresentation
      implements IdentifiedData {

    public IdentifiedRasterAdapter(IntermediateRasterPresentation raster) {
      super(raster.getName(), raster.getLocalUri(), raster.isShown());
    }

    @Override
    public String getId() {
      return getName();
    }
  }

  private static class RastersComparator implements Comparator<IdentifiedRasterAdapter> {
    @Override
    public int compare(IdentifiedRasterAdapter lhs, IdentifiedRasterAdapter rhs) {
      return lhs.getName().compareTo(rhs.getName());
    }
  }
}
