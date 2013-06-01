'use strict';

function ElevatorCtrl($scope, $location, $cookieStore, $http) {
    $scope.isLogged = true;
    if ($cookieStore.get('isLogged') == 'true') {
        $scope.isLogged = true;
    }

    $scope.login = function () {
        $http.post('/resources/new-participant?email=' + $scope.email + "&pseudo=" + $scope.pseudo + "&serverURL=" + $scope.serverURL).success(function () {
            $cookieStore.put('isLogged', 'true');
            $scope.isLogged = true;
        });
    };

    $scope.disconnect = function () {
        $cookieStore.remove('isLogged');
        $scope.isLogged = false;
    };
}
ElevatorCtrl.$inject = ['$scope', '$location', '$cookieStore', '$http'];
