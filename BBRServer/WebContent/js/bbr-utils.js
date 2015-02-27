/*
   BBR javascript utils 
 */
var locationSet = false;

function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
}  

function goBackOrTo(address) {
	if (window.history.length > 0)
		window.history.back();
	else 
		window.location.href = address;
}

function getAndPassGPS() {
	if (!locationSet)
		navigator.geolocation.getCurrentPosition(function(location) {
			$.ajax({
				url: "BBRLocation", 
				data: {
					lat: location.coords.latitude,
					lng: location.coords.longitude
				} 
			}).done(function() {
				 locationSet = true;
			});
		});	
}

getAndPassGPS();