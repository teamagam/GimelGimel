package com.teamagam.gimelgimel.app.map.cesium;

import com.google.gson.Gson;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.map.model.entities.MultipleLocationsEntity;
import com.teamagam.gimelgimel.app.map.model.entities.Point;
import com.teamagam.gimelgimel.app.map.model.entities.Polygon;
import com.teamagam.gimelgimel.app.map.model.entities.Polyline;
import com.teamagam.gimelgimel.app.map.model.geometries.MultiPointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bar on 06-Mar-16.
 */
public class CesiumUtils {

    private static final Logger sLogger = LoggerFactory.create(CesiumUtils.class);

    private static Gson sGson = new Gson();
    public static String getLocationJson(PointGeometryApp pointGeometry) {
        return sGson.toJson(pointGeometry);
    }

    public static String getLocationsJson(MultipleLocationsEntity mlEntity) {
        MultiPointGeometryApp mpg = (MultiPointGeometryApp) mlEntity.getGeometry();
        Gson gson = new Gson();
        return gson.toJson(mpg.pointsCollection);
    }

    public static String getBillboardJson(Point pointEntity) {
        SymbolApp symbol = pointEntity.getSymbol();

        JSONObject billboardSymbolJsonObj = new JSONObject();
        if (symbol instanceof PointTextSymbol) {
            PointTextSymbol pts = (PointTextSymbol) symbol;

            try {
                billboardSymbolJsonObj.put("text", pts.getText());
                billboardSymbolJsonObj.put("cssColor", pts.getCssColor());
                billboardSymbolJsonObj.put("size", pts.getSize());
            } catch (JSONException e) {
                sLogger.e("Unable to build json object for point text symbol", e);
            }
        } else if (symbol instanceof PointImageSymbol) {
            PointImageSymbol pis = (PointImageSymbol) symbol;

            try {
                billboardSymbolJsonObj.put("imageUrl", pis.getImageUrl());
                billboardSymbolJsonObj.put("imageWidth", pis.getPixelWidth());
                billboardSymbolJsonObj.put("imageHeight", pis.getPixelHeight());
            } catch (JSONException e) {
                sLogger.e("Unable to build json object for point image symbol", e);
            }
        } else {
            throw new UnsupportedOperationException(
                    "Point entity doesn't support given symbol: " + symbol.getClass().getSimpleName());
        }

        return billboardSymbolJsonObj.toString();
    }


    public static String getPolylineSymbolJson(Polyline polyline) {
        PolylineSymbol ps = (PolylineSymbol) polyline.getSymbol();

        JSONObject polylineSymbolJson = new JSONObject();
        try {
            polylineSymbolJson.put("width", ps.getWidth());
            polylineSymbolJson.put("cssColor", ps.getCssColor());
        } catch (JSONException e) {
            sLogger.e("Unable to build json object for polyline symbol", e);
        }

        return polylineSymbolJson.toString();
    }

    public static String getPolygonSymbolJson(Polygon polygon) {
        PolygonSymbol ps = (PolygonSymbol) polygon.getSymbol();

        JSONObject symbolJson = new JSONObject();
        try {
            symbolJson.put("innerCssColor", ps.getInnerCssColor());
            symbolJson.put("outlineCssColor", ps.getOutlineCssColor());
            symbolJson.put("alpha", ps.getInnerColorAlpha());
        } catch (JSONException e) {
            sLogger.e("Unable to build json object for polygon symbol", e);
        }

        return symbolJson.toString();
    }


    public static PointGeometryApp getPointGeometryFromJson(String jsonString) {
        return sGson.fromJson(jsonString, PointGeometryApp.class);
    }

    public static String getLocationJson(PointGeometry point) {
        return sGson.toJson(PointGeometryApp.create(point));
    }
}
