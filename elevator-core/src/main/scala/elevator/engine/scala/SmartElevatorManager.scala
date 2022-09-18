package elevator.engine.scala

import elevator.Command._
import elevator.Direction.{DOWN => dDOWN, UP => dUP}
import elevator.engine.ElevatorEngine
import elevator.user.User
import elevator.{Command, Direction}

case class SmartElevator(
  floor: Integer = 0,
  direction: Direction = dUP,
  nextCommand: Command = UP,
  targets: List[Integer] = List[Integer]()) {
  def withFloor(f: Integer): SmartElevator = copy(floor = f)
  def withDirection(d: Direction): SmartElevator = copy(direction = d)
  def withNextState(c: Command): SmartElevator = copy(nextCommand = c)
  def next: SmartElevator = SmartElevator.this match {
    case SmartElevator(_, _      , OPEN, _)                              => withNextState(CLOSE)
    case SmartElevator(_, _      , _   , _) if targets.contains(floor)   => withNextState(OPEN).copy(targets = targets.filter(_ != floor))
    case SmartElevator(_, `dUP`  , _   , _) if targets.exists(_ > floor) => withNextState(UP)  .withFloor(floor + 1)
    case SmartElevator(_, `dUP`  , _   , _) if targets.exists(_ < floor) => withNextState(DOWN).withFloor(floor - 1).withDirection(dDOWN)
    case SmartElevator(_, `dDOWN`, _   , _) if targets.exists(_ < floor) => withNextState(DOWN).withFloor(floor - 1)
    case SmartElevator(_, `dDOWN`, _   , _) if targets.exists(_ > floor) => withNextState(UP)  .withFloor(floor + 1).withDirection(dUP)
    case _                                                               => withNextState(NOTHING)
  }
  def withNewTarget(t: Integer): SmartElevator = copy(targets = (targets :+ t).distinct)
}

class SmartElevatorManager extends ElevatorEngine {

  private var elevator = SmartElevator(0, dUP, OPEN)

  def nextCommand: Command = {
    val currentState = elevator.nextCommand
    elevator = elevator.next
    currentState
  }

  def call(atFloor: Integer, to: Direction): SmartElevatorManager = {
    elevator = elevator.withNewTarget(atFloor)
    this
  }

  def go(floorToGo: Integer): SmartElevatorManager = {
    elevator = elevator.withNewTarget(floorToGo)
    this
  }

  def userHasEntered(user: User): SmartElevatorManager = this

  def userHasExited(user: User): SmartElevatorManager = this

  def reset(cause: String): SmartElevatorManager = {
    elevator = SmartElevator(0, Direction.UP, OPEN)
    SmartElevatorManager.this
  }

}