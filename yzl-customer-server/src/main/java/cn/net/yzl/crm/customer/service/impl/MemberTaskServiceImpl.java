package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.crm.customer.dao.MemberProductEffectMapper;
import cn.net.yzl.crm.customer.model.db.MemberProductEffect;
import cn.net.yzl.crm.customer.service.MemberTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberTaskServiceImpl
 * @description todo
 * @date: 2021/2/2 3:50 下午
 */
@Service
@Slf4j
public class MemberTaskServiceImpl implements MemberTaskService {
    @Autowired
    private MemberProductEffectMapper productEffectMapper;
    @Override
    public boolean checkMemberProductEffect() {
        //TODO 获取所有顾客购买商品剩余商品余量大于0
        List<MemberProductEffect> list =productEffectMapper.checkMemberProductEffect();

        return false;
    }
}
