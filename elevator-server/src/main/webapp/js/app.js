angular.module('elevatorApp', ['ngCookies']).
    config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider.
            when('/elevator.html', {templateUrl: 'partials/elevator.html', controller: ElevatorCtrl}).
            when('/leaderboard.html', {templateUrl: 'partials/leaderboard.html', controller: LeaderboardCtrl}).
            otherwise({redirectTo: '/elevator.html'});
        $locationProvider.
            html5Mode(true);
    }]);
