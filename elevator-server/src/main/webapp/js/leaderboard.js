
function LeaderboardCtrl($scope, $http, $timeout) {
    $scope.players = [];

    function fetchLeaderboard($scope, $http, $timeout) {
        (function fetch() {
            $http.get('/resources/leaderboard')
                .success(function (data) {
                    $scope.players = data;
                });
            $timeout(fetch, 1000);
        })();
    }

    fetchLeaderboard($scope, $http, $timeout);

}


LeaderboardCtrl.$inject = ['$scope', '$http', '$timeout'];
