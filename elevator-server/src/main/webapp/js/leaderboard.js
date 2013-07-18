function LeaderboardCtrl($scope, $cookieStore, $http, $timeout) {

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

    $scope.loggedIn = function () {
        if ($cookieStore.get('isLogged')) {
            return true;
        }
        return false;
    }

}

LeaderboardCtrl.$inject = ['$scope', '$cookieStore', '$http', '$timeout'];
