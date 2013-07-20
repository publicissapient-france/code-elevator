angular.module('elevatorApp.services', ['ngCookies']).
    factory('ElevatorAuth', function ($cookieStore) {
        return {
            "loggedIn": function () {
                if ($cookieStore.get('isLogged')) {
                    return true;
                }
                return false;
            }
        };
    });