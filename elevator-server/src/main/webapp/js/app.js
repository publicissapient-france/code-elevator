angular.module('elevatorApp', ['ngCookies', 'md5', 'ui-gravatar', 'elevatorApp.services'], function ($routeProvider) {
    $routeProvider.
        when('/', {templateUrl: 'partials/elevator.html', controller: ElevatorCtrl}).
        when('/leaderboard', {templateUrl: 'partials/leaderboard.html', controller: LeaderboardCtrl}).
        when('/administration', {templateUrl: 'partials/administration.html', controller: AdministrationCtrl}).
        otherwise({redirectTo: '/'});
})
    .directive('building', function () {
        return {
            restrict: 'E',
            link: function (scope, element, attrs) {

                var numberOfFloors = 5;
                var width = 120;
                var heightOfFloor = 40;
                var widthOfFloor = 110;
                var heightOfRoof = 2;
                var height = (numberOfFloors + 1) * heightOfFloor + heightOfRoof;

                var imageObj = new Image();
                imageObj.src = '/img/man.png'

                var stage = new Kinetic.Stage({
                    container: element[0],
                    width: width,
                    height: height
                });

                scope.$watch(attrs.player, function (player) {
                    stage.removeChildren();
                    if (player) {
                        var layer = new Kinetic.Layer();

                        for (var i = 0; i <= numberOfFloors; i++) {

                            var y = heightOfRoof + (i * heightOfFloor);

                            var floor = new Kinetic.Rect({
                                x: 0,
                                y: y,
                                width: widthOfFloor,
                                height: heightOfFloor,
                                fill: 'white',
                                stroke: 'black',
                                strokeWidth: 4
                            });
                            layer.add(floor);

                            var image = new Kinetic.Image({
                                x: 5,
                                y: y + 5,
                                image: imageObj,
                                width: 32,
                                height: 32
                            });
                            layer.add(image);

                            var peopleWaitingTheElevator = new Kinetic.Text({
                                x: 40,
                                y: y + 5,
                                text: player.peopleWaitingTheElevator[numberOfFloors - i],
                                fontSize: 30,
                                fontFamily: 'Calibri',
                                fill: 'red'
                            });
                            layer.add(peopleWaitingTheElevator);

                        }

                        var yElevator = heightOfRoof + ((numberOfFloors - player.elevatorAtFloor) * heightOfFloor);
                        var elevator = new Kinetic.Rect({
                            x: width - 50,
                            y: yElevator,
                            width: 40,
                            height: heightOfFloor,
                            fill: player.doorIsOpen ? 'lightgrey' : 'darkgrey',
                            stroke: 'black',
                            strokeWidth: player.doorIsOpen ? 0 : 4
                        });
                        layer.add(elevator);

                        var peopleInTheElevator = new Kinetic.Text({
                            x: width - 42,
                            y: yElevator + 5,
                            text: player.peopleInTheElevator,
                            fontSize: 30,
                            fontFamily: 'Calibri',
                            fill: 'green'
                        });
                        layer.add(peopleInTheElevator);

                        // add the layer to the stage
                        stage.add(layer);
                    }
                });
            }
        };
    });


