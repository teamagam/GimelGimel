package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractorFactory;

import javax.inject.Inject;


/**
 * View Model that handles send geographic messages from user in
 * {@link SendGeoMessageViewModel}.
 * <p>
 * Controls communication between presenter and view.
 */
public class SendGeoMessageViewModel extends SendMessageViewModel {

    public String[] mTypes;
    @Inject
    Context context;
    @Inject
    SendGeoMessageInteractorFactory mInteractorFactory;
    private int mTypeIdx;
    private PointGeometryApp mPoint;
    private String mType;

    @Inject
    public SendGeoMessageViewModel() {
        super();
    }

    public void init(IViewDismisser view,
                     PointGeometryApp point) {
        mTypes = context.getResources().getStringArray(R.array.geo_locations_types);
        mPoint = point;
        mView = view;
    }

    @Override
    protected void executeInteractor() {
        com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry geometry =
                new com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry(mPoint.latitude,
                        mPoint.longitude, mPoint.altitude);

        SendGeoMessageInteractor interactor = mInteractorFactory.create(mText, geometry,
                mType);

        interactor.execute();
    }

    public PointGeometryApp getPoint() {
        return mPoint;
    }


    public String[] getTypes() {
        return mTypes;
    }

    public int getTypeIdx() {
        return mTypeIdx;
    }

    public void setTypeIdx(int typeId) {
        mType = mTypes[typeId];
        mTypeIdx = typeId;
    }
}
