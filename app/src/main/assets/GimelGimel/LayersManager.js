/**
 * Created by Bar on 01-Mar-16.
 */

var GG = GG || {};

GG.Layers = GG.Layers || {};

GG.Layers.LayersManager = function(viewer){
    this._viewer = viewer;
    this._layers = {};
};


GG.Layers.LayersManager.prototype.addLayer = function(vectorLayer){
    GG.Utils.assertIdNotExists(vectorLayer._id, this._layers);

    this._layers[vectorLayer._id] = vectorLayer;
    this._viewer.dataSources.add(vectorLayer._dataSource);

};

GG.Layers.LayersManager.prototype.removeLayer = function(vectorLayerId){
    GG.Utils.assertIdNotExists(vectorLayerId, this._layers);

    var vectorLayer = this._layers[vectorLayerId];

    this._layers.remove(vectorLayer);
    this._viewer.dataSources.remove(vectorLayer._dataSources);
};

GG.Layers.LayersManager.prototype.getLayer = function(id){
  return this._layers[id];
};
