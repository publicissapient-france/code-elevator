function ElevatorCtrl($scope, $timeout, $http, ElevatorAuth) {
    $scope.player = {};

    $scope.playerInfo = {
        pseudo: "",
        score: 0,
        peopleWaitingTheElevator: Array.apply(null, new Array(6)).map(Number.prototype.valueOf, 0),
        elevatorAtFloor: 0,
        peopleInTheElevator: 0,
        doorIsOpen: false,
        lastErrorMessage: null,
        state: 'RESUME'
    };

    $scope.loggedIn = ElevatorAuth.loggedIn;

    if ($scope.loggedIn()) {
        $scope.player = ElevatorAuth.player();
    }

    function fetchPlayerInfo($scope, ElevatorAuth, $timeout) {
        (function fetch() {
            if ($scope.loggedIn()) {
                ElevatorAuth.playerInfo()
                    .success(function (data) {
                        $scope.playerInfo = data;
                    });
                $scope.nextFetchPlayerInfo = $timeout(fetch, 1000);
            }
        })();
    }

    fetchPlayerInfo($scope, ElevatorAuth, $timeout);

    $scope.login = function () {
        ElevatorAuth.register($scope.player)
            .success(function () {
                delete $scope.message;
                $scope.player = ElevatorAuth.player();
                fetchPlayerInfo($scope, ElevatorAuth, $timeout);
            })
            .error(function (data) {
                $scope.message = data;
            });
    };

    $scope.disconnect = function () {
        ElevatorAuth.unregister($scope.player);
    };

    $scope.reset = function () {
        $http({
            'method': 'POST',
            'url': '/resources/player/reset?email=' + $scope.player.email,
            'headers': {
                'Authorization': 'Basic ' + $scope.player.cookieValue
            }
        });
    };

    $scope.pause = function () {
        $http({
            'method': 'POST',
            'url': '/resources/player/pause?email=' + $scope.player.email,
            'headers': {
                'Authorization': 'Basic ' + $scope.player.cookieValue
            }
        }).
            success(function () {
                $scope.playerInfo.state = 'PAUSE';
            });
    };

    $scope.resume = function () {
        $http({
            'method': 'POST',
            'url': '/resources/player/resume?email=' + $scope.player.email,
            'headers': {
                'Authorization': 'Basic ' + $scope.player.cookieValue
            }
        }).
            success(function () {
                $scope.playerInfo.state = 'RESUME';
            });
    };

    $scope.$on("$destroy", function () {
        $timeout.cancel($scope.nextFetchPlayerInfo);
    });
}

ElevatorCtrl.$inject = ['$scope', '$timeout', '$http', 'ElevatorAuth'];
