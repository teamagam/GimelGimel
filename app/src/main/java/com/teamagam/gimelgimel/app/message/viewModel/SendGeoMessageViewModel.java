package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.model.entities.GeoContent;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.presentation.presenters.SendGeoMessagePresenter;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import javax.inject.Inject;


/**
 * View Model that handles send geographic messages from user in
 * {@link SendGeoMessageViewModel}.
 * <p>
 * Controls communication between presenter and view.
 */
@PerFragment
public class SendGeoMessageViewModel extends BaseObservable {

    private Logger sLogger = LoggerFactory.create(this.getClass());

    public String[] mTypes;

    GeoContent mGeoContent;

    private int mTypeIdx;

    private ISendGeoMessageView mView;

    @Inject
    SendGeoMessagePresenter mPresenter;

    @Inject
    Context context;

    @Inject
    public SendGeoMessageViewModel() {
        super();
    }

    public void init(ISendGeoMessageView view, PointGeometry point) {
        this.mTypes = context.getResources().getStringArray(R.array.geo_locations_types);
        mGeoContent = new GeoContent(point);
        this.mView = view;
    }

    public void onClickedOK() {
        mPresenter.sendGeoMessage(
                GGMessageSender.getUserName(context),
                mGeoContent.getText(),
                mGeoContent.getPointGeometry().latitude,
                mGeoContent.getPointGeometry().longitude,
                mGeoContent.getPointGeometry().altitude,
                mGeoContent.getType());
        mView.dismiss();
    }

    @Bindable
    public boolean isInputNotValid() {
        String text = mGeoContent.getText();
        return text == null || text.isEmpty();
    }

    public PointGeometry getPoint() {
        return mGeoContent.getPointGeometry();
    }

    public void setText(String text) {
        mGeoContent.setText(text);
        notifyPropertyChanged(BR.inputNotValid);
    }

    public String getText() {
        return mGeoContent.getText();
    }

    public String[] getTypes() {
        return mTypes;
    }

    public void setTypeIdx(int type) {
        mGeoContent.setType(mTypes[type]);
        mTypeIdx = type;
    }

    public int getTypeIdx() {
        return mTypeIdx;
    }

    public interface ISendGeoMessageView {

        void dismiss();
    }

}
