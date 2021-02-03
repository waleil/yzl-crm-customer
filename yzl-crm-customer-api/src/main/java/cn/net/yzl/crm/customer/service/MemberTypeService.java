package cn.net.yzl.crm.customer.service;

import cn.net.yzl.crm.customer.vo.MemberTypeVO;

import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberTypeService
 * @description 顾客类型
 * @date: 2021/2/3 5:12 下午
 */
public interface MemberTypeService {
    List<MemberTypeVO> queryMemberType();
}
