package cn.net.yzl.crm.customer.utils;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 对一些字符串的操作类
 * wzQQ
 * 2019-6-26
 */
public class StringHelper {

    /**
     * 判断一个string类型的元素是否在目标元素中
     * wzQQ
     * 2019-06-13
     * @param element 元素
     * @param elements 目标元素
     * @return true or false
     */
    public static boolean in (String element,String... elements){
        for (String e : elements) {
            if (element.equals(e)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断一个string类型的元素是否在目标元素中
     * wzQQ
     * 2019-06-13
     * @param element 元素
     * @param elements 目标元素
     * @return true or false
     */
    public static boolean notin (String element,String... elements){
        for (String e : elements) {
            if (element.equals(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断一个string类型的元素是否在包含目标元素
     * wzQQ
     * 2019-06-13
     * @param element 元素
     * @param elements 目标元素
     * @return true or false
     */
    public static boolean contains (String element,String... elements){
        if (StringUtils.isNotEmpty(element)){
            for (String e : elements) {
                if (element.indexOf(e) > -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断一个string类型的元素是否不包含目标元素
     * wzQQ
     * 2019-06-13
     * @param element 元素
     * @param elements 目标元素
     * @return true or false
     */
    public static boolean notContains (String element,String... elements){
        if (StringUtils.isNotEmpty(element)){
            for (String e : elements) {
                if (element.indexOf(e) > -1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断一个string类型的元素按指定符号分割后，是否在包含目标元素
     * wzQQ
     * 2019-09-17
     * @param element 元素
     * @param elements 目标元素
     * @return true or false
     */
    public static boolean splitContains (String element,String split,String... elements){
        if (StringUtils.isNotEmpty(element) && elements != null) {
            List<String> list = Arrays.asList(element.split(split,-1));
            for (String e : elements) {
                if (list.contains(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断一个string类型的元素是否在目标元素中（忽略大小写）
     * wzQQ
     * 2019-10-14
     * @param element 元素
     * @param elements 目标元素
     * @return true or false
     */
    public static boolean notinIgnoreCase (String element,String... elements){
        for (String e : elements) {
            if (element.equalsIgnoreCase(e)) {
                return false;
            }
        }
        return true;
    }
    /**
     * 判断一个string类型的元素是否含有目标元素
     * wzQQ
     * 2019-08-15
     * @param element 元素
     * @param elements 目标元素集
     * @return true or false
     */
    public static boolean likes (String element,String... elements){
        for (String e : elements) {
            if (element.contains(e)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断一个string类型的元素是否不含有目标元素
     * wzQQ
     * 2019-08-15
     * @param element 元素
     * @param elements 目标元素集
     * @return true or false
     */
    public static boolean notLikes (String element,String... elements){
        for (String e : elements) {
            if (element.contains(e)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 集合转成单引号拼拼接的字符串
     * wzQQ
     * 2019-07-25
     * @param strList
     * @return 处理后的字符串
     */
    public static String convertListToString(List<String> strList) {
        StringBuffer sb = new StringBuffer();
        if (CollectionUtil.isNotEmpty(strList)){
            sb.append("'").append(strList.get(0)).append("'");
            for (int i=1;i<strList.size();i++) {
                sb.append(",").append("'").append(strList.get(i)).append("'");
            }
        }
        return sb.toString();
    }

    /**
     * 兼容sql语句红的in('') 操作，封装的参数处理
     * wzQQ
     * 2019-07-31
     * @param list sql语句in包含的
     * @return 处理好的 实参集合和sql字符串
     */
    /*public static <T> Pair<List<Object>,String> getParamAndSqlStrForIn22 (List<T> list){
        StringBuffer sql=new StringBuffer("?");
        List<Object> param = new ArrayList<Object>();
        if (CollectionUtil.isEmpty(list)){
            param.add("-1");//默认的实参，如果使用时，巧合会影响实际结果，请修改
        }else{
            param.add(list.get(0));
            for (int i=1;i<list.size();i++){
                sql.append(",?");
                param.add(list.get(i));
            }
        }
        return Pair.with(param,sql.toString());

    }*/

    /**
     * 兼容sql语句红的in('') 操作，封装的参数处理
     * wzQQ
     * 2019-07-31
     * @param list sql语句in包含的
     * @return 处理好的 sql字符串
     */
    public static String getSqlStrForIn (List<Object> list){
        StringBuffer sql=new StringBuffer("?");
        if(list == null){
//            list = new ArrayList<Object>();
            sql=new StringBuffer("'-1'");
        }else if (list.isEmpty()){
            list.add("-1");//默认的实参，如果使用时，巧合会影响实际结果，请修改
        }else{
            for (int i=1;i<list.size();i++){
                sql.append(",?");
            }
        }
        return sql.toString();
    }

    public static void main(String[] args) {
        /*List<Object> list = new ArrayList<Object>();
        getSqlStrForIn(list);
        System.out.println(list);*/
        System.out.println(subString("a'b,c|d'", "'"));
    }

    /**
     * @param str
     * @param sep
     * @return
     */
    public static String subString (String str,String sep){
        if (StringUtils.isNotBlank(str) && str.endsWith(sep)) {
            return str.substring(0, str.lastIndexOf(sep));
        }
        return str;
    }

}
