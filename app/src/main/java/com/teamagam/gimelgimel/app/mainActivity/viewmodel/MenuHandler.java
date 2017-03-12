package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import com.teamagam.gimelgimel.app.common.utils.IntegerIdGenerator;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivityDrawer;

public class MenuHandler {

    private final MainActivityDrawer mMainActivityDrawer;
    private final IntegerIdGenerator mIntegerIdGenerator;

    public MenuHandler(
            MainActivityDrawer mainActivityDrawer,
            IntegerIdGenerator integerIdGenerator) {
        mMainActivityDrawer = mainActivityDrawer;
        mIntegerIdGenerator = integerIdGenerator;
    }

    public void add(String id, String title, int submenuId) {
        int itemId = mIntegerIdGenerator.generate(id);
        mMainActivityDrawer.addToMenu(submenuId, itemId, title);
    }

    public void update(String id, String title) {
        int itemId = mIntegerIdGenerator.get(id);
        mMainActivityDrawer.updateMenu(title, itemId);
    }

    public boolean isNew(String id) {
        return !mIntegerIdGenerator.contains(id);
    }

    public void setChecked(String id, boolean isChecked) {
        int itemId = mIntegerIdGenerator.get(id);
        mMainActivityDrawer.setChecked(isChecked, itemId);
    }
}