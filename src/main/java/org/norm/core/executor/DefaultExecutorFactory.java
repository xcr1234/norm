package org.norm.core.executor;

import org.norm.Configuration;

public class DefaultExecutorFactory implements ExecutorFactory {
    @Override
    public Executor getExecutor(Configuration configuration) {
        return new DefaultExecutor(configuration);
    }
}
