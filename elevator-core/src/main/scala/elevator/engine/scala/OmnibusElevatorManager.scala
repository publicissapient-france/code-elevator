package elevator.engine.scala

import elevator.Command._
import elevator.Direction.{DOWN => dDOWN, UP => dUP}
import elevator.engine.ElevatorEngine
import elevator.engine.ElevatorEngine._
import elevator.user.User
import elevator.{Command, Direction}

case class OmnibusElevator(
  floor: Integer = 0,
  direction: Direction = dUP,
  nextCommand: Command = UP) {
  def withFloor(f: Int): OmnibusElevator = copy(floor = f)
  def withDirection(d: Direction): OmnibusElevator = copy(direction = d)
  def withNextState(c: Command): OmnibusElevator = copy(nextCommand = c)
  def next: OmnibusElevator = OmnibusElevator.this match {
    case OmnibusElevator(_           , _      , OPEN   ) => withNextState(CLOSE)
    case OmnibusElevator(HIGHER_FLOOR, _      , CLOSE  ) => withDirection(dDOWN).withNextState(DOWN)
    case OmnibusElevator(LOWER_FLOOR , _      , CLOSE  ) => withDirection(dUP).withNextState(UP)
    case OmnibusElevator(_           , `dUP`  , CLOSE  ) => withNextState(UP)
    case OmnibusElevator(_           , `dDOWN`, CLOSE  ) => withNextState(DOWN)
    case OmnibusElevator(_           , _      , UP     ) => withFloor(floor + 1).withNextState(OPEN)
    case OmnibusElevator(_           , _      , DOWN   ) => withFloor(floor - 1).withNextState(OPEN)
    case OmnibusElevator(_           , _      , NOTHING) => this
  }
}

class OmnibusElevatorManager extends ElevatorEngine {

  private var elevator = OmnibusElevator(0, dUP, OPEN)

  def nextCommand: Command = {
    val currentState = elevator.nextCommand
    elevator = elevator.next
    currentState
  }

  def call(atFloor: Integer, to: Direction): OmnibusElevatorManager = {
    if (atFloor > elevator.floor) elevator = elevator withDirection dUP
    if (atFloor < elevator.floor) elevator = elevator withDirection dDOWN
    OmnibusElevatorManager.this
  }

  def go(floorToGo: Integer): OmnibusElevatorManager = {
    if (floorToGo > elevator.floor) elevator = elevator withDirection dUP
    if (floorToGo < elevator.floor) elevator = elevator withDirection dDOWN
    OmnibusElevatorManager.this
  }

  def userHasEntered(user: User): OmnibusElevatorManager = OmnibusElevatorManager.this

  def userHasExited(user: User): OmnibusElevatorManager = OmnibusElevatorManager.this

  def reset(cause: String): OmnibusElevatorManager = {
    elevator = OmnibusElevator(0, dUP, OPEN)
    OmnibusElevatorManager.this
  }

}