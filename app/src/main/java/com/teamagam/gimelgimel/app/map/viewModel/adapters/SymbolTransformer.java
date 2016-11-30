package com.teamagam.gimelgimel.app.map.viewModel.adapters;

import android.content.Context;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PointSymbolApp;
import com.teamagam.gimelgimel.app.map.model.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.MyLocationSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * SymbolApp transformer.
 */
@PerActivity
public class SymbolTransformer {

    private static final int MY_LOCATION_MARKER_SIZE_PX = 32;

    private final Map<String, String> mEntityTypeToMarkerUrl;
    private String mImageMarkerUrl;
    private String mMyLocationMarkerUrl;

    @Inject
    public SymbolTransformer(Context context) {
        mEntityTypeToMarkerUrl = new HashMap<>();
        initMarkersUrls(context);
    }

    public SymbolApp transform(Symbol symbol) {
        return new SymbolToAppMapper().mapSymbol(symbol);
    }

    private void initMarkersUrls(Context context) {
        initEntityMarkersMap(context);
        initImageMarkerPath(context);
        initMyLocationMarkerUrl(context);
    }

    private void initMyLocationMarkerUrl(Context context) {
        mMyLocationMarkerUrl = context.getString(R.string.my_location_image_path);
    }

    private void initImageMarkerPath(Context context) {
        mImageMarkerUrl = context.getString(R.string.geo_locations_marker_image);
    }


    private void initEntityMarkersMap(Context context) {
        String[] entityTypes = context.getResources().getStringArray(R.array.geo_locations_types);
        String[] entityMarkersUrl = context.getResources().getStringArray(
                R.array.geo_locations_markers_matched_types);

        for (int i = 0; i < entityTypes.length; i++) {
            mEntityTypeToMarkerUrl.put(entityTypes[i], entityMarkersUrl[i]);
        }
    }

    private PointSymbolApp createImageSymbolFromPath(String symbolPath) {
        return new PointImageSymbol(
                symbolPath,
                36, 36);
    }

    private SymbolApp createActiveUserLocationSymbol(String userId) {
        return new PointTextSymbol(Constants.ACTIVE_USER_LOCATION_PIN_CSS_COLOR,
                userId, Constants.USER_LOCATION_PIN_SIZE_PX);
    }

    private SymbolApp createStaleUserLocationSymbol(String userId) {
        return new PointTextSymbol(Constants.STALE_USER_LOCATION_PIN_CSS_COLOR,
                userId, Constants.USER_LOCATION_PIN_SIZE_PX);
    }

    private class SymbolToAppMapper implements ISymbolVisitor {

        SymbolApp mSymbolApp;

        private SymbolApp mapSymbol(Symbol symbol) {
            symbol.accept(this);
            return mSymbolApp;
        }

        @Override
        public void visit(PointSymbol symbol) {
            String symbolPath = mEntityTypeToMarkerUrl.get(symbol.getType());
            mSymbolApp = createImageSymbolFromPath(symbolPath);
        }

        @Override
        public void visit(ImageSymbol symbol) {
            mSymbolApp = createImageSymbolFromPath(mImageMarkerUrl);
        }

        @Override
        public void visit(UserSymbol symbol) {
            if (symbol.isActive()) {
                mSymbolApp = createActiveUserLocationSymbol(symbol.getUserName());
            } else {
                mSymbolApp = createStaleUserLocationSymbol(symbol.getUserName());
            }
        }

        @Override
        public void visit(MyLocationSymbol symbol) {
            mSymbolApp = new PointImageSymbol(mMyLocationMarkerUrl,
                    MY_LOCATION_MARKER_SIZE_PX,
                    MY_LOCATION_MARKER_SIZE_PX);
        }
    }
}
