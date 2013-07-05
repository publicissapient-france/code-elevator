'use strict';

function ElevatorCtrl($scope, $cookieStore, $http, $timeout) {
    $scope.player = {};

    $scope.playerInfo = {
        email: "",
        score: 0,
        peopleWaitingTheElevator: Array.apply(null, new Array(6)).map(Number.prototype.valueOf, 0),
        elevatorAtFloor: 0,
        peopleInTheElevator: 0,
        doorIsOpen: false
    };

    if ($cookieStore.get('isLogged')) {
        $scope.loggedIn = true;
        $scope.player.email = $cookieStore.get('isLogged');
    }

    function fetchPlayerInfo($scope, $http, $timeout) {
        (function fetch() {
            if ($scope.loggedIn) {
                $http.get('/resources/player/info?email=' + $scope.player.email)
                    .success(function (data) {
                        $scope.playerInfo = data;
                    });
                $timeout(fetch, 1000);
            }
        })();
    }

    fetchPlayerInfo($scope, $http, $timeout);

    $scope.login = function () {
        $http.post('/resources/player/register?email=' + $scope.player.email
                + "&pseudo=" + $scope.player["pseudo"]
                + "&serverURL=http://" + $scope.player["serverURL"])
            .success(function () {
                delete $scope.message;
                $cookieStore.put('isLogged', $scope.player.email);
                $scope.loggedIn = true;
                fetchPlayerInfo($scope, $http, $timeout);
            })
            .error(function (data) {
                $scope.message = data;
            });
    };

    $scope.disconnect = function () {
        $http.post('/resources/player/unregister?email=' + $scope.player.email)
            .success(function () {
                $cookieStore.remove('isLogged');
                $scope.loggedIn = false;
            });
    };
}
ElevatorCtrl.$inject = ['$scope', '$cookieStore', '$http', '$timeout'];
