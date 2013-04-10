function IndexCtrl($scope, $location, $cookies) {
    if ($cookies.isLogged) {
        $location.path('/elevator');
    }
    $scope.login = function () {
        console.log('login()');
        $location.url('/elevator');
    };
}
IndexCtrl.$inject = ['$scope', '$location', '$cookies'];

function ElevatorCtrl($cookies, $location) {
    $scope.disconnect = function () {
        $cookies.isLogged = false;
        $location.path('/login');
    };
}
ElevatorCtrl.$inject = ['$cookies', '$location'];
