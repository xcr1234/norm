package org.norm.core;

public interface QueryGeneratorFactory {
    QueryGenerator getGenerator(Class<?> type);
}
