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

GG.Layers.VectorLayer.prototype.addMarker = function (id, location) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var pinBuilder = new Cesium.PinBuilder();
    var marker = this._dataSource.entities.add({
        position: Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude),
        billboard: {
            image: pinBuilder.fromColor(Cesium.Color.ROYALBLUE, 48).toDataURL(),
            verticalOrigin: Cesium.VerticalOrigin.BOTTOM
        }
    });

    this._entities[id] = marker;
};

GG.Layers.VectorLayer.prototype.updateMarker = function (id, location) {
    GG.Utils.assertIdExists(id, this._entities);

    var marker = this._entities[id];
    marker.position = Cesium.Cartesian3.fromDegrees(location.longitude, location.latitude);
};

GG.Layers.VectorLayer.prototype.addPolyline = function (id, locations) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var degsArray = GG.Utils.locationsToDegreesArray(locations);
    var positions = Cesium.Cartesian3.fromDegreesArray(degsArray);

    this._entities[id] = this._dataSource.entities.add({
        polyline: {
            positions: positions,
            width: 5,
            material: Cesium.Color.RED
        }
    });
};

GG.Layers.VectorLayer.prototype.updatePolyline = function (id, locations) {
    GG.Utils.assertIdExists(id, this._entities);

    var degsArray = GG.Utils.locationsToDegreesArray(locations);
    this._entities[id].positions = Cesium.Cartesian3.fromDegreesArray(degsArray);
};

GG.Layers.VectorLayer.prototype.addPolygon = function (id, locations) {
    GG.Utils.assertIdNotExists(id, this._entities);

    var degsArray = GG.Utils.locationsToDegreesArray(locations);

    var hierarchy = Cesium.Cartesian3.fromDegreesArray(degsArray);

    this._entities[id] = this._dataSource.entities.add({
        polygon: {
            hierarchy: hierarchy,
            material: Cesium.Color.RED.withAlpha(0.5),
            outline: true,
            outlineColor: Cesium.Color.BLACK
        }
    });
};

GG.Layers.VectorLayer.prototype.updatePolygon = function (id, locations) {
    GG.Utils.assertIdExists(id, this._entities);

    var degsArray = GG.Utils.locationsToDegreesArray(locations);
    this._entities[id].hierarchy = Cesium.Cartesian3.fromDegreesArray(degsArray);
};
