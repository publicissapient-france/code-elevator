function AdministrationCtrl($scope, $http, ElevatorAuth, Base64) {

    $scope.loggedIn = ElevatorAuth.loggedIn;
    $scope.maxNumberOfUsers = -1;
    $scope.user = null;
    $scope.password = null;
    $scope.players = [];

    var cookieValue = null;

    $scope.adminAuthorization = function () {
        return $scope.maxNumberOfUsers >= 0;
    };

    $scope.login = function () {
        cookieValue = Base64.encode($scope.user + ":" + $scope.password);
        updateMaxNumberOfUsers('maxNumberOfUsers');
    };

    $scope.logout = function () {
        cookieValue = null;
        $scope.maxNumberOfUsers = -1;
        $scope.errorMessage = '';
        $scope.players = [];
    };

    $scope.increaseMaxNumberOfUsers = function () {
        updateMaxNumberOfUsers('increaseMaxNumberOfUsers');
    };

    $scope.decreaseMaxNumberOfUsers = function () {
        updateMaxNumberOfUsers('decreaseMaxNumberOfUsers');
    };

    var updateMaxNumberOfUsers = function (path) {
        $http({
            'method': 'GET',
            'url': '/resources/admin/' + path,
            'headers': {
                'Authorization': 'Basic ' + cookieValue
            }}).
            success(function (data) {
                $scope.maxNumberOfUsers = data;
                $scope.errorMessage = '';
                fetchPlayers();
            }).
            error(function () {
                $scope.maxNumberOfUsers = -1;
                $scope.errorMessage = 'You are not allowed to access to this page.';
                fetchPlayers();
            });
    };

    $scope.removeElevatorGame = function (email) {
        $http({
            'method': 'GET',
            'url': '/resources/admin/removeElevatorGame?email=' + email,
            'headers': {
                'Authorization': 'Basic ' + cookieValue
            }}).
            success(function () {
                $scope.errorMessage = '';
                fetchPlayers();
            }).
            error(function () {
                $scope.errorMessage = 'Error during removing player.';
                fetchPlayers();
            });
    };

    var fetchPlayers = function () {
        $http.get('/resources/leaderboard')
            .success(function (data) {
                $scope.players = data;
            });
    };

}

AdministrationCtrl.$inject = ['$scope', '$http', 'ElevatorAuth', 'Base64'];
