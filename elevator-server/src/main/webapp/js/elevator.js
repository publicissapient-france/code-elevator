const NUMBER_OF_FLOORS = 6;
const LINE_COLOR = 'rgb(96, 96, 96)';

const img = new Image();
img.src = "smile.png";

var Elevator = function (canvasId) {
    this.stage = new createjs.Stage(canvasId);

    for (var floorNumber = 1; floorNumber <= NUMBER_OF_FLOORS; floorNumber++) {
        this.drawFloor(floorNumber);
    }

    this.drawElevator();

    createjs.Ticker.addEventListener("tick", this.stage);
    createjs.Ticker.setFPS(30);
    createjs.Ticker.useRAF = true;

    this.addKeyListeners();
}

Elevator.prototype.addKeyListeners = function () {
    var elevator = this;

    var previousOnKeyDownFunction = document.onkeydown;

    document.onkeydown = function (e) {
        if (typeof previousOnKeyDownFunction == 'function') {
            previousOnKeyDownFunction(e);
        }
        if (e.keyCode == 38) {
            elevator.move(1);
        } else if (e.keyCode == 40) {
            elevator.move(-1);
        }
    }
}

Elevator.prototype.move = function (direction) {
    createjs.Tween.get(this.shape, {}).to({y: this.shape.y + this.elevatorHeight() * -direction}, 1000, createjs.Ease.sineInOut);
}

Elevator.prototype.drawFloor = function (floorNumber) {
    var x = Math.floor(this.stage.canvas.width * .25);
    var y = Math.floor(this.elevatorHeight() * floorNumber);

    var floor = new createjs.Graphics().beginStroke(LINE_COLOR)
        .moveTo(x, y)
        .lineTo(this.stage.canvas.width, y)
        .endStroke();

    this.stage.addChild(new createjs.Shape(floor));
}

Elevator.prototype.drawElevator = function () {
    var x = Math.floor(this.stage.canvas.width * .05);
    var y = this.stage.canvas.height - this.elevatorHeight();
    var rect = new createjs.Graphics().beginStroke(LINE_COLOR).drawRect(
        x,
        y,
        Math.floor(this.stage.canvas.width * .2),
        this.elevatorHeight()
    );
    this.shape = new createjs.Shape(rect);

    this.stage.addChild(this.shape);
}

Elevator.prototype.elevatorHeight = function () {
    return this.stage.canvas.height / (NUMBER_OF_FLOORS + 1);
}

Elevator.prototype.drawNewUser = function (floorNumber, rank) {
    var user = new createjs.Bitmap(img);
    user.x = Math.floor(this.stage.canvas.width * .30 + (img.width + img.width / 5) * rank);
    user.y = Math.floor(this.elevatorHeight() * (NUMBER_OF_FLOORS - floorNumber)) - img.height;
    this.stage.addChild(user);
}
