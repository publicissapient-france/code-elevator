angular.module('elevatorApp', ['ngCookies', 'md5', 'ui-gravatar', 'elevatorApp.services'], function ($routeProvider) {
    $routeProvider.
        when('/', {templateUrl: 'partials/elevator.html', controller: ElevatorCtrl}).
        when('/leaderboard', {templateUrl: 'partials/leaderboard.html', controller: LeaderboardCtrl}).
        when('/halloffame',  {templateUrl: 'partials/halloffame.html', controller: HallOfFameCtrl}).
        when('/administration', {templateUrl: 'partials/administration.html', controller: AdministrationCtrl}).
        otherwise({redirectTo: '/'});
});

