package cn.net.yzl.crm.customer.utils;


import java.math.BigDecimal;

/**
 * 分转元
 * wangzhe
 * 2021-03-12
 */
public class CentYuanConvertUtil {

    private final static BigDecimal BD_100 = new BigDecimal("100");


    /**
     * 分转换成元
     * @param cent
     * @return
     */
    public static BigDecimal cent2Yuan(Integer cent){
        if (cent == null) {
            return null;
        } else if (cent == 0) {
            return BigDecimal.ZERO;
        }
        //保留两位小数，其余舍弃
        return new BigDecimal(cent).divide(BD_100, 2, BigDecimal.ROUND_DOWN);
    }
}
