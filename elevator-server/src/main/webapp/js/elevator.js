'use strict';

function ElevatorCtrl($scope, $location, $cookieStore, $http) {
    $scope.player = {
        email: $cookieStore.get('isLogged')
    }
    if ($cookieStore.get('isLogged')) {
        $scope.loggedIn = true;
        $scope.player.email = $cookieStore.get('isLogged');
    }

    $scope.login = function () {
        $http.get('/resources/player/register?email=' + $scope.player.email
                + "&pseudo=" + $scope.player.pseudo
                + "&serverURL=" + $scope.player.serverURL).success(function () {
                $cookieStore.put('isLogged', $scope.player.email);
                $scope.loggedIn = true;
                $http.get('/resources/player/info?email=' + $scope.player.email)
                    .success(function (data) {
                           //TODO !!!!
                        console.log(data);
                    });
        });
    };

    $scope.disconnect = function () {
        $http.get('/resources/player/unregister?email=' + $scope.player.email)
            .success(function (data) {
                $cookieStore.remove('isLogged');
                $scope.loggedIn = false;
            });
    };
}
ElevatorCtrl.$inject = ['$scope', '$location', '$cookieStore', '$http'];
