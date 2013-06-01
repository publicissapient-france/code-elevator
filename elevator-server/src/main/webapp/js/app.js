angular.module('elevatorApp', ['ngCookies', 'md5', 'ui-gravatar']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/', {templateUrl: 'partials/elevator.html', controller: ElevatorCtrl}).
            when('/leaderboard', {templateUrl: 'partials/leaderboard.html', controller: LeaderboardCtrl}).
            otherwise({redirectTo: '/'});
    }]);
