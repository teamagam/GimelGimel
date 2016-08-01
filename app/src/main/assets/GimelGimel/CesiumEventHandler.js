/**
 * used to handle all user interactions with cesium
 */

var GG = GG || {};

GG.EventHandler = function (viewer) {
    this._viewer = viewer;
    this._scene = viewer.scene;
    this._canvas = viewer.scene.canvas;
    this._handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
};

/**
 * Returns the top-most Entity at the provided window coordinates
 * or undefined if no Entity is at that location.
 *
 * Attention:
 * we changed Cesium.js  windowPosition from 3.0/3.0 to:
 * var rectangleWidth = 55.0;
 * var rectangleHeight = 55.0;
 * so the windowPosition will be large enough to catch our touch.
 *
 * @param {Cartesian2} windowPosition The window coordinates.
 * @returns {Entity} The picked Entity or undefined.
 */
GG.EventHandler.prototype.pickEntity = function (windowPosition) {
    var picked = this._scene.pick(windowPosition);
    if (Cesium.defined(picked)) {
        var id = Cesium.defaultValue(picked.id, picked.primitive.id);
        if (id instanceof Cesium.Entity) {
            return id;
        }
    }
    return undefined;
};

/**
 * Sets an action to be executed when event of given type occurs in viewer
 *
 * @param {Viewer} viewer - cesium viewer to attached the listener to
 * @param {Number} screenSpaceEventType - the type of space event to register action.
 * Must be one of Cesium.ScreenSpaceEventType types.
 * @param {function(movement)} listener - function to be called when event occurs
 */
GG.EventHandler.prototype.setScreenSpaceEventAction = function (screenSpaceEventType, action) {
    this._handler.setInputAction(action, screenSpaceEventType);
};


GG.EventHandler.prototype.onSingleTap = function (relativeX, relativeY) {
    var self = this;
    var position = self.getWindowPosition(relativeX, relativeY);
    var entity = self.pickEntity(position);
    this.clickEntityWithLayer(entity);

};

GG.EventHandler.prototype.onLongPress = function (relativeX, relativeY) {
    var self = this;
    var position = self.getWindowPosition(relativeX, relativeY);
    var ray = self._viewer.camera.getPickRay(position);
    var cartesian = self._scene.globe.pick(ray, self._scene);
    var cartographic = Cesium.Cartographic.fromCartesian(cartesian);

    GG.AndroidAPI.onLongPress({
        longitude: Cesium.Math.toDegrees(cartographic.longitude),
        latitude: Cesium.Math.toDegrees(cartographic.latitude),
        altitude: cartographic.height,
        hasAltitude: true
    });

};

GG.EventHandler.prototype.onDoubleTap = function (relativeX, relativeY) {
    var self = this;
    var position = self.getWindowPosition(relativeX, relativeY);
    var cartesian = self._viewer.camera.pickEllipsoid(position, self._scene.globe.ellipsoid);
    var cartographic = Cesium.Cartographic.fromCartesian(cartesian);

    GG.AndroidAPI.onDoubleTap({
        longitude: Cesium.Math.toDegrees(cartographic.longitude),
        latitude: Cesium.Math.toDegrees(cartographic.latitude),
        altitude: GG.cameraManager.getCameraPosition().height,
        hasAltitude: true
    });
};

GG.EventHandler.prototype.getWindowPosition = function (relativeX, relativeY) {
    var x = window.innerWidth * relativeX;
    var y = window.innerHeight * relativeY;
    return new Cesium.Cartesian2(x, y);
};

GG.EventHandler.prototype.clickEntityWithLayer = function (entity) {
    $.each(GG.layerManager.getLayers(), function (layerId, layer) {
        try {
            if (layer.getEntity(entity.id)) {
                GG.AndroidAPI.onEntityClicked(layerId, entity.id)
            }
        } catch (err) {
//              iterate to next layer
        }
    });
};

GG.EventHandler.prototype.setViewedLocationUpdates = function (camera) {
    // Call the android API when the scene has changed
    this._viewer.camera.moveEnd.addEventListener(function () {
        var currentLocation = camera.getCameraPosition();
        GG.AndroidAPI.updateViewedLocation(currentLocation);
    });
};