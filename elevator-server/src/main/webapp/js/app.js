angular.module('elevator', ['ngCookies']).
    config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $routeProvider.
            when('/', {templateUrl: 'partials/index.html', controller: IndexCtrl}).
            when('/elevator', {templateUrl: 'partials/elevator.html', controller: ElevatorCtrl}).
            otherwise({redirectTo: '/'});
        $locationProvider.
            html5Mode(true);
    }]);
