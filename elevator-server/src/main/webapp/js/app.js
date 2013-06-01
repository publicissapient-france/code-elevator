angular.module('elevatorApp', ['ngCookies']).
    config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider.
            when('/elevator', {templateUrl: 'partials/elevator.html', controller: ElevatorCtrl}).
            when('/leaderboard', {templateUrl: 'partials/leaderboard.html', controller: LeaderboardCtrl}).
            otherwise({redirectTo: '/elevator'});
        $locationProvider.
            html5Mode(true);
    }]);
