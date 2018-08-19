package org.norm.core.executor;

import org.norm.Configuration;

public interface ExecutorFactory {
    Executor getExecutor(Configuration configuration);
}
