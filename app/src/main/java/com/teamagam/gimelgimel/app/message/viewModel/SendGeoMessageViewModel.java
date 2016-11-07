package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.message.model.contents.GeoContentApp;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractorFactory;

import javax.inject.Inject;


/**
 * View Model that handles send geographic messages from user in
 * {@link SendGeoMessageViewModel}.
 * <p>
 * Controls communication between presenter and view.
 */
public class SendGeoMessageViewModel extends BaseObservable {

    private Logger sLogger = LoggerFactory.create(this.getClass());

    public String[] mTypes;

    GeoContentApp mGeoContent;

    private int mTypeIdx;

    private ISendGeoMessageView mView;

    @Inject
    Context context;

    @Inject
    SendGeoMessageInteractorFactory mInteractorFactory;

    @Inject
    public SendGeoMessageViewModel() {
        super();
    }

    public void init(ISendGeoMessageView view,
                     PointGeometryApp point) {
        mTypes = context.getResources().getStringArray(R.array.geo_locations_types);
        mGeoContent = new GeoContentApp(point);
        mView = view;
    }

    public void onClickedOK() {

        executeInteractor(
                mGeoContent.getText(),
                mGeoContent.getPointGeometry(),
                mGeoContent.getType());

        mView.dismiss();
    }

    private void executeInteractor(String messageText, PointGeometryApp point, String type) {
        com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry geometry =
                new com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry(point.latitude,
                        point.longitude, point.altitude);

        SendGeoMessageInteractor interactor = mInteractorFactory.create(messageText, geometry,
                type);

        interactor.execute();
    }

    @Bindable
    public boolean isInputNotValid() {
        String text = mGeoContent.getText();
        return text == null || text.isEmpty();
    }

    public PointGeometryApp getPoint() {
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
