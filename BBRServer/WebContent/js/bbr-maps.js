/*
   BBR javascript maps 
 */
var bbrMap;

function initMap (locationLat, locationLng) {
    bbrMap = new ymaps.Map('map', {
        center: [locationLat, locationLng],
        zoom: 16,
        controls: ["zoomControl"]
    });
}

function addPosesToMap (coords, bbrIds) {
	var bbrCollection = new ymaps.GeoObjectCollection(null, {
        preset: 'islands#yellowIcon'
    })
    for (var i = 0, l = coords.length; i < l; i++) {
        bbrCollection.add(new ymaps.Placemark(coords[i], {bbrId : bbrIds[i]}));
    }
    bbrMap.geoObjects.add(bbrCollection);
    bbrCollection.events.add('click', function (event) { 
    		alert('Кликнули по желтой метке') 
    	});
}