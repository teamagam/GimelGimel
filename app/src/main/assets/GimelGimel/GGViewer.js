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

// 1. Fly to a position with a top-down view
GG.viewer.zoomTo3Point = function(x,y,z){
    this.camera.flyTo({
        destination : Cesium.Cartesian3.fromDegrees(x,y,z)
    });
};

// 2. Fly to a Rectangle with a top-down view
GG.viewer.zoomToRectangle = function(west, south, east, north) {
    this.camera.flyTo({
        destination: Cesium.Rectangle.fromDegrees(west, south, east, north)
    });
};

