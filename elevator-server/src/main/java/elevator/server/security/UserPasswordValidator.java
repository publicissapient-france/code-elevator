package elevator.server.security;

public interface UserPasswordValidator {
    boolean validate(String email, String password);
}
