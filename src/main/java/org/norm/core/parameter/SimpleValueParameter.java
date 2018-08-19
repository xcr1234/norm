package org.norm.core.parameter;

public class SimpleValueParameter extends ValueParameter {
    public SimpleValueParameter(Object value) {
        super("$value", value);
    }
}
