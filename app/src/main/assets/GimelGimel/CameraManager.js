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
    if (point.hasOwnProperty('altitude')) {
        height = point.altitude;
    }
    else {
        height = this._camera.positionCartographic.height;
    }
    this._camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(point.longitude, point.latitude, height)
    });
};


GG.CameraManager.prototype.getCameraPosition = function(){
    var point = {
        longitude: this._camera.positionCartographic.longitude,
        latitude: this._camera.positionCartographic.latitude
    };

    return {
        longitude: Cesium.Math.toDegrees(point.longitude),
        latitude: Cesium.Math.toDegrees(point.latitude)
    };
};


//GG.CameraManager.canvas.prototype.addEventListener('touchstart', function(e) {
//    alert("PageX: "+e.pageX+","+e.pageY+"\n LayerX: "+e.layerX+","+e.layerY+"\n" );
//    // prevent the browsers default action!
//    //e.preventDefault();
//    var touch = e.touches[0];
//    var c = getCoords(e);
//    var x = touch.pageX;
//    var y = touch.pageY;
//// or taking offset into consideration
//    var x_2 = touch.pageX - canvas.offsetLeft;
//    var y_2 = touch.pageY - canvas.offsetTop;
//    // Get coordinates
//
//    addClick(c.x, c.y, false);
//});


