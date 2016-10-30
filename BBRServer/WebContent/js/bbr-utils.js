/*
   BBR javascript utils 
 */
var locationSet = false;

function getUrlParameter(sParam) {
    var sPageURL = unescape(decodeURIComponent(window.location.search.substring(1)));
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

function goToGrid(address) {
/*	if (document.referrer)
		window.location.href = document.referrer; 
	else
		goBackOrTo(address);*/
	if (address == "source") {
		address = document.referrer;
	}
	window.location.href = address;
}

function goBackOrTo(address) {
	if (window.history.length > 0)
		window.history.back();
	else 
		window.location.href = address;
}

function reloadWithNewParam(paramString) {
	window.location.href = window.location.href.split("?")[0] + "?" + paramString;
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

function getCookie(name) {
  var matches = document.cookie.match(new RegExp(
    "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
  ));
  return matches ? decodeURIComponent(matches[1]) : undefined;
}

function setCookie(name, value, options) {
	  options = options || {};

	  var expires = options.expires;

	  if (typeof expires == "number" && expires) {
	    var d = new Date();
	    d.setTime(d.getTime() + expires * 1000);
	    expires = options.expires = d;
	  }
	  if (expires && expires.toUTCString) {
	    options.expires = expires.toUTCString();
	  }

	  value = encodeURIComponent(value);

	  var updatedCookie = name + "=" + value;

	  for (var propName in options) {
	    updatedCookie += "; " + propName;
	    var propValue = options[propName];
	    if (propValue !== true) {
	      updatedCookie += "=" + propValue;
	    }
	  }

	  document.cookie = updatedCookie;
}

function deleteCookie(name) {
	  setCookie(name, "", {
	    expires: -1
	  })
}