package org.rao.random;

/**
 * 不重要的 异常信息
 * @author Rao
 * @Date 2021/11/10
 **/
public abstract class UnimportantException extends RuntimeException {
    private static final long serialVersionUID = 2192668711202599895L;

    public UnimportantException() {
        super();
    }

    public UnimportantException(String message) {
        super(message);
    }

    public UnimportantException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnimportantException(Throwable cause) {
        super(cause);
    }

    protected UnimportantException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * avoid the expensive and useless stack trace for api exceptions
     * 不在调用父类栈信息
     *    ( 我认为主动抛出的异常信息 根本不需要栈信息 因为可以通过 自定义的message 搜索到！)
     * @return
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
