package org.rao.openfeign.util;

import lombok.extern.slf4j.Slf4j;

/**
 * desc: 结果校验工具
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Slf4j
public class BaiduMapResultUtils {

    /**
     * 成功
     */
    public static void successfulResult(BaiduMapResultCheck baiduMapResultCheck){
        if (! baiduMapResultCheck.normalState() ){
            throw new RuntimeException("百度地图接口请求结果非正常，请手动debug接口结果！");
        }
    }

}
