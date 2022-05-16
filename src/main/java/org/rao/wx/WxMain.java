package org.rao.wx;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * desc: 公众号
 *
 * @author Rao
 * @Date 2022/04/27
 **/
@Slf4j
public class WxMain {
    public static void main(String[] args) throws UnsupportedEncodingException {

        String encode = URLEncoder.encode("ssss", "UTF-8");
        log.info(encode);

    }
}
