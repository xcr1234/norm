package norm.core.executor;

import norm.Norm;

public class DefaultExecutorFactory implements ExecutorFactory {
    @Override
    public Executor getExecutor(Norm norm) {
        return new DefaultExecutor(norm);
    }
}
