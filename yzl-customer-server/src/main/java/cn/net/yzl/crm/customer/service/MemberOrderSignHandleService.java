package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.entity.Page;
import cn.net.yzl.crm.customer.dto.PageDTO;
import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandle;

public interface MemberOrderSignHandleService {


    public ComResponse<Boolean> saveDealErrorOrderData(MemberOrderSignHandle error);

    /**
     * 处理失败的订单签收消息
     * wangzhe
     * 2021-03-18
     * @param primaryKey
     * @return
     */
    Boolean dealFailRecord(Integer primaryKey);

    /**
     * 分页获取处理失败的消息列表
     * wangzhe
     * 2021-03-18
     * @param page
     * @return
     */

    Page<MemberOrderSignHandle> getFailRecordList(PageDTO page);

    /**
     * 根据记录主键更细数据
     * wangzhe
     * 2021-03-19
     * @param signHandle
     * @return
     */
    public Integer updateByPrimaryKeySelective(MemberOrderSignHandle signHandle);
}
