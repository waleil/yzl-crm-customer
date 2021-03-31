package cn.net.yzl.crm.customer.test;

import org.junit.Test;

public class MathTest {


    /**
     * Math.ceil(2.1)=3
     * Math.ceil(-2.1)=-2
     */
    @Test
    public void ceil(){
        int ceil = (int)Math.ceil(-99 / (float)2);
        System.out.println(ceil);

        ceil = (int)Math.ceil(2.1);
        System.out.println(ceil);

        ceil = (int)Math.ceil(-2.1);
        System.out.println(ceil);

    }


}
