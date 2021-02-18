package cn.net.yzl.crm.customer.model;

import lombok.Data;

@Data
public class Label {
    private Integer id;

    private Integer pid;

    private String name;

    private String fieldName;

    private Integer code;

    private String value1;

    private String value2;

    private Short labelType;

    private Float limitDn;

    private Float limitUp;

    private Integer sort;

    private String url;

    private Boolean checkBox;

    private Integer groupId;

    private String route;


}
