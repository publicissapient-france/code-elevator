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
        $http.get('/resources/new-participant?email=' + $scope.player.email
                + "&pseudo=" + $scope.player.pseudo
                + "&serverURL=" + $scope.player.serverURL).success(function () {

                $http.get('/resources/playerinfo?email=' + $scope.player.email)
                    .success(function (data) {
                           //TODO !!!!
                    });
        });
        $cookieStore.put('isLogged', $scope.player.email);
        $scope.loggedIn = true;
    };

    $scope.disconnect = function () {
        $cookieStore.remove('isLogged');
        $scope.loggedIn = false;
    };
}
ElevatorCtrl.$inject = ['$scope', '$location', '$cookieStore', '$http'];
