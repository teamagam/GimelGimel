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



