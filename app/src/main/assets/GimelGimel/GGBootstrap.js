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

GG.layerManager = new GG.Layers.LayersManager(GG.viewer);
GG.cameraManager = new GG.CameraManager(GG.viewer.camera);


//Notify Android app viewer is ready
GG.AndroidAPI.onReady();
