package org.rao.openfeign.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.rao.openfeign.util.BaiduMapResultCheck;

import java.util.List;

/**
 * desc: 请不要调整字段的大小写
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Data
public class BaiduMapRegionResult implements BaiduMapResultCheck {

    /**
     * 本次API访问状态，如果成功返回0，如果失败返回其他数字去上面的地址查看服务状态码，没必要枚举。
     */
    private int status = -1;

    /**
     * 数据版本
     */
    @JsonProperty("data_version")
    private String dataVersion;

    /**
     * 结果值
     */
    @JsonProperty("result_size")
    private Integer resultSize;

    /**
     * 地区信息
     */
    private List<District> districts;

    @Data
    public static class District {

        /**
         * 地区编码
         */
        private String code;

        /**
         * 地区名称
         */
        private String name;

        /**
         * 地区等级 国0省1市2区3街道4
         */
        private Integer level;

        /**
         * 下级地区信息
         */
        private List<District> districts;

    }

    @Override
    public boolean normalState() {
        return this.status == 0;
    }

}
