package com.teamagam.gimelgimel.app.view.view.fragments;

//TODO: clean
//import entity


/**
 * A fragment representing a list of Favorite gimels.
 */
public class GGFavoriteFragment extends GGFragment {

    @Override
    protected String getFragmentSelection() {
        //TODO: clean
        return null;
//        return GGEntity.DB.IS_FAVORITE + "=?";
    }

    @Override
    protected String[] getFragmentSelectionArgs() {
        // "1" - represents true in ContentProvider
        return new String[]{"1"};
    }

    @Override
    public int getTitle() {
        //TODO: clean
        return -1;
//        return R.string.fragment_gimel_favorite_title;
    }
}
