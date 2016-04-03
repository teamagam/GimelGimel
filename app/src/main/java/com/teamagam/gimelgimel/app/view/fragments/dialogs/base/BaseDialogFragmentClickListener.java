package com.teamagam.gimelgimel.app.view.fragments.dialogs.base;

/**
 * Generic click listener for dialog fragments
 */
public interface BaseDialogFragmentClickListener<DataType> {

    void onOk(DataType data);

    void onCancel(DataType data);

    void onNeutral(DataType data);
}
