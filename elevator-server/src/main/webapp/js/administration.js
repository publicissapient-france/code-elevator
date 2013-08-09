function AdministrationCtrl($scope, ElevatorAuth) {

    $scope.loggedIn = ElevatorAuth.loggedIn;

    var adminAuthorization = false;

    $scope.adminAuthorization = function () {
        return adminAuthorization;
    }

    $scope.login = function () {
        console.log("should login " + $scope.user + " with password " + $scope.password);
        adminAuthorization = true;
    }

    $scope.logout = function () {
        adminAuthorization = false;
    }

    $scope.maxNumberOfUsers = 0;

    $scope.increaseMaxNumberOfUsers = function () {
        $scope.maxNumberOfUsers++;
    }

    $scope.decreaseMaxNumberOfUsers = function () {
        $scope.maxNumberOfUsers--;
    }

}

AdministrationCtrl.$inject = ['$scope', 'ElevatorAuth'];
