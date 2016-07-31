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


GG.EventHandler.prototype.setSingleTouchActions = function () {

    var self = this;
    //used for measuring time for
    this.setScreenSpaceEventAction(Cesium.ScreenSpaceEventType.LEFT_DOWN, function (movement) {
        self.position = movement.position;

        var cartesian = self._viewer.camera.pickEllipsoid(self.position, self._scene.globe.ellipsoid);
        self._viewer.entities.add({
          position : cartesian ,
          point : {
            pixelSize : 5,
        	color : Cesium.Color.RED,
        	outlineColor : Cesium.Color.WHITE,
        	outlineWidth : 2
          }});

        var cartesian3 = self._scene.pickPosition(self.position);
                self._viewer.entities.add({
                          position : cartesian3 ,
                          point : {
                            pixelSize : 10,
                        	color : Cesium.Color.GREEN,
                        	outlineColor : Cesium.Color.WHITE,
                        	outlineWidth : 2
                          }});

    });
};

GG.EventHandler.prototype.onSingleTap = function () {
    var entity = this.pickEntity(this.position);
    clickEntityWithLayer(entity);

    function clickEntityWithLayer(entity) {
        $.each(GG.layerManager.getLayers(), function (layerId, layer) {
            try {
                if (layer.getEntity(entity.id)) {
                    GG.AndroidAPI.onEntityClicked(layerId, entity.id)
                }
            } catch (err) {
//              iterate to next layer
            }
        });
    }
};

GG.EventHandler.prototype.onLongPress = function () {
    var self = this;
    var cartesian = self._scene.pickPosition(self.position);
    if (self._scene.pickPositionSupported && Cesium.defined(cartesian)) {
        var cartographic = Cesium.Cartographic.fromCartesian(cartesian);
//    getPositionWithHeight(self.position).then(function (updatedPositions) {
        // updatedPositions is just a reference to positions.
//        var position = updatedPositions[0];
//
//        var cart3 = Cesium.Cartesian3.fromRadians(position.longitude, position.latitude, position.height);
//
        self._viewer.entities.add({
                  position : cartesian ,
                  point : {
                    pixelSize : 10,
                	color : Cesium.Color.GREEN,
                	outlineColor : Cesium.Color.WHITE,
                	outlineWidth : 2
                  }});
        GG.AndroidAPI.onLongPress({
            longitude: Cesium.Math.toDegrees(cartographic.longitude),
            latitude: Cesium.Math.toDegrees(cartographic.latitude),
            altitude: cartographic.height,
            hasAltitude: true
        });
    }

//    function getPositionWithHeight(position) {
//        var cartesian = self._viewer.camera.pickEllipsoid(position, self._scene.globe.ellipsoid);
//        if (cartesian) {
//            var cartographic = Cesium.Cartographic.fromCartesian(cartesian);
//
//            var level = 11; //The terrain level-of-detail from which to query terrain heights.
//            return Cesium.sampleTerrain(self._viewer.terrainProvider, level, [cartographic]);
//        }
//    }
};

GG.EventHandler.prototype.onDoubleTap = function () {
    var position = this.position;
    var cartesian = self._viewer.camera.pickEllipsoid(position, self._scene.globe.ellipsoid);
    if (cartesian) {
        var cartographic = Cesium.Cartographic.fromCartesian(cartesian);
        GG.AndroidAPI.onDoubleTap({
            longitude: Cesium.Math.toDegrees(cartographic.longitude),
            latitude: Cesium.Math.toDegrees(cartographic.latitude)
        });
    }
};



GG.EventHandler.prototype.setViewedLocationUpdates = function (camera) {
    // Call the android API when the scene has changed
    this._viewer.camera.moveEnd.addEventListener(function () {
        var currentLocation = camera.getCameraPosition();
        GG.AndroidAPI.updateViewedLocation(currentLocation);
    });
};