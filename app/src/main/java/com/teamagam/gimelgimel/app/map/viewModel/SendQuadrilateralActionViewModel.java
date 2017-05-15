package com.teamagam.gimelgimel.app.map.viewModel;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
import com.teamagam.gimelgimel.domain.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

@AutoFactory
public class SendQuadrilateralActionViewModel extends BaseMapViewModel<SendQuadrilateralActionFragment> {

    private static final String EMPTY_STRING = "";
    public static final String QUADRILATERAL_LONG_PREF = "quadrilateralLong";
    public static final String QUADRILATERAL_LAT_PREF = "quadrilateralLat";
    private static final String QUADRILATERAL_DESC_PREF = "quadrilateralDesc";

    private final SendGeoMessageInteractorFactory mSendGeoMessageInteractorFactory;
    private final GGMapView mGGMapView;
    private final LongLatPicker[] mPickers;

    private PolygonEntity mPolygonEntity;
    private SharedPreferences mDefaultSharedPreferences;

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
        mDefaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                mView.getContext());
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

    public void onRestoreValuesClick() {
        restoreLongLatValues();
        restoreDescriptionValue();
    }

    @Override
    public void stop() {
        super.stop();
        if (hasDescription() || hasAtLeastOnePoint()) {
            saveLongLatValues();
            saveDescription();
        }
    }

    private boolean isFormFilled() {
        return hasPoints(mPickers) && hasDescription();
    }

    private boolean hasPoints(LongLatPicker... pickers) {
        for (LongLatPicker picker : pickers) {
            if (!picker.hasPoint()) {
                return false;
            }
        }
        return true;
    }

    private boolean hasDescription() {
        return !TextUtils.isOnlyWhiteSpaces(mView.getDescription());
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
        List<PointGeometry> pgs = new ArrayList<>(mPickers.length);
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

    private void restoreLongLatValues() {
        PointGeometry[] points = loadLongLatValues();
        for (int i = 0; i < mPickers.length; i++) {
            mPickers[i].setPoint(points[i]);
        }
    }

    private PointGeometry[] loadLongLatValues() {
        PointGeometry[] values = new PointGeometry[mPickers.length];
        for (int i = 0; i < mPickers.length; i++) {
            values[i] = loadLongLatValue(i);
        }
        return values;
    }

    private PointGeometry loadLongLatValue(int pickerIndex) {
        double lat = mDefaultSharedPreferences.getFloat(QUADRILATERAL_LAT_PREF + pickerIndex, 0);
        lat = nanToZero(lat);
        double lng = mDefaultSharedPreferences.getFloat(QUADRILATERAL_LONG_PREF + pickerIndex, 0);
        lng = nanToZero(lng);
        return new PointGeometry(lat, lng);
    }

    private double nanToZero(double lat) {
        if (Double.isNaN(lat)) {
            lat = 0;
        }
        return lat;
    }

    private void restoreDescriptionValue() {
        String description = mDefaultSharedPreferences.getString(QUADRILATERAL_DESC_PREF, "");
        mView.setDescription(description);
    }

    private boolean hasAtLeastOnePoint() {
        for (LongLatPicker mPicker : mPickers) {
            if (mPicker.hasPoint()) {
                return true;
            }
        }
        return false;
    }

    private void saveLongLatValues() {
        SharedPreferences.Editor prefEditor = mDefaultSharedPreferences.edit();
        for (int i = 0; i < mPickers.length; i++) {
            saveLongLat(prefEditor, mPickers[i], i);
        }
        prefEditor.apply();
    }

    private void saveLongLat(SharedPreferences.Editor prefEditor, LongLatPicker picker,
                             int pickerIndex) {
        PointGeometry point = picker.getPoint();
        prefEditor
                .putFloat(QUADRILATERAL_LONG_PREF + pickerIndex, (float) point.getLongitude())
                .putFloat(QUADRILATERAL_LAT_PREF + pickerIndex, (float) point.getLatitude())
                .apply();
    }

    private void saveDescription() {
        mDefaultSharedPreferences.edit()
                .putString(QUADRILATERAL_DESC_PREF, mView.getDescription())
                .apply();
    }
}