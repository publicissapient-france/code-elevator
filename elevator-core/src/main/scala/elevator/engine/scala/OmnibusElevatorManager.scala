package elevator.engine.scala

import elevator.Command._
import elevator.{Command, Direction}
import elevator.Direction.{DOWN => dDOWN, UP => dUP}
import elevator.engine.ElevatorEngine
import elevator.engine.ElevatorEngine._
import elevator.user.User

case class OmnibusElevator(
  floor: Integer = 0,
  direction: Direction = dUP,
  nextCommand: Command = UP) {
  def withFloor(f: Int) = copy(floor = f)
  def withDirection(d: Direction) = copy(direction = d)
  def withNextState(c: Command) = copy(nextCommand = c)
  def next = OmnibusElevator.this match {
    case OmnibusElevator(_           , _      , OPEN ) => withNextState(CLOSE)
    case OmnibusElevator(HIGHER_FLOOR, _      , CLOSE) => withDirection(dDOWN).withNextState(DOWN)
    case OmnibusElevator(LOWER_FLOOR , _      , CLOSE) => withDirection(dUP).withNextState(UP)
    case OmnibusElevator(_           , `dUP`  , CLOSE) => withNextState(UP)
    case OmnibusElevator(_           , `dDOWN`, CLOSE) => withNextState(DOWN)
    case OmnibusElevator(_           , _      , UP   ) => withFloor(floor + 1) withNextState OPEN
    case OmnibusElevator(_           , _      , DOWN ) => withFloor(floor - 1) withNextState OPEN
  }
}

class OmnibusElevatorManager extends ElevatorEngine {

  var elevator = OmnibusElevator(0, dUP, OPEN)

  def nextCommand = {
    val currentState = elevator.nextCommand
    elevator = elevator.next
    currentState
  }

  def call(atFloor: Integer, to: Direction) = {
    if (atFloor > elevator.floor) elevator = elevator withDirection dUP
    if (atFloor < elevator.floor) elevator = elevator withDirection dDOWN
    OmnibusElevatorManager.this
  }

  def go(floorToGo: Integer) = {
    if (floorToGo > elevator.floor) elevator = elevator withDirection dUP
    if (floorToGo < elevator.floor) elevator = elevator withDirection dDOWN
    OmnibusElevatorManager.this
  }

  def userHasEntered(user: User) = OmnibusElevatorManager.this

  def userHasExited(user: User) = OmnibusElevatorManager.this

  def reset(cause: String) = {
    elevator = OmnibusElevator(0, dUP, OPEN)
    OmnibusElevatorManager.this
  }

}