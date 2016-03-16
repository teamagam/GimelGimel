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
    var height;
    if (point.altitude == 0) {
        height = point.altitude;
    }
    else {
        height = this._camera.positionCartographic.height;
    }
    this._camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(point.longitude, point.latitude, height)
    });
};


GG.CameraManager.prototype.getCameraPosition = function () {
    var longitude = this._camera.positionCartographic.longitude;
    var latitude = this._camera.positionCartographic.latitude;
    return {
        longitude: Cesium.Math.toDegrees(longitude),
        latitude: Cesium.Math.toDegrees(latitude)
    };
};
