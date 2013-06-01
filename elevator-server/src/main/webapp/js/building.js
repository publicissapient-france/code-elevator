'use strict';

var module = angular
    .module('elevatorApp', [])
    .directive('building', function () {
        return {
            restrict:'E',
            link:function (scope, element, attrs) {

                var scale = 1;
                if (attrs.scale) {
                    scale = attrs.scale;
                }

                var elevatorAtFloor = 0;

                var numberOfFloors = 10;
                var width = 120 * scale;
                var heightOfFloor = 40 * scale;
                var widthOfFloor = 100 * scale;
                var heightOfRoof = 20 * scale;
                var height = numberOfFloors * heightOfFloor + heightOfRoof;


                var stage = new Kinetic.Stage({
                    container: element,
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

                }

                var yElevator = heightOfRoof + (elevatorAtFloor * heightOfFloor);
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
                    text: '2',
                    fontSize: 30,
                    fontFamily: 'Calibri',
                    fill: 'green'
                });
                layer.add(peopleInTheElevator);

                var peopleWaitingTheElevator = new Kinetic.Text({
                    x: width - 60,
                    y: yElevator + 5,
                    text: '2',
                    fontSize: 30,
                    fontFamily: 'Calibri',
                    fill: 'red'
                });
                layer.add(peopleWaitingTheElevator);


                // add the layer to the stage
                stage.add(layer);
            }
        };
    });