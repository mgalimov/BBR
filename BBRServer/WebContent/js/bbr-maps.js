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

function addPosesToMap (coords, posIds, fn) {
	var bbrCollection = new ymaps.GeoObjectCollection(null, {
        preset: 'islands#yellowIcon'
    })
    for (var i = 0, l = coords.length; i < l; i++) {
    	var placemark = new ymaps.Placemark(coords[i], {posId: posIds[i]});
        bbrCollection.add(placemark);
    }
    bbrMap.geoObjects.add(bbrCollection);
    bbrCollection.events.add('click', function (e) { 
    	placemark = e.get('target');
    	posId = placemark.properties.get('posId', 0);
    	fn(posId);
    });
}