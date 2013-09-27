package elevator.scala

import elevator.Command
import elevator.engine.ElevatorEngine
import elevator.engine.ElevatorEngine._
import elevator.User
import elevator.Direction
import scala.collection.immutable.Queue

case class OmnibusElevator(
  floor: Integer = 0,
  direction: Direction = Direction.UP,
  nextCommand: Command = Command.UP) {
  def withFloor(f: Integer) = copy(floor = f)
  def withDirection(d: Direction) = copy(direction = d)
  def withNextState(c: Command) = copy(nextCommand = c)
  def next = OmnibusElevator.this match {
    case OmnibusElevator(_, _, Command.OPEN) => withNextState(Command.CLOSE)
    case OmnibusElevator(HIGHER_FLOOR, _, Command.CLOSE) => withDirection(Direction.DOWN).withNextState(Command.DOWN)
    case OmnibusElevator(LOWER_FLOOR, _, Command.CLOSE) => withDirection(Direction.UP).withNextState(Command.UP)
    case OmnibusElevator(_, Direction.UP, Command.CLOSE) => withNextState(Command.UP)
    case OmnibusElevator(_, Direction.DOWN, Command.CLOSE) => withNextState(Command.DOWN)
    case OmnibusElevator(floor, _, Command.UP) => withFloor(floor + 1) withNextState (Command.OPEN)
    case OmnibusElevator(floor, _, Command.DOWN) => withFloor(floor - 1) withNextState (Command.OPEN)
  }
}

class OmnibusElevatorManager extends ElevatorEngine {

  var elevator = OmnibusElevator(0, Direction.UP, Command.OPEN)

  def nextCommand = {
    val currentState = elevator.nextCommand
    elevator = elevator.next
    currentState
  }

  def call(atFloor: Integer, to: Direction) = {
    if (atFloor > elevator.floor) elevator = elevator withDirection Direction.UP
    if (atFloor < elevator.floor) elevator = elevator withDirection Direction.DOWN
    OmnibusElevatorManager.this
  }

  def go(floorToGo: Integer) = {
    if (floorToGo > elevator.floor) elevator = elevator withDirection Direction.UP
    if (floorToGo < elevator.floor) elevator = elevator withDirection Direction.DOWN
    OmnibusElevatorManager.this
  }

  def userHasEntered(user: User) = OmnibusElevatorManager.this

  def userHasExited(user: User) = OmnibusElevatorManager.this

  def reset(cause: String) = {
    elevator = OmnibusElevator(0, Direction.UP, Command.OPEN)
    OmnibusElevatorManager.this
  }

}