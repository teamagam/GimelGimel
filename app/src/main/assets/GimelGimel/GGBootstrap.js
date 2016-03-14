/**
 * Created by Bar on 01-Mar-16.
 */


var GG = GG || {};

GG.viewer = new Cesium.Viewer('cesiumContainer', {
    imageryProvider: new Cesium.BingMapsImageryProvider({
        url: 'http://dev.virtualearth.net',
        mapStyle: Cesium.BingMapsStyle.AERIAL
    }),
    terrainProvider: new Cesium.CesiumTerrainProvider({
        url: 'http://assets.agi.com/stk-terrain/world'
    }),
    //sceneMode: Cesium.SceneMode.SCENE2D,
    baseLayerPicker: false,
    fullScreenButton: false,
    homeButton: false,
    timeline: false,
    sceneModePicker: false,
    navigationHelpButton: false,
    selectionIndicator: false,
    infoBox: false,
    geocoder: false,
    animation: false,
});

//Set mouse move event listener
//Cesium.ScreenSpaceEventType.LEFT_DOWN is mapped to touch events.
GG.Utils.setScreenSpaceEventAction(GG.viewer, Cesium.ScreenSpaceEventType.LEFT_DOWN, function (movement) {
    var cartesian = GG.viewer.camera.pickEllipsoid(movement.position, GG.viewer.scene.globe.ellipsoid);
    if (cartesian) {
        var cartographic = Cesium.Cartographic.fromCartesian(cartesian);
        var longitude = Cesium.Math.toDegrees(cartographic.longitude);
        var latitude = Cesium.Math.toDegrees(cartographic.latitude);

        GG.AndroidAPI.updateLocation({
            longitude: longitude,
            latitude: latitude
        });
    }
});


// 1. Fly to a position with a top-down view
GG.viewer.zoomTo3Point = function (x, y, z) {
    this.camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(x, y, z)
    });
};

// 2. Fly to a Rectangle with a top-down view
GG.viewer.zoomToRectangle = function (west, south, east, north) {
    this.camera.flyTo({
        destination: Cesium.Rectangle.fromDegrees(west, south, east, north)
    });
};

GG.viewer.zoomTo2Point = function (x, y) {
    var z = this.camera.positionCartographic.height;
    this.zoomTo3Point(x, y, z);
};

GG.viewer.getCameraPosition = function () {
    var point = {
        longitude: this.camera.positionCartographic.longitude,
        latitude: this.camera.positionCartographic.latitude
    };

    return {
        longitude: Cesium.Math.toDegrees(point.longitude),
        latitude: Cesium.Math.toDegrees(point.latitude)
    };
};


GG.viewer.canvas.addEventListener('touchstart', function (e) {
    alert("PageX: " + e.pageX + "," + e.pageY + "\n LayerX: " + e.layerX + "," + e.layerY + "\n");
    // prevent the browsers default action!
    //e.preventDefault();
    var touch = e.touches[0];
    var c = getCoords(e);
    var x = touch.pageX;
    var y = touch.pageY;
// or taking offset into consideration
    var x_2 = touch.pageX - canvas.offsetLeft;
    var y_2 = touch.pageY - canvas.offsetTop;
    // Get coordinates

    addClick(c.x, c.y, false);
});

