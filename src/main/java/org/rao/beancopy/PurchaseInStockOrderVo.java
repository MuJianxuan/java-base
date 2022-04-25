package org.rao.beancopy;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.rao.beancopy.number2.Number;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/25
 **/
@Accessors(chain = true)
@NoArgsConstructor
@Data
public class PurchaseInStockOrderVo implements Serializable {
    private static final long serialVersionUID = -374674862054221184L;
    
    /**
     * fid
     */
    @JSONField(name = "FID")
    private String fid;

    /**
     * 单据编号
     */
    @JSONField(name = "FBillTypeID")
    private Number fBillTypeId;

    /**
     *  入库日期
     */
    @NotNull(message = "入库日期参数缺失！")
    @JSONField(name = "FDate")
    private String fDate;

    /**
     * 收料组织
     */
    @JSONField(name = "FStockOrgId")
    private Number fStockOrgId;

    /**
     *  采购组织
     */
    @JSONField(name = "FPurchaseOrgId")
    private Number fPurchaseOrgId;

    /**
     *  物料分类
     */
    @JSONField(name = "F_MS_WLFL")
    private Number fMsWlfl;

    /**
     * 物料明细列表
     */
    @JSONField(name = "FInStockEntry")
    private List<FInStockEntry> fInStockEntryList;


    /**
     * FInStockEntry
     */
    @Accessors(chain = true)
    @NoArgsConstructor
    @Data
    public static class FInStockEntry {
        /**
         * fEntryID
         */
        @JSONField(name = "FEntryID")
        private String fEntryId;

        /**
         * 物料编码
         */
        @JSONField(name = "FMaterialId")
        private Number fMaterialId;

        /**
         * 库存单位
         */
        @JSONField(name = "FUnitID")
        private Number fUnitId;

        /**
         *  计价单位
         */
        @JSONField(name = "FPriceUnitID")
        private Number fPriceUnitId;

        /**
         *  采购单位
         */
        @JSONField(name = "FRemainInStockUnitId")
        private Number fRemainInStockUnitId;

        /**
         * 货主类型
         */
        @NotNull(message = "货主类型参数缺失！")
        @JSONField(name = "FOWNERTYPEID")
        private String fownertypeid;

        /**
         *  货主
         */
        @JSONField(name = "FOWNERID")
        private Number fownerid;

        /**
         * 供应商订单号
         */
        @NotNull(message = "供应商订单号参数缺失！")
        @JSONField(name = "F_QH_SUPPLYNO")
        private String fQhSupplyno;

        /**
         *  实收数量
         */
        @NotNull(message = "实收数量参数缺失！")
        @JSONField(name = "FRealQty")
        private String fRealQty;

        /**
         *  批号
         */
        @JSONField(name = "FLot")
        private Number fLot;

    }



}
