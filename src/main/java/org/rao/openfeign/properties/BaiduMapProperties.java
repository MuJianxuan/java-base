package org.rao.openfeign.properties;

import lombok.Getter;
import lombok.Setter;
import org.rao.openfeign.model.enums.Output;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * desc:
 *   使用ak密钥的话是使用白名单的方式来访问。
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@RefreshScope(proxyMode = ScopedProxyMode.NO)
@Setter
@Getter
@ConfigurationProperties(prefix = "application.baidu-map")
public class BaiduMapProperties {


    /**
     * 域名
     */
    private String url;

    /**
     * ak 密钥 ，百度开发平台注册并填写。https://lbsyun.baidu.com/index.php?title=%E9%A6%96%E9%A1%B5
     */
    private String ak;

    /**
     * 输出 xml/json
     */
    private Output output = Output.json;

}
