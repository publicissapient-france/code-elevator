package elevator.server.security;

class AdminPassword implements Password {

    static final String PASSWORD_ROPERTY = "ADMIN_PASSWORD";

    private final String password;

    AdminPassword() {
        this.password = System.getProperty(PASSWORD_ROPERTY, "admin");
    }

    @Override
    public String value() {
        return password;
    }

}
