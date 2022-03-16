package org.rao.random;

/**
 * 重要的异常
 * @author Rao
 * @Date 2021/11/10
 **/
public class ImportantException extends RuntimeException {
    private static final long serialVersionUID = 7306186393421365750L;

    public ImportantException() {
        super();
    }

    public ImportantException(String message) {
        super(message);
    }

    public ImportantException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportantException(Throwable cause) {
        super(cause);
    }

    protected ImportantException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }



}
