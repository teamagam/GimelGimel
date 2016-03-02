/**
 * Created by Bar on 01-Mar-16.
 */

var GG = GG || {};

GG.Utils = {
    locationsToDegreesArray: function (locations) {
        var degArray = [];
        for (var i = 0; i < locations.length; i++) {
            degArray[i * 2] = locations[i].longitude;
            degArray[i * 2 + 1] = locations[i].latitude;
        }
        return degArray;
    },

    assertIdExists: function (id, collection) {
        if (typeof collection[id] === "undefined") {
            throw new OperationalError("An entity with given id (" + id + ") doesn't exist");
        }
    },

    assertIdNotExists: function (id, collection) {
        if (collection[id]) {
            throw new OperationalError("An entity with given id (" + id + ") already exist");
        }
    }
};
