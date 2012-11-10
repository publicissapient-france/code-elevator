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
    };

    public abstract void run(final Elevator elevator);

}
