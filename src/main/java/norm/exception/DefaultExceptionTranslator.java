package norm.exception;

import norm.util.ExceptionUtils;

public class DefaultExceptionTranslator implements ExceptionTranslator {
    @Override
    public RuntimeException translate(String transInfo, Exception e) {
        if (transInfo == null) {
            return ExceptionUtils.wrap(e);
        }
        return ExceptionUtils.wrap(transInfo, e);
    }
}
