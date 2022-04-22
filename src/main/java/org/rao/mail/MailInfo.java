package org.rao.mail;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/21
 **/
@Builder
@Data
public class MailInfo implements Serializable{

    private String host;
    private String encoding;
    private String userName;
    /**
     * 授权码
     */
    private String password;

}
