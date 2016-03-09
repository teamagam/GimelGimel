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
};


/***
 * This method is used by cesium-bridge
 * ADDs a new marker to cesium view
 * @param id - tUnique id (within layer) to be associated with marker
 * @param location - an object with latitude and longitude Number values
 * @param symbol - An object describing the marker's symbology.
 *                 Can describe either  a text marker (using an object with properties cssColor, width and text)
 *                 or an image marker (using an object with properties: imageUrl, imageWidth, imageHeight)
 */
GG.Layers.VectorLayer.prototype.addMarker = function (id, location, symbol) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var billboard = {};
    var billboardSymbol = symbolToBillboardSymbol(symbol);
    $.extend(billboard, GG.Layers.DefaultSymbols.billboard, billboardSymbol);

    var marker = this._dataSource.entities.add({
        position: Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude),
        billboard: billboard
    });

    this._entities[id] = marker;
};

/***
 * This method is used by cesium-bridge
 * UPDATEs an existing marker on cesium view
 * @param id - Unique id (within layer) associated with marker
 * @param location - an object with latitude and longitude Number values
 * @param symbol - An object describing the marker's symbology.
 *                 Can describe either  a text marker (using an object with properties cssColor, width and text)
 *                 or an image marker (using an object with properties: imageUrl, imageWidth, imageHeight)
 */
GG.Layers.VectorLayer.prototype.updateMarker = function (id, location, symbol) {
    GG.Utils.assertIdExists(id, this._entities);

    var marker = this._entities[id];
    if (location) {
        marker.position = Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude);
    }
    if (symbol) {
        var billboard = {};
        var billboardSymbol = symbolToBillboardSymbol(symbol);
        $.extend(billboard, GG.Layers.DefaultSymbols.billboard, billboardSymbol);

        marker.billboard = billboard;
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

    //override and add current polyline settings with defaults and symbol
    $.extend(true, polyline, GG.Layers.DefaultSymbols.polyline, polylineSymbol);

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

    $.extend(true, polygon, GG.Layers.DefaultSymbols.polygon, polygonSymbol);

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
        $.extend(true, this._entities[id].polygon, polygonSymbol);
    }

};


//Default Symbols

var symbolToBillboardSymbol = function (symbol) {
    assertExists(symbol, "symbol");

    if (symbol.text && symbol.cssColor && symbol.size) {
        return {
            image: GG.Utils.pinBuilder().fromText(symbol.text, Cesium.Color.fromCssColorString(symbol.cssColor), symbol.size)
        }
    }
    else if (symbol.imageUrl, symbol.imageWidth, symbol.imageHeight) {
        return {
            image: symbol.imageUrl,
            width: symbol.imageWidth,
            height: symbol.imageHeight
        }
    }
    else {
        throw new Error("Given symbol argument is missing data or of unsupported type");
    }
};

var symbolToPolylineSymbol = function (symbol) {
    assertExists(symbol, "symbol");

    if (symbol.width && symbol.cssColor) {
        return {
            width: symbol.width,
            material: Cesium.Color.fromCssColorString(symbol.cssColor)
        }
    } else {
        throw new Error("Given symbol argument is missing data or is of unsupported type");
    }
};

var symbolToPolygonSymbol = function (symbol) {
    assertExists(symbol, "symbol");

    if (symbol.innerCssColor && symbol.outlineCssColor && symbol.alpha) {
        return {
            material: Cesium.Color.fromCssColorString(symbol.innerCssColor).withAlpha(symbol.alpha),
            outline: true,
            outlineColor: Cesium.Color.fromCssColorString(symbol.outlineCssColor)
        }
    } else {
        throw new Error("Given symbol argument is missing data or is of unsupported type");
    }

};

var assertExists = function (obj, argName) {
    if (typeof obj === "undefined") {
        argName = argName || "obj";
        var message = "Argument " + argName + " is undefined";
        throw new Error(message);
    }
};

defaultBillboard = function () {
    var pinBuilder = new Cesium.PinBuilder();
    var billboard = {
        image: pinBuilder.fromColor(Cesium.Color.ROYALBLUE, 48).toDataURL(),
        verticalOrigin: Cesium.VerticalOrigin.BOTTOM
    };
    return billboard;
};

GG.Layers.DefaultSymbols = {
    billboard: defaultBillboard(),
    polyline: {
        width: 5,
        material: Cesium.Color.RED
    },
    polygon: {
        material: Cesium.Color.RED.withAlpha(0.5),
        outline: true,
        outlineColor: Cesium.Color.BLACK
    }
};
