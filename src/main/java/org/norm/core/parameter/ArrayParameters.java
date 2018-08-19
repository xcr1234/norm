package org.norm.core.parameter;

import java.util.Iterator;

public class ArrayParameters implements Iterable<Parameter> {

    private Object[] args;

    public ArrayParameters(Object[] args) {
        this.args = args;
    }


    @Override
    public Iterator<Parameter> iterator() {
        return new Iterator<Parameter>() {

            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return args != null && cursor < args.length;
            }

            @Override
            public Parameter next() {
                Object value = args[cursor++ ];
                return new SimpleValueParameter(value);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }
}
