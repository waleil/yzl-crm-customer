package cn.net.yzl.crm.customer;

import cn.net.yzl.common.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author lichanghong
 * @version 1.0
 * @title: BaseObject
 * @description todo
 * @date: 2021/1/22 11:45 上午
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseObject {
    /**
     * A JSON representation of the object.
     */
    @Override
    public String toString() {
        return JsonUtil.toJsonStr(this);
    }
}
