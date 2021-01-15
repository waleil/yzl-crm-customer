package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.crm.customer.dao.LabelMapper;
import cn.net.yzl.crm.customer.dao.mongo.CrowdGroupRepository;
import cn.net.yzl.crm.customer.dto.label.LabelDto;
import cn.net.yzl.crm.customer.model.CrowdGroup;
import cn.net.yzl.crm.customer.model.Label;
import cn.net.yzl.crm.customer.model.LabelType;
import cn.net.yzl.crm.customer.mongomodel.member_crowd_group;
import cn.net.yzl.crm.customer.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    CrowdGroupRepository crowdGroupRepository;

    @Override
    public int saveLavbel(LabelDto labelDto) {
        return 0;
    }

    @Override
    public List<LabelDto> getCustomerLabels() {
        List<Label> customerLabels = labelMapper.getCustomerLabels();

        return null;
    }

    public void saveCrowd(CrowdGroup crowdGroup){
        crowdGroupRepository.save(new member_crowd_group());

    }

    @Override
    public List<LabelType> getLabelTypes() {
        return labelMapper.selectAllLabelTypes();
    }
}
