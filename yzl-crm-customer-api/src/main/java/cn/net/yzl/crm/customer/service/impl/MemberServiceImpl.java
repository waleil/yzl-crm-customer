package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.util.AssemblerResultUtil;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dto.CrowdGroupDTO;
import cn.net.yzl.crm.customer.dto.member.MemberSerchConditionDTO;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberGrad;
import cn.net.yzl.crm.customer.service.MemberService;
import cn.net.yzl.crm.customer.viewmodel.MemberOrderStatViewModel;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.yzl.crm.customer.model.*;

import java.util.ArrayList;
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
    public Page<Member> findPageByCondition(MemberSerchConditionDTO dto) {
        if (dto.getCurrentPage() == null || dto.getCurrentPage() == 0) {
            dto.setCurrentPage(1);
        }
        if (dto.getPageSize() == null || dto.getPageSize() == 0) {
            dto.setPageSize(10);
        }
        PageHelper.startPage(dto.getCurrentPage(), dto.getPageSize());
        List<Member> list = memberMapper.findPageByCondition(dto);
        Page<Member> page = AssemblerResultUtil.resultAssembler(list);

        return page;
    }

    @Override
    public int updateByMemberCardSelective(Member dto) {
        return memberMapper.updateByMemberCardSelective(dto);
    }

    @Override
    public Member selectMemberByCard(String memberCard) {
        return memberMapper.selectMemberByCard(memberCard);
    }

    /**
     * 获取顾客联系方式信息，包括手机号，座机号
     * @param member_card
     * @return
     */
    @Override
    public List<MemberPhone> getMemberPhoneList(String member_card) {
        return memberMapper.getMemberPhoneList(member_card);
    }

    /**
     * 根据手机号获取顾客信息（可用来判断手机号是否被注册，如果被注册则返回注册顾客实体）
     * @param phone
     * @return
     */
    @Override
    public Member getMemberByPhone(String phone) {
        List<String> phoneList=new ArrayList<>();
        phoneList.add(phone);
        phoneList.add("0"+phone);
        phoneList.add("00"+phone);
        return memberMapper.getMemberByPhone(phoneList); //因为手机号或许有前缀0，所以要加0的判断
    }

    /**
     * 设置顾客为会员
     * @param member_card
     */
    @Override
    public void setMemberToVip(String member_card) {
        memberMapper.setMemberToVip(member_card);
    }

    @Override
    public List<MemberProductEffect> getMemberProductEffectList(String member_card) {
        return memberMapper.getMemberProductEffectList(member_card);
    }

    /**
     * 获取咨询商品
     * @param member_card
     * @return
     */
    @Override
    public List<ProductConsultation> getProductConsultationList(String member_card) {
        return memberMapper.getProductConsultationList(member_card);
    }

    @Override
    public List<MemberDisease> getMemberDisease(String member_card) {
        return memberMapper.getMemberDisease(member_card);
    }

    @Override
    public void saveReveiverAddress(ReveiverAddress reveiverAddress) {
        memberMapper.saveReveiverAddress(reveiverAddress);
    }

    @Override
    public void updateReveiverAddress(ReveiverAddress reveiverAddress) {
        memberMapper.updateReveiverAddress(reveiverAddress);
    }

    @Override
    public List<ReveiverAddress> getReveiverAddress(String member_card) {
        return memberMapper.getReveiverAddress(member_card);
    }

    @Override
    public MemberOrderStat getMemberOrderStat(String member_card) {
        return memberMapper.getMemberOrderStat(member_card);
    }

    /**
     * 保存顾客购买能力
     * @param memberOrderStat
     */
    @Override
    public void addMemberOrderStat(MemberOrderStat memberOrderStat) {
        memberMapper.addMemberOrderStat(memberOrderStat);
    }

    /**
     * 修改顾客购买能力
     * @param memberOrderStat
     */
    @Override
    public void updateMemberOrderStat(MemberOrderStat memberOrderStat) {
        memberMapper.updateMemberOrderStat(memberOrderStat);
    }

    /**
     * 获取顾客行为偏好
     * @param member_card
     * @return
     */
    @Override
    public MemberAction getMemberAction(String member_card) {
        return memberMapper.getMemberAction(member_card);
    }

    @Override
    public void saveMemberAction(MemberAction memberAction) {
        memberMapper.saveMemberAction(memberAction);
    }

    @Override
    public void updateMemberAction(MemberAction memberAction) {
        memberMapper.updateMemberAction(memberAction);
    }

    @Override
    public List<MemberOrderStatViewModel> getMemberList(List<String> member_cards) {
        return memberMapper.getMemberList(member_cards);
    }

    /**
     * 添加顾客圈选
     * @param crowdGroup
     * @return
     */
    @Override
    public int addCrowdGroup(CrowdGroup crowdGroup) {
        return memberMapper.addCrowdGroup(crowdGroup);
    }

    @Override
    public List<CrowdGroup> getCrowdGroupByIds(List<Integer> groupIds) {
        return memberMapper.getCrowdGroupByIds(groupIds);
    }

    @Override
    public Page<CrowdGroup> getCrowdGroupByPage(CrowdGroupDTO crowdGroupDTO) {
        if (crowdGroupDTO.getCurrentPage() == null || crowdGroupDTO.getCurrentPage() == 0) {
            crowdGroupDTO.setCurrentPage(1);
        }
        if (crowdGroupDTO.getPageSize() == null || crowdGroupDTO.getPageSize() == 0) {
            crowdGroupDTO.setPageSize(10);
        }
        PageHelper.startPage(crowdGroupDTO.getCurrentPage(), crowdGroupDTO.getPageSize());
        List<CrowdGroup> list = memberMapper.getCrowdGroupByPage(crowdGroupDTO);
        Page<CrowdGroup> page = AssemblerResultUtil.resultAssembler(list);

        return page;
    }


}
