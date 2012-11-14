package fr.xebia.elevator;

public enum Instruction {

    open {
        @Override
        public void run(final Elevator elevator) {
            elevator.openDoors();
        }
    }, wait {
        @Override
        public void run(Elevator elevator) {
        }
    }, close {
        @Override
        public void run(Elevator elevator) {
            elevator.closeDoors();
        }
    }, up {
        @Override
        public void run(Elevator elevator) {
            elevator.up();
        }
    }, down {
        @Override
        public void run(Elevator elevator) {
            elevator.down();
        }
    };

    public abstract void run(final Elevator elevator);

}
