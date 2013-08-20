package elevator.server.security;

public interface UserPasswordValidator {

    Boolean validate(String email, String password);

}
