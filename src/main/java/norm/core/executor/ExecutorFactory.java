package norm.core.executor;

import norm.Norm;

public interface ExecutorFactory {
    Executor getExecutor(Norm norm);
}
