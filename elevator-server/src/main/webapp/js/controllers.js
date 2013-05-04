function IndexCtrl($scope, $location, $cookieStore, $http) {
    if ($cookieStore.get('isLogged') == 'true') {
        $location.path('/elevator');
    }
    $scope.login = function () {
        $http.post('/new-participant?email=' + $scope.email + "&serverURL=" + $scope.serverURL).success(function () {
            $cookieStore.put('isLogged', 'true');
            $location.path('/elevator');
        });
    };
}
IndexCtrl.$inject = ['$scope', '$location', '$cookieStore', '$http'];

function ElevatorCtrl($scope, $location, $cookieStore) {
    $scope.disconnect = function () {
        $cookieStore.remove('isLogged');
        $location.path('/');
    };
}
ElevatorCtrl.$inject = ['$scope', '$location', '$cookieStore'];
