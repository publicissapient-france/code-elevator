package elevator.user;

class Assertions {

    static FloorsAndDirectionAssert assertThat(FloorsAndDirection actual) {
        return new FloorsAndDirectionAssert(actual);
    }

}
