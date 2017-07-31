package com.teamagam.gimelgimel.app.mainActivity.drawer;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.ActivitySubcomponent;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.databinding.ActivityMainDrawerBinding;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import devlight.io.library.ntb.NavigationTabBar;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class MainActivityDrawer extends ActivitySubcomponent {

  @Inject
  DrawerViewModelFactory mDrawerViewModelFactory;
  @Inject
  UserPreferencesRepository mUserPreferencesRepository;

  @BindView(R.id.drawer_navigation_tab_bar)
  NavigationTabBar mNavigationTabBar;

  @BindView(R.id.drawer_content_layout)
  LinearLayout mDrawerContentLayout;

  @BindView(R.id.drawer_content_recycler)
  RecyclerView mRecyclerView;

  @BindView(R.id.drawer_rasters_tree_container)
  ViewGroup mLayersTreeContainer;

  private DrawerViewModel mViewModel;
  private MainActivity mMainActivity;
  private TreeNode mLayersTreeRoot;
  private AndroidTreeView mAndroidTreeView;

  public MainActivityDrawer(MainActivity activity) {
    mMainActivity = activity;

    mMainActivity.getMainActivityComponent().inject(this);
    ButterKnife.bind(this, activity);

    setupActionBar();
    setupLayersTree();
    initViewModel();
    setupNavTabBar();

    mRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity));
  }

  @Override
  public void onResume() {
    super.onResume();
    mViewModel.resume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mViewModel.pause();
  }

  private void setupActionBar() {
    mMainActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private void initViewModel() {
    mViewModel = mDrawerViewModelFactory.create(mMainActivity, new AndroidTreeViewNodeDisplayer());
    bindViewModel();
    mViewModel.setView(this);
    mViewModel.start();
    mRecyclerView.setAdapter(mViewModel.getUsersAdapter());
  }

  private void bindViewModel() {
    ActivityMainDrawerBinding binding = ActivityMainDrawerBinding.bind(mDrawerContentLayout);
    binding.setViewModel(mViewModel);
  }

  private void setupNavTabBar() {
    mNavigationTabBar.setModels(mViewModel.getTabModels());
    mNavigationTabBar.setOnTabBarSelectedIndexListener(
        new NavigationTabBar.OnTabBarSelectedIndexListener() {
          @Override
          public void onStartTabSelected(NavigationTabBar.Model model, int index) {
            mViewModel.onNavigationTabBarSelected(index);
          }

          @Override
          public void onEndTabSelected(NavigationTabBar.Model model, int index) {

          }
        });
    mNavigationTabBar.setModelIndex(0);
  }

  private void setupLayersTree() {
    mLayersTreeRoot = TreeNode.root();
    mAndroidTreeView = new AndroidTreeView(mMainActivity, mLayersTreeRoot);
    mLayersTreeContainer.addView(mAndroidTreeView.getView());
    mAndroidTreeView.setDefaultViewHolder(SimpleTreeViewHolder.class);
    mAndroidTreeView.setDefaultAnimation(true);
  }

  private class AndroidTreeViewNodeDisplayer implements LayersNodeDisplayer {

    private Map<String, TreeNode> mIdToNodeMap = new HashMap<>();

    @Override
    public void addNode(Node node) {
      TreeNode treeNode = createTreeNode(node);
      mIdToNodeMap.put(node.getId(), treeNode);
      if (node.hasParent()) {
        TreeNode parentNode = mIdToNodeMap.get(node.getParentId());
        parentNode.addChild(treeNode);
      } else {
        mAndroidTreeView.addNode(mLayersTreeRoot, treeNode);
      }
    }

    @Override
    public void setNodeSelectionState(String nodeId, boolean isSelected) {
      //mIdToNodeMap.get(nodeId)
      //mAndroidTreeView.
      //TODO implement me
    }

    private TreeNode createTreeNode(Node node) {
      return new TreeNode(node);
    }
  }
}
