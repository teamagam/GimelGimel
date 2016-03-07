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

GG.Layers.VectorLayer.prototype.addMarker = function (id, location, symbol) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var billboard = {};
    var pinBuilder = new Cesium.PinBuilder();
    $.extend(billboard, GG.Layers.DefaultSymbols.billboard, symbol);

    var marker = this._dataSource.entities.add({
        position: Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude),
        billboard: billboard
    });

    this._entities[id] = marker;
};

GG.Layers.VectorLayer.prototype.updateMarker = function (id, location, symbol) {
    GG.Utils.assertIdExists(id, this._entities);

    var marker = this._entities[id];
    if (location) {
        marker.position = Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude);
    }
    if (symbol) {
        var pinBuilder = new Cesium.PinBuilder();
        var billboard = {};
        $.extend(billboard, GG.Layers.DefaultSymbols.billboard, symbol);

        marker.billboard = billboard;
    }
};

GG.Layers.VectorLayer.prototype.addPolyline = function (id, locations, symbol) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var degsArray = GG.Utils.locationsToDegreesArray(locations);
    var positions = Cesium.Cartesian3.fromDegreesArray(degsArray);

    var polyline = {
        positions: positions
    };

    //override and add current polyline settings with defaults and symbol
    $.extend(true, polyline, GG.Layers.DefaultSymbols.polyline, symbol);

    this._entities[id] = this._dataSource.entities.add({
        polyline: polyline
    });
};

GG.Layers.VectorLayer.prototype.updatePolyline = function (id, locations, symbol) {
    GG.Utils.assertIdExists(id, this._entities);

    if (locations) {
        var degsArray = GG.Utils.locationsToDegreesArray(locations);
        this._entities[id].polyline.positions = Cesium.Cartesian3.fromDegreesArray(degsArray);
    }

    if (symbol) {
        $.extend(true, this._entities[id].polyline, symbol);
    }
};

GG.Layers.VectorLayer.prototype.addPolygon = function (id, locations, symbol) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var degsArray = GG.Utils.locationsToDegreesArray(locations);

    var hierarchy = Cesium.Cartesian3.fromDegreesArray(degsArray);

    var polygon = {
        hierarchy: hierarchy
    };

    $.extend(true, polygon, GG.Layers.DefaultSymbols.polygon, symbol);

    this._entities[id] = this._dataSource.entities.add({
        polygon: polygon
    });
};

GG.Layers.VectorLayer.prototype.updatePolygon = function (id, locations, symbol) {
    GG.Utils.assertIdExists(id, this._entities);

    if (locations) {
        var degsArray = GG.Utils.locationsToDegreesArray(locations);
        this._entities[id].polygon.hierarchy = Cesium.Cartesian3.fromDegreesArray(degsArray);
    }

    if(symbol){
        $.extend(true, this._entities[id].polygon, symbol);
    }

};


//Default Symbols

defaultBillboard = function () {
    var pinBuilder = new Cesium.PinBuilder();
    var billboard = {
        image: pinBuilder.fromColor(Cesium.Color.ROYALBLUE, 48).toDataURL(),
        verticalOrigin: Cesium.VerticalOrigin.BOTTOM
    };
    return billboard;
}

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
