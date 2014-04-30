package elevator.server.security;

@FunctionalInterface
public interface UserPasswordValidator {
    boolean validate(String email, String password);
}
