package fr.xebia.elevator;

public enum Instruction {

    open {
        @Override
        public void run(final Elevator elevator) {
            elevator.openDoors();
        }
    };

    public abstract void run(final Elevator elevator);

}
