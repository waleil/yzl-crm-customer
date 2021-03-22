package cn.net.yzl.crm.customer.feign.api;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.feign.client.order.OrderFien;
import cn.net.yzl.crm.customer.feign.client.product.ProductFien;
import cn.net.yzl.crm.customer.model.MemberOrderObject;
import cn.net.yzl.order.model.vo.member.MemberTotal;
import cn.net.yzl.order.model.vo.order.OrderTotal4MemberDTO;
import cn.net.yzl.product.model.vo.product.dto.ProductMainDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 商品服务相关接口
 * wangzhe
 * 2021-03-17
 */
@Component
@Slf4j
public class ProductClientAPI {

    @Autowired
    ProductFien productFien;

    public static ProductClientAPI productClientAPI;

    public void setProductFien(ProductFien productFien) {
        this.productFien = productFien;
    }

    @PostConstruct
    public void init() {
        productClientAPI = this;
        productClientAPI.productFien = this.productFien;
    }

    /**
     * 根据顾客卡号查询退单/签收信息
     * @param codes 商品编号数组
     * @return
     */
    public static List<ProductMainDTO> queryByProductCodes(@RequestParam("codes") String[] codes){
        List<ProductMainDTO> data = null;
        try {
            ComResponse<List<ProductMainDTO> > response = productClientAPI.productFien.queryByProductCodes(codes);
            if (response.getCode() == 503) {
                log.error("商品服务不可用:queryByProductCodes");
            }
            else if (response.getCode() != null && response.getCode() == 200) {
                data = response.getData();
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        if (CollectionUtil.isEmpty(data)) {
            data = Collections.emptyList();
        }
        return data;
    }


}
