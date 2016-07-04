package com.teamagam.gimelgimel.app.view.adapters;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.teamagam.gimelgimel.app.model.entities.FriendsEntity;
import com.teamagam.gimelgimel.helpers_autodesk.view.adapter.BaseRecyclerViewHolder;

import java.util.ArrayList;

public class FriendsAdapter extends BaseRecyclerArrayAdapter<FriendsAdapter.GGViewHolder, FriendsEntity> {

    /**
     * Construct an adapter with data in it
     *
     * @param data the data for the adapter to display
     */
    public FriendsAdapter(ArrayList<FriendsEntity> data) {
        super(data);
    }

    @Override
    protected Class getViewHolderClass() {
        return GGViewHolder.class;
    }

    @Override
    protected int getSingleItemLayoutRes() {
        //TODO: clean
        return -1;
//        return R.layout.item_gimel_gimel;
    }

    @Override
    public void onBindViewHolderToData(GGViewHolder holder, final FriendsEntity data) {
        // Bind the data to the view holder
        holder.title.setText(data.title);
        holder.favorite.setOnCheckedChangeListener(null);
        holder.favorite.setChecked(data.isFavorite);

        // Bind actions
        holder.favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setFavorite(buttonView.getContext().getContentResolver(), isChecked);
            }
        });
    }


    public static class GGViewHolder extends BaseRecyclerViewHolder {

        public GGViewHolder(View itemView) {
            super(itemView);
        }

//        TODO: clean
//        @ViewHolderBinder(resId = R.id.item_gimel_title)
        public TextView title;

//        TODO: clean
//        @ViewHolderBinder(resId = R.id.item_gimel_favorite)
        public CompoundButton favorite;
    }
}