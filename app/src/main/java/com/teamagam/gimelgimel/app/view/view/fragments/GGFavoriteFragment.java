package com.teamagam.gimelgimel.app.view.view.fragments;

import com.tipz.app.R;
import com.tipz.app.model.entities.TipEntity;

/**
 * A fragment representing a list of Favorite tips.
 */
public class GGFavoriteFragment extends GGFragment {

    @Override
    protected String getFragmentSelection() {
        return TipEntity.DB.IS_FAVORITE + "=?";
    }

    @Override
    protected String[] getFragmentSelectionArgs() {
        // "1" - represents true in ContentProvider
        return new String[]{"1"};
    }

    @Override
    public int getTitle() {
        return R.string.fragment_tips_favorite_title;
    }
}
