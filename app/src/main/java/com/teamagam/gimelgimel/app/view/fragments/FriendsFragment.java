package com.teamagam.gimelgimel.app.view.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.entities.FriendsEntity;
import com.teamagam.gimelgimel.app.view.adapters.FriendsAdapter;
import com.teamagam.gimelgimel.helpers_autodesk.view.BaseDataFragment;

import java.util.ArrayList;

/**
 * A fragment representing a list of friends.
 */
public class FriendsFragment extends BaseDataFragment<GGApplication, FriendsEntity> {

    /**
     * The fragment's RecyclerView to display friends data
     */
    private RecyclerView mRecyclerView;

    /**
     * The Adapter which will be used to populate the RecyclerView
     */
    private FriendsAdapter mAdapter;

    private ArrayList<FriendsEntity> mArryList;
    private LinearLayoutManager mLayoutManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendsFragment() {
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_friends;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Save reference to the recycler view
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.fragment_friends_recycler_view);

        // In contrast to other adapter-backed views such as ListView or GridView - RecyclerView
        // allows client code to provide custom layout arrangements for child views.
        // These arrangements are controlled by the RecyclerView.LayoutManager.
        // A LayoutManager must be provided for RecyclerView to function.
        // (https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html)
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Create the adapter and attach to recycler view
        mArryList = new ArrayList<>();
        mAdapter = new FriendsAdapter(mArryList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onNewData(Cursor cursor) {

        // Remove previous sender
        mArryList.clear();

        // If the cursor has values
        if (cursor != null && cursor.moveToFirst()) {

            FriendsEntity currTip;

            // Run on the cursor and extract data for each gimel
            do {
                // Create the tip and extract data
                currTip = new FriendsEntity(cursor);
                mArryList.add(currTip);
            } while (cursor.moveToNext());
        }

        // Just notify the adapter that data has changed,
        // remember the data is already referenced inside the adapter
        mAdapter.notifyDataSetChanged();

    }

    @Override
    protected Uri getFragmentDataUri() {
        return FriendsEntity.CONTENT_URI;
    }

    @Override
    public int getTitle() {
        return R.string.fragment_friends_title;
    }
}
