function IndexCtrl($scope, $location, $cookieStore, $http) {
    if ($cookieStore.get('isLogged') == 'true') {
        $location.path('/elevator');
    }
    $scope.login = function () {
        $http.post('/resources/new-participant?email=' + $scope.email + "&serverURL=" + $scope.serverURL).success(function () {
            $cookieStore.put('isLogged', 'true');
            $location.path('/elevator');
        });
    };
}
IndexCtrl.$inject = ['$scope', '$location', '$cookieStore', '$http'];

function ElevatorCtrl($scope, $location, $cookieStore, $http) {
    $http.get('/resources/emails').success(function (data) {
        console.log(data)
        $scope.emails = data;
    });
    $scope.disconnect = function () {
        $cookieStore.remove('isLogged');
        $location.path('/index.html');
    };
}
ElevatorCtrl.$inject = ['$scope', '$location', '$cookieStore', '$http'];
