/**
 * Created by Bar on 14-Mar-16.
 */

/**
 * maybe we can use DEFAULT_VIEW_RECTANGLE and flyHome?
 */

var GG = GG || {};

GG.CameraManager = function (camera) {
    this._camera = camera;
};

// 1. Fly to a position with a top-down view
GG.CameraManager.prototype.zoomTo = function (point) {
    //point can be {lat: , long: } or {lat: , long:-, alt: }
    var height = (point.altitude > 0) ?
        point.altitude :
        this._camera.positionCartographic.height;
    this._camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(point.longitude, point.latitude, height),
        duration: 0.2
    });
};

GG.CameraManager.prototype.zoomToRectangle = function (west, south, east, north) {
    this._camera.flyTo({
        destination: Cesium.Rectangle.fromDegrees(west, south, east, north)
    });
};

GG.CameraManager.prototype.getCameraPosition = function () {
    var longitude = this._camera.positionCartographic.longitude;
    var latitude = this._camera.positionCartographic.latitude;
    var altitude = this._camera.positionCartographic.height;
    return {
        longitude: Cesium.Math.toDegrees(longitude),
        latitude: Cesium.Math.toDegrees(latitude),
        altitude: altitude
    };
};
