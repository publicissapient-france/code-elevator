function AdministrationCtrl($scope, $http, ElevatorAuth) {

    $scope.loggedIn = ElevatorAuth.loggedIn;
    $scope.maxNumberOfUsers = -1;
    $scope.basicCookie = null;

    $scope.adminAuthorization = function () {
        return $scope.maxNumberOfUsers >= 0;
    }

    $scope.login = function () {
        updateMaxNumberOfUsers('maxNumberOfUsers');
    }

    $scope.logout = function () {
        $scope.maxNumberOfUsers = -1;
        $scope.errorMessage = '';
    }

    $scope.increaseMaxNumberOfUsers = function () {
        updateMaxNumberOfUsers('increaseMaxNumberOfUsers');
    }

    $scope.decreaseMaxNumberOfUsers = function () {
        updateMaxNumberOfUsers('decreaseMaxNumberOfUsers');
    }

    var updateMaxNumberOfUsers = function (path) {
        $http({
            'method': 'GET',
            'url': '/resources/admin/' + path,
            'headers': {
                'Authorization': 'Basic ' + $scope.basicCookie
            }}).
            success(function (data) {
                $scope.maxNumberOfUsers = data;
                $scope.errorMessage = '';
            }).
            error(function () {
                $scope.maxNumberOfUsers = -1;
                $scope.errorMessage = 'You are not allowed to access to this page.';
            });
    }

}

AdministrationCtrl.$inject = ['$scope', '$http', 'ElevatorAuth'];
