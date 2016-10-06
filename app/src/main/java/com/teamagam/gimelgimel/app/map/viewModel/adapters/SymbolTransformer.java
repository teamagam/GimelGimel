package com.teamagam.gimelgimel.app.map.viewModel.adapters;

import android.content.Context;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PointSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.Symbol;
import com.teamagam.gimelgimel.app.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Symbol transformer.
 */
@PerActivity
public class SymbolTransformer {

    private static final String IMAGE_TYPE = "Image";
    private static final String USER_LOCATION_STALE_TYPE = "User_Stale";
    private static final String USER_LOCATION_ACTIVE_TYPE = "User_Active";

    private final Map<String, String> mEntityTypeToMarkerUrl;
    private String mImageMarkerUrl;

    @Inject
    public SymbolTransformer(Context context) {
        mEntityTypeToMarkerUrl = new HashMap<>();
        initEntityMarkersMap(context);
        initImageMarkerPath(context);
    }

    private void initImageMarkerPath(Context context) {
        mImageMarkerUrl = context.getString(R.string.geo_locations_marker_image);
    }

    private void initEntityMarkersMap(Context context) {
        String[] entityTypes = context.getResources().getStringArray(R.array.geo_locations_types);
        String[] entityMarkersUrl = context.getResources().getStringArray(R.array.geo_locations_markers_matched_types);

        for (int i = 0; i < entityTypes.length; i++) {
            mEntityTypeToMarkerUrl.put(entityTypes[i], entityMarkersUrl[i]);
        }
    }

    public Symbol transform(com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol
                                    pointSymbol) {
        switch (pointSymbol.getType()) {
            case IMAGE_TYPE:
                return createImageEntitySymbol();
            case USER_LOCATION_STALE_TYPE:
                return createStaleUserLocationSymbol(pointSymbol.getText());
            case USER_LOCATION_ACTIVE_TYPE:
                return createActiveUserLocationSymbol(pointSymbol.getText());
            default:
                return createGeoEntitySymbol(pointSymbol.getType());
        }
    }

    private PointSymbol createGeoEntitySymbol(String type) {
        String symbolPath = mEntityTypeToMarkerUrl.get(type);
        return createImageSymbolFromPath(symbolPath);
    }

    private PointSymbol createImageEntitySymbol() {
        return createImageSymbolFromPath(mImageMarkerUrl);
    }


    private PointSymbol createImageSymbolFromPath(String symbolPath) {
        return new PointImageSymbol(
                symbolPath,
                36, 36);
    }

//    private boolean isStale(long userLocationAgeMillis) {
//        return userLocationAgeMillis < Constants.USER_LOCATION_STALE_THRESHOLD_MS;
//    }

    private Symbol createActiveUserLocationSymbol(String userId) {
        return new PointTextSymbol(Constants.ACTIVE_USER_LOCATION_PIN_CSS_COLOR,
                userId, Constants.USER_LOCATION_PIN_SIZE_PX);
    }

    private Symbol createStaleUserLocationSymbol(String userId) {
        return new PointTextSymbol(Constants.STALE_USER_LOCATION_PIN_CSS_COLOR,
                userId,
                Constants.USER_LOCATION_PIN_SIZE_PX);
    }
}
