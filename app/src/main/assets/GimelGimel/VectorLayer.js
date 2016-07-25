/**
 * Created by Bar on 01-Mar-16.
 */

var GG = GG || {};

GG.Layers = GG.Layers || {};

GG.Layers.VectorLayer = function (id) {
    this._id = id;
    this._dataSource = new Cesium.CustomDataSource(this._id);
    this._entities = {};
};


GG.Layers.VectorLayer.prototype.getEntity = function (id) {
    GG.Utils.assertIdExists(id, this._entities);

    return this._entities[id];
};

GG.Layers.VectorLayer.prototype.removeEntity = function (id) {
    GG.Utils.assertIdExists(id, this._entities);

    var entity = this._entities[id];
    this._dataSource.entities.remove(entity);
    delete this._entities[id];
};


/***
 * This method is used by cesium-bridge
 * ADDs a new marker to cesium view
 * @param id - Unique id (within layer) to be associated with marker
 * @param location - An object with latitude and longitude Number values
 * @param symbol - An object describing the marker's symbology.
 *                 Can describe either  a text marker (using an object with properties cssColor, size and text)
 *                 or an image marker (using an object with properties: imageUrl, imageWidth, imageHeight)
 */
GG.Layers.VectorLayer.prototype.addMarker = function (id, location, symbol) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var entity = createMarker(id, location, symbol);
    var marker = this._dataSource.entities.add(entity);

    this._entities[id] = marker;
};

/***
 * This method is used by cesium-bridge
 * UPDATEs an existing marker on cesium view
 * @param id - Unique id (within layer) associated with marker
 * @param location - an object with latitude and longitude Number values
 * @param symbol - An object describing the marker's symbology.
 *                 Can describe either  a text marker (using an object with properties cssColor, size and text)
 *                 or an image marker (using an object with properties: imageUrl, imageWidth, imageHeight)
 */
GG.Layers.VectorLayer.prototype.updateMarker = function (id, location, symbol) {
    GG.Utils.assertIdExists(id, this._entities);

    var marker = this._entities[id];
    if (location) {
        marker.position = Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude);
    }
    if (symbol) {
        clearSymbol(marker);
        setMarkerSymbology(marker, symbol);
    }
};

/***
 * This method is used by cesium-bridge
 * ADDs a new polyline to cesium view
 * @param id - Unique id (within the layer) to be associated with polyline
 * @param locations - An array of location objects, each has latitude and longitude properties
 * @param symbol - Describes polyline's symbology.
 *                 Should be an object with width and cssColor properties
 */
GG.Layers.VectorLayer.prototype.addPolyline = function (id, locations, symbol) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var degsArray = GG.Utils.locationsToDegreesArray(locations);
    var positions = Cesium.Cartesian3.fromDegreesArray(degsArray);

    var polyline = {
        positions: positions
    };

    var polylineSymbol = symbolToPolylineSymbol(symbol);

    //Add current polyline symbology to polyline
    $.extend(true, polyline, polylineSymbol);

    this._entities[id] = this._dataSource.entities.add({
        polyline: polyline
    });
};

/***
 * This method is used by cesium-bridge
 * UPDATEs an existing polyline on cesium view
 * @param id - Unique id (within the layer) associated with polyline
 * @param locations - An array of location objects, each has latitude and longitude properties
 * @param symbol - Describes polyline's symbology.
 *                 Should be an object with width and cssColor properties
 */
GG.Layers.VectorLayer.prototype.updatePolyline = function (id, locations, symbol) {
    GG.Utils.assertIdExists(id, this._entities);

    if (locations) {
        var degsArray = GG.Utils.locationsToDegreesArray(locations);
        this._entities[id].polyline.positions = Cesium.Cartesian3.fromDegreesArray(degsArray);
    }

    if (symbol) {
        var polylineSymbol = symbolToPolylineSymbol(symbol);
        //Overrides symbology with new one
        $.extend(true, this._entities[id].polyline, polylineSymbol);
    }
};

/***
 * This method is used by cesium-bridge
 * ADDs a new polygon to cesium view
 * @param id - Unique id (within the layer) to be associated with polygon
 * @param locations - An array of location objects, each has latitude and longitude properties
 * @param symbol - Describes polygon's symbology.
 *                 Should be an object with innerCssColor, outlineCssColor and alpha
 */
GG.Layers.VectorLayer.prototype.addPolygon = function (id, locations, symbol) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var degsArray = GG.Utils.locationsToDegreesArray(locations);

    var hierarchy = Cesium.Cartesian3.fromDegreesArray(degsArray);

    var polygon = {
        hierarchy: hierarchy
    };

    var polygonSymbol = symbolToPolygonSymbol(symbol);

    //Adds symbology to cesium-polygon object
    $.extend(true, polygon, polygonSymbol);

    this._entities[id] = this._dataSource.entities.add({
        polygon: polygon
    });
};

/***
 * This method is used by cesium-bridge
 * UPDATEs an existing polygon to cesium view
 * @param {string} id - Unique id (within the layer) associated with polygon
 * @param locations - An array of location objects, each has latitude and longitude properties
 * @param symbol - Describes polygon's symbology.
 *                 Should be an object with innerCssColor, outlineCssColor and alpha
 */
GG.Layers.VectorLayer.prototype.updatePolygon = function (id, locations, symbol) {
    GG.Utils.assertIdExists(id, this._entities);

    if (locations) {
        var degsArray = GG.Utils.locationsToDegreesArray(locations);
        this._entities[id].polygon.hierarchy = Cesium.Cartesian3.fromDegreesArray(degsArray);
    }

    if (symbol) {
        var polygonSymbol = symbolToPolygonSymbol(symbol);
        //Used to override all the symbology with the new one.
        $.extend(true, this._entities[id].polygon, polygonSymbol);
    }

};


//Default Symbols

function symbolToImageMarkerSymbol(symbol) {
    GG.Utils.assertDefined(symbol.imageUrl, "symbol.imageUrl");
    GG.Utils.assertDefined(symbol.imageWidth, "symbol.Width");
    GG.Utils.assertDefined(symbol.imageHeight, "symbol.Height");
    return {
        image: symbol.imageUrl,
        width: symbol.imageWidth,
        height: symbol.imageHeight,
        verticalOrigin: Cesium.VerticalOrigin.BOTTOM
    }
}

var createMarker = function (id, location, symbol) {
    GG.Utils.assertDefined(symbol, "symbol");

    var marker = {};
    marker.id = id;
    marker.position = Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude);

    setMarkerSymbology(marker, symbol);

    return marker;
};

var clearSymbol = function (marker) {
    marker.billboard = undefined;
    marker.label = undefined;
    marker.point = undefined;
};

var setMarkerSymbology = function (marker, symbol) {
    if (symbol.text) {
        marker.point = createPoint(symbol.cssColor);
        marker.label = createLabel(symbol.text);
    } else if (symbol.imageUrl) {
        marker.billboard = symbolToImageMarkerSymbol(symbol);
    } else {
        throw new Error("Given symbol argument is missing data or of unsupported type");
    }
};

var createPoint = function (cssColor) {
    return {
        pixelSize: 5,
        color: Cesium.Color.fromCssColorString(cssColor),
        outlineColor: Cesium.Color.WHITE,
        outlineWidth: 2
    };
};

var createLabel = function (text) {
    return {
        text: text,
        font: '10pt monospace',
        style: Cesium.LabelStyle.FILL_AND_OUTLINE,
        outlineWidth: 2,
        verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
        pixelOffset: new Cesium.Cartesian2(0, -9)
    }
};

var symbolToPolylineSymbol = function (symbol) {
    GG.Utils.assertDefined(symbol, "symbol");
    GG.Utils.assertDefined(symbol.width, "symbol.width");
    GG.Utils.assertDefined(symbol.cssColor, "symbol.cssColor");

    return {
        width: symbol.width,
        material: Cesium.Color.fromCssColorString(symbol.cssColor)
    }
};

var symbolToPolygonSymbol = function (symbol) {
    GG.Utils.assertDefined(symbol, "symbol");
    GG.Utils.assertDefined(symbol.innerCssColor, "symbol.innerCssColor");
    GG.Utils.assertDefined(symbol.outlineCssColor, "symbol.outlineCssColor");
    GG.Utils.assertDefined(symbol.alpha, "symbol.alpha");

    return {
        material: Cesium.Color.fromCssColorString(symbol.innerCssColor).withAlpha(symbol.alpha),
        outline: true,
        outlineColor: Cesium.Color.fromCssColorString(symbol.outlineCssColor)
    }
};
