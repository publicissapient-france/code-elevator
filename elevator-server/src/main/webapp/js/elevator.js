'use strict';

function ElevatorCtrl($scope, $cookieStore, $http, $timeout) {
    $scope.playerInfo = {
        peopleWaitingTheElevator: []
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
                + "&pseudo=" + $scope.player.pseudo
                + "&serverURL=" + $scope.player.serverURL).success(function () {
                $cookieStore.put('isLogged', $scope.player.email);
                $scope.loggedIn = true;
                fetchPlayerInfo($scope, $http, $timeout);
        });
    };

    $scope.disconnect = function () {
        $http.post('/resources/player/unregister?email=' + $scope.player.email)
            .success(function (data) {
                $cookieStore.remove('isLogged');
                $scope.loggedIn = false;
            });
    };
}
ElevatorCtrl.$inject = ['$scope', '$cookieStore', '$http', '$timeout'];
