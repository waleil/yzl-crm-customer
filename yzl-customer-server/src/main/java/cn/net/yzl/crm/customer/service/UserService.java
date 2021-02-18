package cn.net.yzl.crm.customer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("user-service")
public interface UserService {
    @RequestMapping(method = RequestMethod.GET, value = "/user/getuser")
    String getUserName();
}

