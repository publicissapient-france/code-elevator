function HallOfFameCtrl($scope, ElevatorAuth, $http, $timeout) {
//TODO ...

    $scope.players = [];
    $scope.famousPlayers = [];

    function fetchHallOfFame($scope, $http, $timeout){
        (function fetch(){
            $http.get('/resources/leaderboard/hallOfFame')
                .success(function(data){
                    $scope.famousPlayers = data;
                });
            $scope.nextFetchHallOfFame = $timeout(fetch, 15000);
        })();
    }

    fetchHallOfFame($scope, $http, $timeout);

    $scope.$on("$destroy", function() {
        $timeout.cancel($scope.nextFetchHallOfFame);
    });


    $scope.loggedIn = ElevatorAuth.loggedIn;

}

HallOfFameCtrl.$inject = ['$scope', 'ElevatorAuth', '$http', '$timeout'];