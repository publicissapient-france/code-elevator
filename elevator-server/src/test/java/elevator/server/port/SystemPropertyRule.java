package elevator.server.port;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class SystemPropertyRule implements TestRule {

    private final String propertyName;

    public SystemPropertyRule(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new SaveAndRestoreSystemProperty(propertyName, base);
    }

    private static class SaveAndRestoreSystemProperty extends Statement {

        private final String propertyName;
        private final Statement base;

        public SaveAndRestoreSystemProperty(String propertyName, Statement base) {
            this.propertyName = propertyName;
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            String previousPropertyValue = System.getProperty(propertyName);
            try {
                base.evaluate();
            } finally {
                if (previousPropertyValue == null) {
                    System.clearProperty(propertyName);
                } else {
                    System.setProperty(propertyName, previousPropertyValue);
                }
            }
        }

    }
}
