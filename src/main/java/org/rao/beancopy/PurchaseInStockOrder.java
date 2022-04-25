package org.rao.beancopy;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.rao.beancopy.number1.Number;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 实际调用 远程需要使用 gson 的注解 @SerializedName(...)
 *
 * @author Rao
 * @Date 2022/04/25
 **/
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class PurchaseInStockOrder implements Serializable {
    private static final long serialVersionUID = -6801100197743133973L;

    /**
     * fid
     */
    @SerializedName("FID")
    private String fid;

    /**
     * 单据编号
     */
    @SerializedName("FBillTypeID")
    private Number fBillTypeId;

    /**
     *  入库日期
     */
    @SerializedName("FDate")
    private String fDate;

    /**
     * 收料组织
     */
    @SerializedName("FStockOrgId")
    private Number fStockOrgId;

    /**
     *  采购组织
     */
    @SerializedName("FPurchaseOrgId")
    private Number fPurchaseOrgId;

    /**
     *  物料分类
     */
    @SerializedName("F_MS_WLFL")
    private Number fMsWlfl;

    /**
     * 物料明细列表
     */
    @SerializedName("FInStockEntry")
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
        @SerializedName("FEntryID")
        private String fEntryId;

        /**
         * 物料编码
         */
        @SerializedName("FMaterialId")
        private Number fMaterialId;

        /**
         * 库存单位
         */
        @SerializedName("FUnitID")
        private Number fUnitId;

        /**
         *  计价单位
         */
        @SerializedName("FPriceUnitID")
        private Number fPriceUnitId;

        /**
         *  采购单位
         */
        @SerializedName("FRemainInStockUnitId")
        private Number fRemainInStockUnitId;

        /**
         * 货主类型
         */
        @SerializedName("FOWNERTYPEID")
        private String fownertypeid;

        /**
         *  货主
         */
        @SerializedName("FOWNERID")
        private Number fownerid;

        /**
         * 供应商订单号
         */
        @SerializedName("F_QH_SUPPLYNO")
        private String fQhSupplyno;

        /**
         *  实收数量
         */
        @SerializedName("FRealQty")
        private String fRealQty;

        /**
         *  批号
         */
        @SerializedName("FLot")
        private Number fLot;

    }



}
