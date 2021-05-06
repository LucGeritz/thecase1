package ninja.pinhole.console2;

@FunctionalInterface
public interface InputValidator {
    boolean validate(String value);
}
