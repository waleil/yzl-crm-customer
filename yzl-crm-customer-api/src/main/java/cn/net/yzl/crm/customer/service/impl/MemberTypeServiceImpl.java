package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.crm.customer.dao.SysDictDataDao;
import cn.net.yzl.crm.customer.service.MemberTypeService;
import cn.net.yzl.crm.customer.vo.MemberTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberTypeServiceImpl
 * @description todo
 * @date: 2021/2/3 5:13 下午
 */
@Service
public class MemberTypeServiceImpl implements MemberTypeService {
    @Autowired
    private SysDictDataDao sysDictDataDao;
    @Override
    public List<MemberTypeVO> queryMemberType() {
        return sysDictDataDao.queryMemberType();
    }
}
