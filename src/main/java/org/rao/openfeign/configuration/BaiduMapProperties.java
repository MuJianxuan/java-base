package org.rao.openfeign.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * desc:
 *   使用ak密钥的话是使用白名单的方式来访问。
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@Setter
@Getter
//@Configuration
@ConfigurationProperties(prefix = "application.baidu-map")
public class BaiduMapProperties {

    /**
     * 域名
     */
    private String url;

    /**
     * ak 密钥
     */
    private String ak;

}
