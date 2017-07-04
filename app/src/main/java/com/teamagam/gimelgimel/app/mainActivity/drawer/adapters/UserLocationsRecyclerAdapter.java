package com.teamagam.gimelgimel.app.mainActivity.drawer.adapters;

import android.view.View;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerArrayAdapter;
import com.teamagam.gimelgimel.app.mainActivity.drawer.SimpleDrawerViewHolder;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.messages.IdentifiedData;
import java.util.Comparator;

public class UserLocationsRecyclerAdapter extends
    BaseRecyclerArrayAdapter<SimpleDrawerViewHolder, UserLocationsRecyclerAdapter.UserLocationAdapter> {

  public UserLocationsRecyclerAdapter(OnItemClickListener<UserLocationAdapter> onItemClickListener) {
    super(UserLocationAdapter.class, new UserLocationComparator(), onItemClickListener);
  }

  @Override
  protected SimpleDrawerViewHolder createNewViewHolder(View v, int viewType) {
    return new SimpleDrawerViewHolder(v);
  }

  @Override
  protected void bindItemToView(SimpleDrawerViewHolder holder, UserLocationAdapter userLocation) {
    holder.setText(userLocation.getUser());
    holder.setTextColor(getTextColor(userLocation));
  }

  @Override
  protected int getListItemLayout(int viewType) {
    return R.layout.drawer_list_item;
  }

  private int getTextColor(UserLocationAdapter userLocation) {
    if (userLocation.isActive()) {
      return R.color.colorPrimaryDark;
    }
    return R.color.themeRed;
  }

  public static class UserLocationAdapter extends UserLocation implements IdentifiedData {

    public UserLocationAdapter(UserLocation userLocation) {
      super(userLocation.getUser(), userLocation.getLocationSample());
    }

    @Override
    public String getId() {
      return getUser();
    }
  }

  private static class UserLocationComparator implements Comparator<UserLocationAdapter> {
    @Override
    public int compare(UserLocationAdapter lhs, UserLocationAdapter rhs) {
      if (lhs.getUser().equalsIgnoreCase(rhs.getUser())) {
        return 0;
      }
      if (bothActive(lhs, rhs)) {
        return compareLexicographically(lhs, rhs);
      }
      return compareByAge(lhs, rhs);
    }

    private boolean bothActive(UserLocationAdapter lhs, UserLocationAdapter rhs) {
      return lhs.isActive() && rhs.isActive();
    }

    private int compareLexicographically(UserLocationAdapter lhs, UserLocationAdapter rhs) {
      return lhs.getUser().compareTo(rhs.getUser());
    }

    private int compareByAge(UserLocationAdapter lhs, UserLocationAdapter rhs) {
      return lhs.getLocationSample().getAgeMillis() < rhs.getLocationSample().getAgeMillis() ? -1
          : 1;
    }
  }
}
