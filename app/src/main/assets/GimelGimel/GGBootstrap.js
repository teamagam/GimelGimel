/**
 * bootstraps end-points between js and java code
 */


var GG = GG || {};

GG.viewer = new Cesium.Viewer('cesiumContainer', {
    imageryProvider : new Cesium.ArcGisMapServerImageryProvider({
        url : 'http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer'
    }),
    terrainProvider: new Cesium.CesiumTerrainProvider({
        url: 'http://assets.agi.com/stk-terrain/world'
    }),
    //sceneMode: Cesium.SceneMode.SCENE2D,
    baseLayerPicker: false,
    fullScreenButton: true,
    homeButton: false,
    timeline: false,
    sceneModePicker: false,
    navigationHelpButton: false,
    selectionIndicator: false,
    infoBox: false,
    geocoder: false,
    animation: false,
    creditContainer: "creditHiddenContainer"
});

//for some unknown reason, when using cross-walk, the cesium still creates fullscreenButton.
//We manually are deleting it.
//To make sure the button exists, we set fullScreenButton attr to be true.
GG.viewer.fullscreenButton.destroy()

GG.layerManager = new GG.Layers.LayersManager(GG.viewer);
GG.cameraManager = new GG.CameraManager(GG.viewer.camera);

//Set mouse move event listener
//Cesium.ScreenSpaceEventType.LEFT_DOWN is mapped to touch events.
GG.Utils.setScreenSpaceEventAction(GG.viewer, Cesium.ScreenSpaceEventType.LEFT_DOWN, function (movement) {
    var cartesian = GG.viewer.camera.pickEllipsoid(movement.position, GG.viewer.scene.globe.ellipsoid);
    if (cartesian) {
        var cartographic = Cesium.Cartographic.fromCartesian(cartesian);
        var longitude = Cesium.Math.toDegrees(cartographic.longitude);
        var latitude = Cesium.Math.toDegrees(cartographic.latitude);

        GG.AndroidAPI.updateSelectedLocation({
            longitude: longitude,
            latitude: latitude
        });
    }
});

// Call the android API when the scene has changed
GG.viewer.camera.moveEnd.addEventListener(function() {
    var currentLocation = GG.cameraManager.getCameraPosition();

    GG.AndroidAPI.updateViewedLocation(currentLocation);
});

