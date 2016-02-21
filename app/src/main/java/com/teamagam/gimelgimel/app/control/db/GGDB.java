package com.teamagam.gimelgimel.app.control.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.teamagam.gimelgimel.app.model.entities.FriendEntity;
import com.teamagam.gimelgimel.helpers_autodesk.control.database.DbEntity;
import com.teamagam.gimelgimel.helpers_autodesk.control.database.network.DbEntityHelper;

import java.util.ArrayList;

/***
 * Manages the creation, upgrades and instantiation of the GG Database
 */
public class GGDB extends DbEntityHelper {

    public GGDB(Context context, int dbVersion) {
        super(context, "GGDB", dbVersion);
    }

    @Override
    public ArrayList<DbEntity> getContracts() {
        ArrayList<DbEntity> contracts = new ArrayList<>();
        contracts.add(new FriendEntity());
        return contracts;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing here yet
    }
}
