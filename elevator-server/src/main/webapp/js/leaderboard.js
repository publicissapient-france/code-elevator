'use strict';

function LeaderboardCtrl($scope, $timeout) {
        $scope.players = [];
        $scope.numberOfFloors = 5;

        var numberOfPlayers = 20;

        for (var i = 0; i < numberOfPlayers; i++) {
            var player = {
                name: 'Player ' + i,
                score: 0,
                elevatorAtFloor: $scope.numberOfFloors - 1,
                peopleInTheElevator: Math.floor(Math.random() * 4)
            }

            var peopleWaitingTheElevator = [];
            for (var j = 0; j < $scope.numberOfFloors; j++) {
                peopleWaitingTheElevator.push(Math.floor(Math.random() * 4));
            }
            player.peopleWaitingTheElevator = peopleWaitingTheElevator;

            $scope.players.push(player);
        }


        randomizeScore($scope, $timeout);

        setInterval(function(){
        },3000);


    };

LeaderboardCtrl.$inject = ['$scope', '$timeout'];


function randomizeScore($scope, $timeout) {
    (function randomize() {
        for (var i = 0; i < $scope.players.length; i++) {
            var player = $scope.players[i];
            player.score = Math.floor(Math.random() * 10);
            player.elevatorAtFloor = Math.floor(Math.random() * 4);

            var peopleWaitingTheElevator = [];
            for (var j = 0; j < $scope.numberOfFloors; j++) {
                peopleWaitingTheElevator.push(Math.floor(Math.random() * 4));
            }
            player.peopleWaitingTheElevator = peopleWaitingTheElevator;

        }

        $timeout(randomize, 1000);
    })();
}
