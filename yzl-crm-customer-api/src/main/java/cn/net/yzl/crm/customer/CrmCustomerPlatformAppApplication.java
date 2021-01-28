package cn.net.yzl.crm.customer;

import cn.net.yzl.common.swagger2.EnableSwagger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableSwagger
@SpringBootApplication
@EnableDiscoveryClient
//@EnableFeignClients(basePackages = {"cn.net.yzl.crm.customer.service.micservice"})
@EnableTransactionManagement
@MapperScan("cn.net.yzl.crm.customer.dao")
public class CrmCustomerPlatformAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrmCustomerPlatformAppApplication.class, args);
	}

}
