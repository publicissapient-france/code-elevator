package elevator;

public class ConstantMaxNumberOfUsers implements MaxNumberOfUsers {

    private static final Integer MAX_NUMBER_OF_USERS = 10;

    private final Integer value;

    public ConstantMaxNumberOfUsers() {
        this.value = MAX_NUMBER_OF_USERS;
    }

    ConstantMaxNumberOfUsers(Integer value) {
        this.value = value;
    }

    @Override
    public Integer value() {
        return value;
    }

}
