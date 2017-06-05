package com.teamagam.gimelgimel.app.common.base.adapters;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.teamagam.gimelgimel.domain.messages.IdentifiedData;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseRecyclerArrayAdapter
    <VIEW_HOLDER extends BaseRecyclerViewHolder<DATA>, DATA extends IdentifiedData>
    extends RecyclerView.Adapter<VIEW_HOLDER> {

  private final OnItemClickListener<DATA> mOnItemClickListener;
  private final SortedList<DATA> mSortedList;
  private final Map<String, DATA> mDataById;

  private OnNewDataListener<DATA> mOnNewDataListener;

  public BaseRecyclerArrayAdapter(Class<DATA> klass, Comparator<DATA> dataComparator,
      OnItemClickListener<DATA> onItemClickListener) {
    mSortedList = new SortedList<>(klass, new SortedListCallback<>(dataComparator));
    mOnItemClickListener = onItemClickListener;
    mDataById = new HashMap<>();
  }

  @Override
  public VIEW_HOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(getListItemLayout(viewType), parent, false);
    return createNewViewHolder(view, viewType);
  }

  @Override
  public int getItemCount() {
    return mSortedList.size();
  }

  @Override
  public void onBindViewHolder(VIEW_HOLDER viewHolder, int position) {
    final DATA data = mSortedList.get(position);
    bindItemToView(viewHolder, data);
    bindOnClickListener(viewHolder, data);
  }

  public synchronized void show(DATA data) {
    if (isNewData(data)) {
      insertNewItem(data);
      mOnNewDataListener.onNewData(data);
    } else {
      updateItem(data);
    }
  }

  public int getItemPosition(String messageId) {
    return mSortedList.indexOf(mDataById.get(messageId));
  }

  public void setOnNewDataListener(OnNewDataListener<DATA> onNewDataListener) {
    mOnNewDataListener = onNewDataListener;
  }

  protected abstract VIEW_HOLDER createNewViewHolder(View v, int viewType);

  protected abstract void bindItemToView(VIEW_HOLDER holder, DATA data);

  protected abstract int getListItemLayout(int viewType);

  protected DATA get(int position) {
    return mSortedList.get(position);
  }

  protected DATA getById(String id) {
    return mDataById.get(id);
  }

  private boolean isNewData(DATA data) {
    return !mDataById.containsKey(data.getId());
  }

  private void insertNewItem(DATA data) {
    mSortedList.add(data);
    mDataById.put(data.getId(), data);
  }

  private void updateItem(DATA updatedData) {
    DATA oldData = mDataById.get(updatedData.getId());
    mSortedList.updateItemAt(mSortedList.indexOf(oldData), updatedData);
    mDataById.put(updatedData.getId(), updatedData);
  }

  private void bindOnClickListener(VIEW_HOLDER viewHolder, final DATA data) {
    viewHolder.itemView.setOnClickListener(v -> mOnItemClickListener.onListItemInteraction(data));
  }

  public interface OnItemClickListener<DATA> {
    void onListItemInteraction(DATA item);
  }

  public interface OnNewDataListener<DATA> {
    void onNewData(DATA data);
  }

  private class SortedListCallback<D extends IdentifiedData> extends SortedList.Callback<D> {

    private final Comparator<D> mDataComparator;

    SortedListCallback(Comparator<D> dataComparator) {
      mDataComparator = dataComparator;
    }

    @Override
    public int compare(D o1, D o2) {
      return mDataComparator.compare(o1, o2);
    }

    @Override
    public void onChanged(int position, int count) {
      notifyItemChanged(position, count);
    }

    @Override
    public boolean areContentsTheSame(D oldItem, D newItem) {
      return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areItemsTheSame(D item1, D item2) {
      return item1.getId().equals(item2.getId());
    }

    @Override
    public void onInserted(int position, int count) {
      notifyItemRangeInserted(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
      notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
      notifyItemMoved(fromPosition, toPosition);
    }
  }
}
