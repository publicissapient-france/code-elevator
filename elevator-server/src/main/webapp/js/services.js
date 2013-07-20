angular.module('elevatorApp.services', ['ngCookies']).
    factory('ElevatorAuth', function ($cookieStore, $http) {
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
                return $http.get('/resources/player/info?pseudo=' + this.player().pseudo);
            },
            "register": function (player) {
                return $http.post('/resources/player/register?email=' + player.email
                        + "&pseudo=" + player.pseudo
                        + "&serverURL=http://" + player.serverURL).
                    success(function () {
                        $cookieStore.put('isLogged', {
                            "pseudo": player.pseudo,
                            "email": player.email
                        });
                    })
            },
            "unregister": function (player) {
                $http.post('/resources/player/unregister?pseudo=' + player.pseudo)
                    .success(function () {
                        $cookieStore.remove('isLogged');
                    });
            }
        };
    });