/**
 * Created by Bar on 14-Mar-16.
 */


var CameraManager = function (camera) {
    this._camera = camera;
};


CameraManager.prototype.zoomTo = function (point) {
    //point can be {lat: , long: } or {lat: , long:, alt: }
    // use this._camera.
};

//add other functions



//TODO: initialize camera manager somewhere (maybe create an initialization file and move viewer instantiation to there too)
//TODO: synchronize java calls to instantiated CameraManager.
