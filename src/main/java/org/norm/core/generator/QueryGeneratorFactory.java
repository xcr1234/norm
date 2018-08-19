package org.norm.core.generator;

public interface QueryGeneratorFactory {
    QueryGenerator getGenerator(Class<?> type);
}
