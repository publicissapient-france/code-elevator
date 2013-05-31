'use strict';

function ElevatorCtrl($scope, $location, $cookieStore, $http) {
    if ($cookieStore.get('isLogged') == 'true') {
        $location.path('/elevator');
    }

    $scope.login = function () {
        $http.post('/resources/new-participant?email=' + $scope.email + "&pseudo=" + $scope.pseudo + "&serverURL=" + $scope.serverURL).success(function () {
            $cookieStore.put('isLogged', 'true');
            $location.path('/elevator');
        });
    };

    $scope.disconnect = function () {
        $cookieStore.remove('isLogged');
        $location.path('/index.html');
    };
}
ElevatorCtrl.$inject = ['$scope', '$location', '$cookieStore', '$http'];
