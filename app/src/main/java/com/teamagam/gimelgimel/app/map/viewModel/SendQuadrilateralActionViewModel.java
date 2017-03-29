package com.teamagam.gimelgimel.app.map.viewModel;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.view.LongLatPicker;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.SendQuadrilateralActionFragment;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

import java.util.ArrayList;
import java.util.List;

@AutoFactory
public class SendQuadrilateralActionViewModel extends BaseMapViewModel<SendQuadrilateralActionFragment> {

    private static final String EMPTY_STRING = "";

    private final SendGeoMessageInteractorFactory mSendGeoMessageInteractorFactory;
    private final GGMapView mGGMapView;
    private final LongLatPicker[] mPickers;

    private PolygonEntity mPolygonEntity;

    SendQuadrilateralActionViewModel(
            @Provided DisplayMapEntitiesInteractorFactory mapEntitiesInteractorFactory,
            @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
            @Provided DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
            @Provided SendGeoMessageInteractorFactory sendGeoMessageInteractorFactory,
            GGMapView ggMapView,
            SendQuadrilateralActionFragment view,
            LongLatPicker[] pickers) {
        super(mapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
                displayIntermediateRastersInteractorFactory, ggMapView);
        mSendGeoMessageInteractorFactory = sendGeoMessageInteractorFactory;
        mGGMapView = ggMapView;
        mView = view;
        mPickers = pickers;
    }

    public void onSendClick() {
        if (!isFormFilled()) {
            mView.showInvalidInput();
        } else {
            sendPolygon();
            mView.finish();
        }
    }

    public void onShowButtonClick() {
        if (!isFormFilled()) {
            mView.showInvalidInput();
        } else {
            drawNewPolygon();
            centerMapOnPolygon();
            mView.hideKeyboard();
        }
    }

    private boolean isFormFilled() {
        return hasPoints(mPickers);
    }

    private boolean hasPoints(LongLatPicker... pickers) {
        for (LongLatPicker picker : pickers) {
            if (!picker.hasPoint()) {
                return false;
            }
        }
        return true;
    }

    private void sendPolygon() {
        mSendGeoMessageInteractorFactory
                .create(mView.getDescription(), createPolygon(), EMPTY_STRING)
                .execute();
    }

    private void drawNewPolygon() {
        clearOldPolygon();
        drawPolygon();
    }

    private void clearOldPolygon() {
        if (mPolygonEntity != null) {
            mGGMapView.updateMapEntity(GeoEntityNotification.createRemove(mPolygonEntity));
        }
    }

    private void drawPolygon() {
        mPolygonEntity = createPolygonEntity();
        mGGMapView.updateMapEntity(GeoEntityNotification.createAdd(mPolygonEntity));
    }

    private PolygonEntity createPolygonEntity() {
        return new PolygonEntity(EMPTY_STRING, mView.getDescription(), createPolygon(),
                createSymbol());
    }

    private Polygon createPolygon() {
        List<PointGeometry> pgs = new ArrayList<>(4);
        for (LongLatPicker mPicker : mPickers) {
            pgs.add(mPicker.getPoint());
        }
        return new Polygon(pgs);
    }

    private PolygonSymbol createSymbol() {
        return new PolygonSymbol(false);
    }

    private void centerMapOnPolygon() {
        mGGMapView.lookAt(mPolygonEntity.getGeometry());
    }
}