package cn.net.yzl.crm.customer.mongomodel;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("圈选媒体信息")
@Data
public class crowd_media {

    private int media_id;

    private String media_name;

    private int media_type_code;

    private String media_type_name;
}
