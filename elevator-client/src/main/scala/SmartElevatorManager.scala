package elevator.scala

import elevator.Command
import elevator.engine.ElevatorEngine
import elevator.engine.ElevatorEngine._
import elevator.User
import elevator.Direction
import scala.collection.immutable.Queue

case class SmartElevator(
  floor: Integer = 0,
  direction: Direction = Direction.UP,
  nextCommand: Command = Command.UP,
  targets: List[Integer] = List[Integer]()) {
  def withFloor(f: Integer) = copy(floor = f)
  def withDirection(d: Direction) = copy(direction = d)
  def withNextState(c: Command) = copy(nextCommand = c)
  def next = SmartElevator.this match {
    case SmartElevator(_, _, Command.OPEN, _) => withNextState(Command.CLOSE)
    case SmartElevator(f, _, _, t) if t.contains(f) => withNextState(Command.OPEN).copy(targets = targets.filter(_ != f))
    case SmartElevator(f, Direction.UP, _, t) if t.filter(_ > f).nonEmpty => withNextState(Command.UP) withFloor (f + 1)
    case SmartElevator(f, Direction.UP, _, t) if t.filter(_ < f).nonEmpty => withNextState(Command.DOWN) withFloor (f - 1) withDirection (Direction.DOWN)
    case SmartElevator(f, Direction.DOWN, _, t) if t.filter(_ < f).nonEmpty => withNextState(Command.DOWN) withFloor (f - 1)
    case SmartElevator(f, Direction.DOWN, _, t) if t.filter(_ > f).nonEmpty => withNextState(Command.UP) withFloor (f + 1) withDirection (Direction.UP)
    case _ => withNextState(Command.NOTHING)
  }
  def withNewTarget(t: Integer) = copy(targets = (targets :+ t).removeDuplicates)
}

class SmartElevatorManager extends ElevatorEngine {

  var elevator = SmartElevator(0, Direction.UP, Command.OPEN)

  def nextCommand = {
    val currentState = elevator.nextCommand
    elevator = elevator.next
    currentState
  }

  def call(atFloor: Integer, to: Direction) = {
    elevator = elevator withNewTarget atFloor
    this
  }

  def go(floorToGo: Integer) = {
    elevator = elevator withNewTarget floorToGo
    this
  }

  def userHasEntered(user: User) = this

  def userHasExited(user: User) = SmartElevatorManager.this

  def reset(cause: String) = {
    elevator = SmartElevator(0, Direction.UP, Command.OPEN)
    SmartElevatorManager.this
  }

}