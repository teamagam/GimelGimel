package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.util.Log;

import com.google.gson.Gson;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.MultipleLocationsEntity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polygon;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polyline;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bar on 06-Mar-16.
 */
public class CesiumUtils {

    public static final String LOG_TAG = CesiumUtils.class.getSimpleName();

    public static String getLocationJson(Point pointEntity) {
        PointGeometry pLoc = (PointGeometry) pointEntity.getGeometry();
        Gson gson = new Gson();
        return gson.toJson(pLoc);
    }

    public static String getLocationsJson(MultipleLocationsEntity<P> mlEntity) {
        MultiPointGeometry mpg = (MultiPointGeometry) mlEntity.getGeometry();
        Gson gson = new Gson();
        return gson.toJson(mpg.pointsCollection);
    }

    public static String getBillboardJson(Point pointEntity) {
        Symbol symbol = pointEntity.getSymbol();

        JSONObject billboardSymbolJsonObj = new JSONObject();
        if (symbol instanceof PointTextSymbol) {
            PointTextSymbol pts = (PointTextSymbol) symbol;

            try {
                billboardSymbolJsonObj.put("text", pts.getText());
                billboardSymbolJsonObj.put("cssColor", pts.getCssColor());
                billboardSymbolJsonObj.put("size", pts.getSize());
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Unable to build json object for point text symbol", e);
            }
        } else if (symbol instanceof PointImageSymbol) {
            PointImageSymbol pis = (PointImageSymbol) symbol;

            try {
                billboardSymbolJsonObj.put("imageUrl", pis.getImageUrl());
                billboardSymbolJsonObj.put("imageWidth", pis.getPixelWidth());
                billboardSymbolJsonObj.put("imageHeight", pis.getPixelHeight());
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Unable to build json object for point image symbol", e);
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
            Log.e(LOG_TAG, "Unable to build json object for polyline symbol", e);
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
            Log.e(LOG_TAG, "Unable to build json object for polygon symbol", e);
        }

        return symbolJson.toString();
    }
}
