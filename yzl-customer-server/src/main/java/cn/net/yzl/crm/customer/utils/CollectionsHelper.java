package cn.net.yzl.crm.customer.utils;

import java.util.Collections;
import java.util.List;

public class CollectionsHelper {


    /**
     * 为空则设置为空集合
     * @param list
     */
    public final static void nullSetEmptyList(List list){
        if (list == null) {
            list = Collections.emptyList();
        }
    }

    /**
     * 初始化空集合
     */
    public final static <T> List<T> createEmptyList(){
        return Collections.emptyList();
    }



}
