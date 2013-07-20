function ElevatorCtrl($scope, $cookieStore, $http, $timeout) {
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

    $scope.loggedIn = function () { // TODO extract with other controller
        if ($cookieStore.get('isLogged')) {
            return true;
        }
        return false;
    };

    if ($scope.loggedIn()) {
        $scope.player = $cookieStore.get('isLogged');
    }

    function fetchPlayerInfo($scope, $http, $timeout) {
        (function fetch() {
            if ($scope.loggedIn()) {
                $http.get('/resources/player/info?pseudo=' + $scope.player.pseudo)
                    .success(function (data) {
                        $scope.playerInfo = data;
                    });
                $timeout(fetch, 1000);
            }
        })();
    }

    fetchPlayerInfo($scope, $http, $timeout);

    $scope.login = function () {
        $http.post('/resources/player/register?email=' + $scope.player.email
                + "&pseudo=" + $scope.player.pseudo
                + "&serverURL=http://" + $scope.player["serverURL"])
            .success(function () {
                delete $scope.message;
                $cookieStore.put('isLogged', {
                    "pseudo": $scope.player.pseudo,
                    "email": $scope.player.email
                });
                fetchPlayerInfo($scope, $http, $timeout);
            })
            .error(function (data) {
                $scope.message = data;
            });
    };

    $scope.disconnect = function () {
        $http.post('/resources/player/unregister?pseudo=' + $scope.player.pseudo)
            .success(function () {
                $cookieStore.remove('isLogged');
            });
    };
}

ElevatorCtrl.$inject = ['$scope', '$cookieStore', '$http', '$timeout'];
