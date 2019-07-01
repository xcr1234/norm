package norm.exception;


public interface ExceptionTranslator {
    RuntimeException translate(String transInfo,Exception e);
}
