/**
* used to handle all user interactions with cesium
*/

var GG = GG || {};

GG.EventHandler = function(viewer){
    this._viewer = viewer;
    this._scene = viewer.scene;
    this._canvas = viewer.scene.canvas;
    this._handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
};

/**
 * Returns the top-most Entity at the provided window coordinates
 * or undefined if no Entity is at that location.
 *
 * @param {Cartesian2} windowPosition The window coordinates.
 * @returns {Entity} The picked Entity or undefined.
 */
GG.EventHandler.prototype.pickEntity = function(windowPosition) {
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
 * Returns the list of Entities at the provided window coordinates.
 * The Entities are sorted front to back by their visual order.
 *
 * @param {Cartesian2} windowPosition The window coordinates.
 * @returns {Entity[]} The picked Entities or undefined.
 */
GG.EventHandler.prototype.pickEntities = function(viewer, windowPosition) {
  var i;
  var entity;
  var picked;
  var pickedPrimitives = viewer.scene.drillPick(windowPosition);
  var length = pickedPrimitives.length;
  var result = [];
  var hash = {};

  for (i = 0; i < length; i++) {
    picked = pickedPrimitives[i];
    entity = Cesium.defaultValue(picked.id, picked.primitive.id);
    if (entity instanceof Cesium.Entity &&
        !Cesium.defined(hash[entity.id])) {
      result.push(entity);
      hash[entity.id] = true;
    }
  }
  return result;
};

/**
* Sets an action to be executed when event of given type occurs in viewer
*
* @param {Viewer} viewer - cesium viewer to attached the listener to
* @param {Number} screenSpaceEventType - the type of space event to register action.
* Must be one of Cesium.ScreenSpaceEventType types.
* @param {function(movement)} listener - function to be called when event occurs
*/
GG.EventHandler.prototype.setScreenSpaceEventAction = function(screenSpaceEventType, action){
this._handler.setInputAction(action, screenSpaceEventType);
};




GG.EventHandler.prototype.setSingleTouchActions = function (layersManager){

    var handler = this;
    //Set mouse move event listener
    //Cesium.ScreenSpaceEventType.LEFT_DOWN is mapped to touch events.
    this.setScreenSpaceEventAction( Cesium.ScreenSpaceEventType.LEFT_DOWN, function (movement) {
        entity = handler.pickEntity(movement.position);
        if(entity){
            $.each(layersManager.getLayers(), function(layerId, layer){
                try{
                    layer.getEntity(entity.id)
                    GG.AndroidAPI.onEntityClicked(layerId, entity.id)
                    return;
                } catch(err) {
//                    iterate to next layer
                }
            });

        } else{
            var cartesian = handler._viewer.camera.pickEllipsoid(movement.position, handler._scene.globe.ellipsoid);
            if (cartesian) {
                var cartographic = Cesium.Cartographic.fromCartesian(cartesian);
                var longitude = Cesium.Math.toDegrees(cartographic.longitude);
                var latitude = Cesium.Math.toDegrees(cartographic.latitude);

                GG.AndroidAPI.updateSelectedLocation({
                    longitude: longitude,
                    latitude: latitude
                });
            }
        };
    });
};



GG.EventHandler.prototype.setViewedLocationUpdates = function(camera){
    // Call the android API when the scene has changed
    this._viewer.camera.moveEnd.addEventListener(function() {
        var currentLocation = camera.getCameraPosition();
        GG.AndroidAPI.updateViewedLocation(currentLocation);
    });
};