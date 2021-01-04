package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberGrad;
import cn.net.yzl.crm.customer.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.yzl.crm.customer.model.*;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberMapper memberMapper;

    @Override
    public List<MemberGrad> getMemberGrad() {
        return memberMapper.getMemberGrad();
    }

    @Override
    public int insert(Member record) {
        return memberMapper.insert(record);
    }

    @Override
    public List<Member> findPageByCondition(MemberSerchConditionDTO dto) {
        return memberMapper.findPageByCondition(dto);
    }

    @Override
    public int updateByMemberCardSelective(Member dto) {
        return memberMapper.updateByMemberCardSelective(dto);
    }

    @Override
    public Member selectMemberByCard(String memberCard) {
        return memberMapper.selectMemberByCard(memberCard);
    }
}
