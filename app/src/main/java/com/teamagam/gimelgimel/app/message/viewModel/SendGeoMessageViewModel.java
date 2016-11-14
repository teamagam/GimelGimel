package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
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


    private int mTypeIdx;

    private ISendGeoMessageView mView;

    @Inject
    Context context;

    @Inject
    SendGeoMessageInteractorFactory mInteractorFactory;
    private PointGeometryApp mPoint;
    private String mText;
    private String mType;

    @Inject
    public SendGeoMessageViewModel() {
        super();
    }

    public void init(ISendGeoMessageView view,
                     PointGeometryApp point) {
        mTypes = context.getResources().getStringArray(R.array.geo_locations_types);
        mPoint = point;
        mView = view;
    }

    public void onClickedOK() {
        executeInteractor(
                mText,
                mPoint,
                mType);

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
        return mText == null || mText.isEmpty();
    }

    public PointGeometryApp getPoint() {
        return mPoint;
    }

    public void setText(String text) {
        mText = text;
        notifyPropertyChanged(BR.inputNotValid);
    }

    public String getText() {
        return mText;
    }

    public String[] getTypes() {
        return mTypes;
    }

    public void setTypeIdx(int typeId) {
        mType = mTypes[typeId];
        mTypeIdx = typeId;
    }

    public int getTypeIdx() {
        return mTypeIdx;
    }

    public interface ISendGeoMessageView {

        void dismiss();
    }
}
