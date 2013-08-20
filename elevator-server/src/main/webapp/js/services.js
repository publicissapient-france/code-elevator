angular.module('elevatorApp.services', ['ngCookies']).
    factory('ElevatorAuth',function ($cookieStore, $http, Base64) {
        return {
            "loggedIn": function () {
                if ($cookieStore.get('isLogged')) {
                    return true;
                }
                return false;
            },
            "player": function () {
                if (!this.loggedIn()) {
                    throw "not logged in";
                }
                return $cookieStore.get('isLogged');
            },
            "playerInfo": function () {
                if (!this.loggedIn()) {
                    throw "not logged in";
                }
                return $http({
                    'method': 'GET',
                    'url': '/resources/player/info?email=' + this.player().email,
                    'headers': {
                        'Authorization': 'Basic ' + $cookieStore.get('isLogged').cookieValue
                    }
                }).
                    error(function () {
                        $cookieStore.remove('isLogged');
                    });
            },
            "register": function (player) {
                return $http.post('/resources/player/register?email=' + player.email
                        + "&pseudo=" + player.pseudo
                        + "&serverURL=http://" + player.serverURL).
                    success(function (data) {
                        $cookieStore.put('isLogged', {
                            "pseudo": player.pseudo,
                            "email": player.email,
                            "cookieValue": Base64.encode(player.email + ":" + data)
                        });
                    })
            },
            "unregister": function (player) {
                $http({
                    'method': 'POST',
                    'url': '/resources/player/unregister?email=' + player.email,
                    'headers': {
                        'Authorization': 'Basic ' + $cookieStore.get('isLogged').cookieValue
                    }
                }).
                    success(function () {
                        $cookieStore.remove('isLogged');
                    }).
                    error(function () {
                        $cookieStore.remove('isLogged');
                    });
            }
        };
    }).
    factory('Base64', function () {
        // based on http://www.webtoolkit.info/javascript-base64.html
        return {
            "_keyStr" : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
            "encode" : function (input) {
                var output = "";
                var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
                var i = 0;

                input = this._utf8_encode(input);

                while (i < input.length) {
                    chr1 = input.charCodeAt(i++);
                    chr2 = input.charCodeAt(i++);
                    chr3 = input.charCodeAt(i++);

                    enc1 = chr1 >> 2;
                    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                    enc4 = chr3 & 63;

                    if (isNaN(chr2)) {
                        enc3 = enc4 = 64;
                    } else if (isNaN(chr3)) {
                        enc4 = 64;
                    }

                    output = output +
                        this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
                        this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
                }

                return output;
            },
            "_utf8_encode" : function (string) {
                string = string.replace(/\r\n/g,"\n");
                var utftext = "";

                for (var n = 0; n < string.length; n++) {
                    var c = string.charCodeAt(n);

                    if (c < 128) {
                        utftext += String.fromCharCode(c);
                    }
                    else if((c > 127) && (c < 2048)) {
                        utftext += String.fromCharCode((c >> 6) | 192);
                        utftext += String.fromCharCode((c & 63) | 128);
                    }
                    else {
                        utftext += String.fromCharCode((c >> 12) | 224);
                        utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                        utftext += String.fromCharCode((c & 63) | 128);
                    }
                }

                return utftext;
            }
        };
    });