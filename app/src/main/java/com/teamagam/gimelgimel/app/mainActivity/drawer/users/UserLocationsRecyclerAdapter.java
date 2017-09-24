package com.teamagam.gimelgimel.app.mainActivity.drawer.users;

import android.view.View;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.adapters.BaseRecyclerArrayAdapter;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.base.repository.IdentifiedData;
import java.util.Comparator;

public class UserLocationsRecyclerAdapter extends
    BaseRecyclerArrayAdapter<UserLocationViewHolder, UserLocationsRecyclerAdapter.UserLocationAdapter> {

  public UserLocationsRecyclerAdapter(OnItemClickListener<UserLocationAdapter> onItemClickListener) {
    super(UserLocationAdapter.class, new UserLocationComparator(), onItemClickListener);
  }

  @Override
  protected UserLocationViewHolder createNewViewHolder(View v, int viewType) {
    return new UserLocationViewHolder(v);
  }

  @Override
  protected void bindItemToView(UserLocationViewHolder holder, UserLocationAdapter userLocation) {
    holder.bind(userLocation);
  }

  @Override
  protected int getListItemLayout(int viewType) {
    return R.layout.drawer_user_item_name;
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
