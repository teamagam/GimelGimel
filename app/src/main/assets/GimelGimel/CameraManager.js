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

// 1. Fly to a position with a top-down view on the point
GG.CameraManager.prototype.lookAt = function (point) {
    var newAltitude = this._camera.positionCartographic.height;
    this.flyTo(point, newAltitude);
};

GG.CameraManager.prototype.lookAt = function (point, newHeight) {
    this._camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(point.longitude, point.latitude, newHeight)
    });
};


GG.CameraManager.prototype.setCameraPosition = function (viewerCamera) {
    var point = viewerCamera.cameraPosition;
    this._camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(point.longitude, point.latitude, point.altitude),
        orientation: {
            heading: viewerCamera.heading,
            pitch: viewerCamera.pitch,
            roll: viewerCamera.roll
        },
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
