package org.rao.bigdecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Rao
 * @Date 2021/11/26
 **/
public class BigDecimalMain {

    public static void main(String[] args) {

        BigDecimal bigDecimal = new BigDecimal("2.33");
        BigDecimal bigDecimal1 = bigDecimal.setScale(1, RoundingMode.DOWN);
        System.out.println(bigDecimal1);

        System.out.println( bigDecimal.toString() );

    }
}
