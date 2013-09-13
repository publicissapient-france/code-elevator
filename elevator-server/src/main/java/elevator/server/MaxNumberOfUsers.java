package elevator.server;

import static java.lang.Math.max;

class MaxNumberOfUsers implements elevator.MaxNumberOfUsers {

    private Integer value;

    MaxNumberOfUsers(Integer numberOfUsers) {
        this.value = numberOfUsers;
    }

    MaxNumberOfUsers() {
        this.value = 3;
    }

    @Override
    public Integer value() {
        return value;
    }

    Integer increase() {
        return ++value;
    }

    Integer decrease() {
        value = max(0, value - 1);
        return value;
    }

}
