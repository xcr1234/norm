package org.norm.core.executor;

import org.norm.Configuration;
import org.norm.Norm;

public class DefaultExecutorFactory implements ExecutorFactory {
    @Override
    public Executor getExecutor(Norm norm) {
        return new DefaultExecutor(norm);
    }
}
