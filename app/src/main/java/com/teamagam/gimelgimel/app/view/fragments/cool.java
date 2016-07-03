package com.teamagam.gimelgimel.app.view.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

/**
 * Created by Yoni on 03 יולי 2016.
 */
public abstract class cool extends RecyclerView.ViewHolder {

    public cool(View itemView) {
        super(itemView);
    }

    abstract View bindView(Message msg);

    abstract void bindView(View view, Message msg);

    abstract int countTypeView();

    abstract int messageType(Message msg);

}
