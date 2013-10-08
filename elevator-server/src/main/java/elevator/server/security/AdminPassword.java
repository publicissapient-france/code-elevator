package elevator.server.security;

class AdminPassword implements Password {

    @Override
    public String value() {
        return "admin";
    }

}
