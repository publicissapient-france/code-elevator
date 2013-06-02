'use strict';

var module = angular
    .module('elevatorApp', [])
    .directive('building', function () {

        return {
            restrict:'E',
            link:function (scope, element, attrs) {
                scope.$watch(attrs.player, function (player) {

                    var numberOfFloors = 5;
                    var width = 120;
                    var heightOfFloor = 40;
                    var widthOfFloor = 100;
                    var heightOfRoof = 2;
                    var height = numberOfFloors * heightOfFloor + heightOfRoof;

                    var stage = new Kinetic.Stage({
                        container: element[0],
                        width: width,
                        height: height
                    });

                    var layer = new Kinetic.Layer();

                    for (var i = 0; i < numberOfFloors; i++) {
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
                            text: player.peopleWaitingTheElevator[i],
                            fontSize: 30,
                            fontFamily: 'Calibri',
                            fill: 'red'
                        });
                        layer.add(peopleWaitingTheElevator);

                    }

                    var yElevator = heightOfRoof + ((numberOfFloors - 1 - player.elevatorAtFloor) * heightOfFloor);
                    var elevator = new Kinetic.Rect({
                        x: width - 40,
                        y: yElevator,
                        width: 30,
                        height: heightOfFloor,
                        fill: 'lightgrey',
                        stroke: 'black',
                        strokeWidth: 2
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
                });
            }
        };
    });