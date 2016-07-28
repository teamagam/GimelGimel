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
    //Start in Columbus Viewer
    sceneMode : Cesium.SceneMode.COLUMBUS_VIEW,

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
GG.eventHandler = new GG.EventHandler(GG.viewer);

GG.eventHandler.setSingleTouchActions();
GG.eventHandler.setViewedLocationUpdates(GG.cameraManager);