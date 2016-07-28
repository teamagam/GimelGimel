/**
 * used to handle all user interactions with cesium
 */

var GG = GG || {};

GG.EventHandler = function (viewer) {
    this._viewer = viewer;
    this._scene = viewer.scene;
    this._canvas = viewer.scene.canvas;
    this._handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
    this.LONG_TAP_THRESHOLD_MILLIS = 1000;
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
    ;
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


GG.EventHandler.prototype.setSingleTouchActions = function (layersManager) {

    var self = this;
    //used for measuring time for
    this.setScreenSpaceEventAction(Cesium.ScreenSpaceEventType.LEFT_DOWN, function (movement) {
        self.startPosition = movement.position;
        self.timePressDownMillis = new Date().getTime();
    });

    this.setScreenSpaceEventAction(Cesium.ScreenSpaceEventType.LEFT_UP, function (movement) {
        var diffTimeMillis = new Date().getTime() - self.timePressDownMillis;
        if (diffTimeMillis > self.LONG_TAP_THRESHOLD_MILLIS) {
            getPositionWithHeight(movement).then(function (updatedPositions) {
                // updatedPositions is just a reference to positions.
                var position = updatedPositions[0];

                GG.AndroidAPI.onLongPress({
                    longitude: Cesium.Math.toDegrees(position.longitude),
                    latitude: Cesium.Math.toDegrees(position.latitude),
                    altitude: position.height,
                    hasAltitude: true
                });
            });
        } else {
            var entity = self.pickEntity(movement.position);
            clickEntityWithLayer(entity);
        }
    });

    this.setScreenSpaceEventAction(Cesium.ScreenSpaceEventType.LEFT_DOUBLE_CLICK, function (movement) {
        var position = movement.position;
        GG.AndroidAPI.onDoubleTap({
            longitude: Cesium.Math.toDegrees(position.longitude),
            latitude: Cesium.Math.toDegrees(position.latitude)
        });
    });

    function getPositionWithHeight(movement) {
        var cartesian = self._viewer.camera.pickEllipsoid(movement.position, self._scene.globe.ellipsoid);
        if (cartesian) {
            var cartographic = Cesium.Cartographic.fromCartesian(cartesian);

            var level = 11; //The terrain level-of-detail from which to query terrain heights.
            return Cesium.sampleTerrain(self._viewer.terrainProvider, level, [cartographic]);
        }
    }

    function clickEntityWithLayer(entity) {
        $.each(layersManager.getLayers(), function (layerId, layer) {
            try {
                if (layer.getEntity(entity.id)) {
                    GG.AndroidAPI.onEntityClicked(layerId, entity.id)
                }
            } catch (err) {
//                    iterate to next layer
            }
        });
    }
}
;


GG.EventHandler.prototype.setViewedLocationUpdates = function (camera) {
    // Call the android API when the scene has changed
    this._viewer.camera.moveEnd.addEventListener(function () {
        var currentLocation = camera.getCameraPosition();
        GG.AndroidAPI.updateViewedLocation(currentLocation);
    });
};