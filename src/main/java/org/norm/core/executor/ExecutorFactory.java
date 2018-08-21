package org.norm.core.executor;

import org.norm.Configuration;
import org.norm.Norm;

public interface ExecutorFactory {
    Executor getExecutor(Norm norm);
}
