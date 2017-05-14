package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.esri.EsriUtils;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractorFactory;

import javax.inject.Inject;

import static com.teamagam.gimelgimel.data.config.Constants.USE_UTM;

public class SendGeoMessageViewModel extends SendMessageViewModel {

    @Inject
    Context mContext;
    @Inject
    SendGeoMessageInteractorFactory mInteractorFactory;

    private String[] mTypes;
    private int mTypeIdx;
    private PointGeometryApp mPoint;

    @Inject
    public SendGeoMessageViewModel() {
        super();
    }

    public void init(IViewDismisser view,
                     PointGeometryApp point) {
        mTypes = mContext.getResources().getStringArray(R.array.geo_location_types);
        mPoint = point;
        mView = view;
    }

    public PointGeometryApp getPoint() {
        return mPoint;
    }

    public String getFormattedPoint() {
        if (USE_UTM) {
            PointGeometry point = new PointGeometry(mPoint.latitude, mPoint.longitude);
            PointGeometry utmPoint = EsriUtils.projectDomainPoint(
                    point, EsriUtils.WGS_84_GEO, EsriUtils.ED50_UTM_36_N);
            return mContext.getString(
                    R.string.utm_36N_format, utmPoint.getLongitude(), utmPoint.getLatitude());
        } else {
            return mContext.getString(R.string.geo_dd_format, mPoint.latitude, mPoint.longitude);
        }
    }

    public String[] getTypes() {
        return mTypes;
    }

    public int getTypeIdx() {
        return mTypeIdx;
    }

    public void setTypeIdx(int typeId) {
        mTypeIdx = typeId;
    }

    @Override
    protected void executeInteractor() {
        com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry pointGeometry =
                new com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry(
                        mPoint.latitude,
                        mPoint.longitude, mPoint.altitude);

        SendGeoMessageInteractor interactor = mInteractorFactory.create(mText, pointGeometry,
                getMessageType());

        interactor.execute();
    }

    private String getMessageType() {
        switch (mTypeIdx) {
            case 0:
                return PointSymbol.POINT_TYPE_BUILDING;
            case 1:
                return PointSymbol.POINT_TYPE_ENEMY;
            default:
                return PointSymbol.POINT_TYPE_GENERAL;
        }
    }
}
