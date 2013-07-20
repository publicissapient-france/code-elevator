function ElevatorCtrl($scope, $timeout, ElevatorAuth) {
    $scope.player = {};

    $scope.playerInfo = {
        pseudo: "",
        score: 0,
        peopleWaitingTheElevator: Array.apply(null, new Array(6)).map(Number.prototype.valueOf, 0),
        elevatorAtFloor: 0,
        peopleInTheElevator: 0,
        doorIsOpen: false,
        lastErrorMessage: null
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
                fetchPlayerInfo($scope, ElevatorAuth, $timeout);
            })
            .error(function (data) {
                $scope.message = data;
            });
    };

    $scope.disconnect = function () {
        ElevatorAuth.unregister($scope.player);
    };

    $scope.$on("$destroy", function() {
        $timeout.cancel($scope.nextFetchPlayerInfo);
    });
}

ElevatorCtrl.$inject = ['$scope', '$timeout', 'ElevatorAuth'];
