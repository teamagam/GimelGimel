/**
 * Created by Yoni on 3/7/2016.
 */

var GG = GG || {};

GG.Layers = GG.Layers || {};

GG.Layers.KMLLayer = function (id) {
    this._id = id;
    this._dataSource = new Cesium.KmlDataSource();

};

GG.Layers.KMLLayer.prototype.loadKML = function (kmlPath) {
    this._dataSource.load(kmlPath);
};
