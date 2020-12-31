package cn.net.yzl.crm.customer.service;


import cn.net.yzl.crm.customer.model.Province;
import org.springframework.stereotype.Component;

/**
 * @author : zhangruisong
 * @date : 2020/12/1 14:54
 * @description:
 */
@Component
public interface CityService {
    void saveProvince(Province province);

    Province getProvince();
}
