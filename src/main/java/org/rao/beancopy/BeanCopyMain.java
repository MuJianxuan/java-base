package org.rao.beancopy;

import lombok.extern.slf4j.Slf4j;
import org.rao.beancopy.number2.Number;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/25
 **/
@Slf4j
public class BeanCopyMain {
    public static void main(String[] args) {

        PurchaseInStockOrderVo purchaseInStockOrderVo = new PurchaseInStockOrderVo().setFBillTypeId(new Number("1111")).setFDate("2020").setFInStockEntryList(Arrays.asList(new PurchaseInStockOrderVo.FInStockEntry().setFLot(new Number("1111")), new PurchaseInStockOrderVo.FInStockEntry().setFLot(new Number("222"))) );
        PurchaseInStockOrder purchaseInStockOrder = new PurchaseInStockOrder();

        BeanUtils.copyProperties( purchaseInStockOrderVo,purchaseInStockOrder);

        log.info("[json]:{}",purchaseInStockOrderVo);

        // Bean 拷贝


    }
}
