package cn.net.yzl.crm.customer.service;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.dto.label.LabelDto;
import cn.net.yzl.crm.customer.model.LabelType;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


/**
 * 标签业务处理层
 */
public interface LabelService {

   // List<LabelGroup>

    /**
     * 保存单个标签  返回主键
     */
     int saveLavbel(LabelDto labelDto);

    /**
     * 保获取所有客户标签
     */
    List<LabelDto> getCustomerLabels();


    /**
     * 获取标签类型接口
     */
    List<LabelType> getLabelTypes();
}
