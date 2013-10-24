angular.module('elevatorApp').directive('building', function () {
    return {
        restrict: 'E',
        scope: {
            player: '='
        },
        templateUrl: 'partials/building.html'
    };
});
