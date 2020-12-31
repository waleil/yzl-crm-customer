package cn.net.yzl.crm.customer.service.impl;

import cn.net.yzl.crm.customer.dao.LabelMapper;
import cn.net.yzl.crm.customer.dto.label.LabelDto;
import cn.net.yzl.crm.customer.model.Label;
import cn.net.yzl.crm.customer.model.LabelType;
import cn.net.yzl.crm.customer.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelMapper labelMapper;
    @Override
    public int saveLavbel(LabelDto labelDto) {
        return 0;
    }

    @Override
    public List<LabelDto> getCustomerLabels() {
        List<Label> customerLabels = labelMapper.getCustomerLabels();

        return null;
    }

    @Override
    public List<LabelType> getLabelTypes() {
        return labelMapper.selectAllLabelTypes();
    }
}
