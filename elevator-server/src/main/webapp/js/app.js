angular.module('elevatorApp', ['ngCookies', 'md5', 'ui-gravatar']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.
            when('/', {templateUrl: 'partials/elevator.html', controller: ElevatorCtrl}).
            when('/leaderboard', {templateUrl: 'partials/leaderboard.html', controller: LeaderboardCtrl}).
            otherwise({redirectTo: '/'});
    }])
    .directive('building', function () {
        return {
            restrict:'E',
            link:function (scope, element, attrs) {

                var numberOfFloors = 5;
                var width = 120;
                var heightOfFloor = 40;
                var widthOfFloor = 110;
                var heightOfRoof = 2;
                var height = (numberOfFloors + 1) * heightOfFloor + heightOfRoof;

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
                                x: 10,
                                y: y,
                                width: widthOfFloor,
                                height: heightOfFloor,
                                fill: 'white',
                                stroke: 'black',
                                strokeWidth: 4
                            });
                            layer.add(floor);

                            for (var j = 0; j < 1; j++) {
                                var roundWindow = new Kinetic.Circle({
                                    x: 30 + (j * 30),
                                    y: y + 20,
                                    radius: 8,
                                    fill: 'blue',
                                    stroke: 'black',
                                    strokeWidth: 2
                                });
                                layer.add(roundWindow);
                            }

                            var peopleWaitingTheElevator = new Kinetic.Text({
                                x: width - 60,
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
                            x: width - 40,
                            y: yElevator,
                            width: 40,
                            height: heightOfFloor,
                            fill: player.doorIsOpen ? 'lightgrey' : 'darkgrey',
                            stroke: 'black',
                            strokeWidth: player.doorIsOpen ? 0 : 4
                        });
                        layer.add(elevator);

                        var peopleInTheElevator = new Kinetic.Text({
                            x: width - 32,
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
