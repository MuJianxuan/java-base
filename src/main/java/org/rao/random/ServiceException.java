package org.rao.random;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常
 * @author Rao
 * @Date 2021/11/10
 **/
@Setter
@Getter
public class ServiceException extends UnimportantException {
    private static final long serialVersionUID = 6924713140817601466L;

    /**
     * bind ,but Null does not handle
     * {@link com.meiqijiacheng.sango.core.common.response.ApiResult#status}
     */
    private Integer status;
    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(Integer status,String message){
        super(message);
        this.status = status;
        this.message = message;
    }

    public ServiceException(Integer status,String message,Throwable cause){
        super(message);
        this.status = status;
        this.message = message;
    }

    // 其他的话 用到再说吧

}
