package com.teamagam.gimelgimel.app.dynamic_layer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseViewModelFragment;
import com.teamagam.gimelgimel.databinding.FragmentDynamicLayerDetailsBinding;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class DynamicLayerDetailsFragment
    extends BaseViewModelFragment<DynamicLayerDetailsViewModel> {
  private static final String ARG_LAYER_ID = "layer_id";
  private static final String ARG_ENTITY_ID = "entity_id";

  @BindView(R.id.dynamic_layer_details_list)
  RecyclerView mMasterRecyclerView;

  @Inject
  DynamicLayerDetailsViewModelFactory mDynamicLayerDetailsViewModelFactory;

  private String mDynamicLayerId;
  private String mDynamicEntityId;

  private OnDynamicEntityClickedListener mListener;
  private SimpleStringRecyclerViewAdapter mAdapter;
  private DynamicLayerDetailsViewModel mViewModel;

  public DynamicLayerDetailsFragment() {
    // Required empty public constructor
  }

  public static DynamicLayerDetailsFragment newInstance(String dynamicLayerId,
      String dynamicEntityId) {
    DynamicLayerDetailsFragment fragment = new DynamicLayerDetailsFragment();
    Bundle args = new Bundle();
    args.putString(ARG_LAYER_ID, dynamicLayerId);
    args.putString(ARG_ENTITY_ID, dynamicEntityId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mDynamicLayerId = getArguments().getString(ARG_LAYER_ID);
      mDynamicEntityId = getArguments().getString(ARG_ENTITY_ID);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    ((GGApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
    initViewModel(view);
    initDetailsRecyclerView();
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    mListener = getListener(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public DynamicLayerDetailsViewModel getViewModel() {
    return mViewModel;
  }

  @Override
  protected DynamicLayerDetailsViewModel getSpecificViewModel() {
    return mViewModel;
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_dynamic_layer_details;
  }

  private void initViewModel(View view) {
    mViewModel =
        mDynamicLayerDetailsViewModelFactory.create(new RecyclerViewMasterListCallback(), mListener,
            new ResourcesDetailsStringProvider(), mDynamicLayerId, mDynamicEntityId);
    FragmentDynamicLayerDetailsBinding binding = FragmentDynamicLayerDetailsBinding.bind(view);
    binding.setViewModel(mViewModel);
  }

  private void initDetailsRecyclerView() {
    mMasterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mAdapter = new SimpleStringRecyclerViewAdapter();
    mMasterRecyclerView.setAdapter(mAdapter);
  }

  private OnDynamicEntityClickedListener getListener(Context context) {
    Fragment parentFragment = getParentFragment();
    if (parentFragment instanceof OnDynamicEntityClickedListener) {
      return (OnDynamicEntityClickedListener) parentFragment;
    } else if (context instanceof OnDynamicEntityClickedListener) {
      return (OnDynamicEntityClickedListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " or the parent fragment ["
          + parentFragment
          + "] must implement OnDynamicEntityClickedListener");
    }
  }

  public interface OnDynamicEntityClickedListener {

    void onDynamicEntityListingClicked(DynamicEntity dynamicEntity);

    void onDynamicLayerListingClicked(DynamicLayer dynamicLayer);
  }

  static class SimpleTextViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.dynamic_layer_details_item_text)
    TextView mTextView;

    @BindView(R.id.dynamic_layer_details_item_layout)
    ViewGroup mLayout;

    public SimpleTextViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(String text, View.OnClickListener listener) {
      mTextView.setText(text);
      mLayout.setOnClickListener(listener);
      setBackgroundColor(R.color.transparent);
    }

    public void select() {
      setBackgroundColor(R.color.selected_list_item);
    }

    private void setBackgroundColor(int colorResId) {
      int bgColor = ContextCompat.getColor(mLayout.getContext(), colorResId);
      mTextView.setBackgroundColor(bgColor);
    }
  }

  private class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleTextViewHolder> {

    private final List<String> mData;
    private final List<View.OnClickListener> mOnClickListeners;
    private int mSelectedPosition;

    private SimpleStringRecyclerViewAdapter() {
      mData = new ArrayList<>();
      mOnClickListeners = new ArrayList<>();
    }

    @Override
    public SimpleTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.fragment_dynamic_layer_details_item, parent, false);
      return new SimpleTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleTextViewHolder holder, int position) {
      holder.bind(mData.get(position), mOnClickListeners.get(position));
      if (isSelected(position)) {
        holder.select();
      }
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    public synchronized void add(String text, View.OnClickListener listener) {
      int position = mData.size();
      mData.add(text);
      mOnClickListeners.add(view -> {
        select(position);
        listener.onClick(view);
      });
      notifyDataSetChanged();
    }

    public synchronized void clear() {
      mData.clear();
      mOnClickListeners.clear();
      mSelectedPosition = -1;
      notifyDataSetChanged();
    }

    public void select(String title) {
      int position = mData.indexOf(title);
      if (position != -1) {
        select(position);
      }
    }

    private void select(int position) {
      mSelectedPosition = position;
      notifyDataSetChanged();
      mMasterRecyclerView.smoothScrollToPosition(position);
    }

    private boolean isSelected(int position) {
      return position == mSelectedPosition;
    }
  }

  static class SimpleTextViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.dynamic_layer_details_item_text)
    TextView mTextView;

    @BindView(R.id.dynamic_layer_details_item_layout)
    ViewGroup mLayout;

    public SimpleTextViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(String text, View.OnClickListener listener) {
      mTextView.setText(text);
      mLayout.setOnClickListener(listener);
    }

    public void select() {
      int bgColor = ContextCompat.getColor(mLayout.getContext(), R.color.selected_list_item);
      mLayout.setBackgroundColor(bgColor);
    }
  }

  private class RecyclerViewMasterListCallback
      implements DynamicLayerDetailsViewModel.MasterListCallback {
    @Override
    public void addHeader(String title, View.OnClickListener listener) {
      mAdapter.add(title, listener);
    }

    @Override
    public void addDetails(String title, View.OnClickListener listener) {
      mAdapter.add(title, listener);
    }

    @Override
    public void select(String title) {
      mAdapter.select(title);
    }

    @Override
    public void clear() {
      mAdapter.clear();
    }
  }

  private class ResourcesDetailsStringProvider
      implements DynamicLayerDetailsViewModel.DetailsStringProvider {
    @Override
    public String getDefaultDetails() {
      return getString(R.string.dynamic_layer_details_default);
    }

    @Override
    public String getLayerDetailsHeader(DynamicLayer dynamicLayer) {
      return getString(R.string.dynamic_layer_details_layer_title, dynamicLayer.getName());
    }

    @Override
    public String getEntityDetailsHeader(DynamicEntity dynamicEntity, int entityCount) {
      return getString(R.string.dynamic_layer_details_entity_title, entityCount);
    }
  }
}
