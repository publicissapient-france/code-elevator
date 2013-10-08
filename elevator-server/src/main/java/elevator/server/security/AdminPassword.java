package elevator.server.security;

class AdminPassword implements Password {

    static final String PASSWORD_PROPERTY = "ADMIN_PASSWORD";

    @Override
    public String value() {
        return System.getProperty(PASSWORD_PROPERTY, "admin");
    }

}
