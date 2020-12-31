package cn.net.yzl.crm.customer.service.impl;


import cn.net.yzl.crm.customer.model.Province;
import cn.net.yzl.crm.customer.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : zhangruisong
 * @date : 2020/12/1 14:57
 * @description:
 */
@Service
public class CityServiceImpl implements CityService {

//    @Autowired
//    CityMapper cityMapper;
//
//    @Autowired
//    ProvinceMapper provinceMapper;

    @Override
    public void saveProvince(Province province) {
//         cityMapper.saveProvince(province);
    }

    @Override
    public Province getProvince() {
//        return provinceMapper.getProvince();
        return null;
    }
}
