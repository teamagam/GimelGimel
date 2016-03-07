package com.teamagam.gimelgimel.app.view.viewer.cesium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.MultipleLocationsEntity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.MultiPointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointTextSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

import java.io.Serializable;

/**
 * Created by Bar on 06-Mar-16.
 */
public class CesiumUtils {
    public static String getLocationJson(Point pointEntity) {
        PointGeometry pLoc = (PointGeometry) pointEntity.getGeometry();
        return getLocationJson(pLoc);
    }


    //TODO: use jackson/gson for json creation
    public static String getLocationJson(PointGeometry pLoc) {
        Gson gson = new Gson();
        return gson.toJson(pLoc);
    }

    public static String getLocationsJson(MultipleLocationsEntity mlEntity) {
        MultiPointGeometry mpg = (MultiPointGeometry) mlEntity.getGeometry();
        Gson gson = new Gson();
        return gson.toJson(mpg.pointsCollection);
    }

    public static String getBillboardJson(Point pointEntity) {
        Symbol symbol = pointEntity.getSymbol();
        if (symbol instanceof PointTextSymbol) {
            PointTextSymbol pts = (PointTextSymbol) symbol;
            String cesiumColor = getCesiumColor(pts.getCssColor());
            String image = String.format("GG.Utils.pinBuilder().fromText('%s',%s,%d).toDataURL()",
                    pts.getText(), cesiumColor, pts.getSize());

            return String.format("{image: %s}", image);
        } else if (symbol instanceof PointImageSymbol) {
            final PointImageSymbol pis = (PointImageSymbol) symbol;

            //Create an anonymous class for GSON conversion
            Serializable ser = new Serializable() {
                public String image = pis.getImageUrl();
                public int width = pis.getPixelWidth();
                public int height = pis.getPixelHeight();
            };
            Gson gson = new Gson();
            return gson.toJson(ser);
        } else {
            throw new UnsupportedOperationException(
                    "Point entity doesn't support given symbol: " + symbol.getClass().getSimpleName());
        }
    }


    private static String getCesiumColor(String cssColor) {
        return String.format("Cesium.Color.fromCssColorString('%s')", cssColor);
    }
}
