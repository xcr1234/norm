package norm;

/**
 * 当实体不合java bean规范时，抛出该异常
 */
public class BeanException extends RuntimeException{
    private static final long serialVersionUID = 1658834910384121614L;

    public BeanException() {
    }

    public BeanException(String message) {
        super(message);
    }

    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
