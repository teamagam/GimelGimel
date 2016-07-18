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

    var billboardSymbol = symbolToBillboardSymbol(symbol);

    var marker = this._dataSource.entities.add({
        id: id,
        position: Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude),
        billboard: billboardSymbol
    });

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
        var billboardSymbol = symbolToBillboardSymbol(symbol);
        //Override marker's symbology with new one
        $.extend(true, marker.billboard, billboardSymbol);
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

function symbolToTextMarkerSymbol(symbol) {
    GG.Utils.assertDefined(symbol.text, "symbol.text");
    GG.Utils.assertDefined(symbol.cssColor, "symbol.cssColor");
    GG.Utils.assertDefined(symbol.size, "symbol.size");

    return {
        image: GG.Utils.pinBuilder().fromText(symbol.text, Cesium.Color.fromCssColorString(symbol.cssColor), symbol.size),
        verticalOrigin: Cesium.VerticalOrigin.BOTTOM
    }
}
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
var symbolToBillboardSymbol = function (symbol) {
    GG.Utils.assertDefined(symbol, "symbol");

    if (symbol.text) {
        //if text-marker-symbol
        return symbolToTextMarkerSymbol(symbol);
    } else if (symbol.imageUrl) {
        // if image-marker-symbol
        return symbolToImageMarkerSymbol(symbol);
    } else {
        throw new Error("Given symbol argument is missing data or of unsupported type");
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
