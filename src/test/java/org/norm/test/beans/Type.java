package org.norm.test.beans;

import norm.convert.IEnum;

public enum Type implements IEnum<String> {
    A,B;

    @Override
    public String getValue() {
        return this.name();
    }
}
