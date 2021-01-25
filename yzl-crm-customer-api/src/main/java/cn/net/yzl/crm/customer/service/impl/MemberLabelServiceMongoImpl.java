package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.crm.customer.dao.MemberActionRelationMapper;
import cn.net.yzl.crm.customer.dao.MemberDiseaseMapper;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.MemberOrderStatMapper;
import cn.net.yzl.crm.customer.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.model.db.MemberOrderStat;
import cn.net.yzl.crm.customer.model.mogo.ActionDict;
import cn.net.yzl.crm.customer.model.mogo.MemberDisease;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.service.MemberLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabelServiceMongoImpl
 * @description 同步数据至MongoDB
 * @date: 2021/1/25 7:53 下午
 */
@Slf4j
@Service
public class MemberLabelServiceMongoImpl implements MemberLabelService {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberDiseaseMapper memberDiseaseMapper;
    @Autowired
    private MemberActionRelationMapper memberActionRelationMapper;
    @Autowired
    private MemberOrderStatMapper memberOrderStatMapper;
    private MemberLabelDao memberLabelDao;
    @Override
    public Boolean syncProcess(int pageNo, int pageSize) {
        List<MemberLabel> list = memberMapper.queryAllMemberByPage(pageNo, pageSize);
        if (!CollectionUtils.isEmpty(list)) {
            log.info("同步顾客数据至MongoDB,当前页数:{},分页大小:{},数据大小:{}", pageNo, pageSize, list.size());
            //处理顾客编号
            List<String> memberCodes = list.stream().map(MemberLabel::getMember_card)
                    .collect(Collectors.toList());
            //查询相关病症信息
            List<MemberDisease> memberDiseaseList = memberDiseaseMapper.queryByMemberCodes(memberCodes);
            // 查询订单相关信息
            List<MemberOrderStat> memberOrderStatList = memberOrderStatMapper.queryByMemberCodes(memberCodes);
            //查询综合行为
            List<ActionDict> actionDictList = memberActionRelationMapper.queryByMemberCodes(memberCodes);
            //批量保存至MongoDB
            memberLabelDao.bathSave(list);
            return true;
        } else {
            return false;
        }
    }
}
