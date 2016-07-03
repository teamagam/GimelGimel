package com.teamagam.gimelgimel.app.control.content_provider;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.control.db.GGDB;
import com.teamagam.gimelgimel.helpers_autodesk.control.content_provider.BaseContentProvider;

public class GGContentProvider extends BaseContentProvider {

    /***
     * This represents the authority of the content provider, and it is being generated
     * dynamically to support multiple application build variants
     */
    public static String authority;

    @Override
    public boolean onCreate() {
        authority = getAuthority(getContext());
        return super.onCreate();
    }

    @Override
    protected SQLiteOpenHelper createSQLiteOpenHelper() {
        return new GGDB(this.getContext(), BuildConfig.VERSION_CODE);
    }

    @Override
    protected Cursor querySpecific(SQLiteDatabase db, int match, Uri uri, String[] projection,
                                   String selection, String[] selectionArgs, String sortOrder) {
        // We don't need any specific query
        return null;
    }

}