package norm.core.parameter;

import java.util.Iterator;

public class ArrayParameters implements Iterable<Parameter> {

    private Object[] args;

    private int nullType;

    public ArrayParameters(Object[] args, int nullType) {
        this.args = args;
        this.nullType = nullType;
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
                Object value = args[cursor];
                Parameter parameter = new ValueParameter("#p" + cursor,value,nullType);
                cursor++;
                return parameter;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }
}
