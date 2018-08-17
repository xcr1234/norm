package org.norm.meta;

public interface MetaFactory {
    Meta getMeta(Class<?> type);
    Meta getMeta(Object object);
}
