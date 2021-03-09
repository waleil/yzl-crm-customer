package cn.net.yzl.crm.customer.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.net.yzl.crm.customer.dao.MemberRestMapper;
import cn.net.yzl.crm.customer.dto.MemberQuery;
import cn.net.yzl.crm.customer.service.MemberRestService;

/**
 * 顾客信息
 * 
 * @author zhangweiwei
 * @date 2021年2月5日,下午8:33:22
 */
@Service
public class MemberRestServiceImpl implements MemberRestService {
	@Resource
	private MemberRestMapper memberRestMapper;

	@Override
	public Integer selectMemberCount(MemberQuery memberQuery) {
		return this.memberRestMapper.selectMemberCount(memberQuery);
	}

	@Override
	public List<String> selectMemberCards(String memberName) {
		return this.memberRestMapper.selectMemberCards(memberName);
	}
}
