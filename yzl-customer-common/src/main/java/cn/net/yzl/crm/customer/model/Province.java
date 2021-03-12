package cn.net.yzl.crm.customer.model;

import lombok.Data;

/**
 * @author : zhangruisong
 * @date : 2020/12/8 19:01
 * @description:
 */
@Deprecated
@Data
public class Province {
    private int id;
    private String name;
    private int code;

    private int country_id;
    private String zname;

    private String aname;

    private String pname;

    private String abbr;
}
