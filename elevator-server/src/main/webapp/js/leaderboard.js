function LeaderboardCtrl($scope, ElevatorAuth, $http, $timeout) {

    $scope.players = [];
    $scope.famousPlayers = [];

    function fetchLeaderboard($scope, $http, $timeout) {
        (function fetch() {
            $http.get('/resources/leaderboard')
                .success(function (data) {
                    $scope.players = data;
                });
            $scope.nextFetchLeaderboard = $timeout(fetch, 1000);
        })();
    }

	function fetchHallOfFame($scope, $http, $timeout){
    	(function fetch(){
    		$http.get('/resources/leaderboard/hallOfFame')
    			.success(function(data){
    				$scope.famousPlayers = data;
    			});
    		$scope.nextFetchHallOfFame = $timeout(fetch, 10000);
    	})();
    }

    fetchLeaderboard($scope, $http, $timeout);
	fetchHallOfFame($scope, $http, $timeout);

    $scope.$on("$destroy", function() {
        $timeout.cancel($scope.nextFetchLeaderboard);
        $timeout.cancel($scope.nextFetchHallOfFame);
    });


    $scope.loggedIn = ElevatorAuth.loggedIn;

}

LeaderboardCtrl.$inject = ['$scope', 'ElevatorAuth', '$http', '$timeout'];