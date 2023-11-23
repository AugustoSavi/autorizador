package org.autorizador.violation;

public class Violation {
    private ViolationDefinition violationDefinition;

    public Violation(ViolationDefinition violationDefinition) {
        this.violationDefinition = violationDefinition;
    }

    public String getViolationDefinition() {
        return violationDefinition.name();
    }

    public static String buildFromEnum(ViolationDefinition violationDefinition){
        return new Violation(violationDefinition).getViolationDefinition();
    }
}
