package cn.net.yzl.crm.customer.test;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;

import cn.net.yzl.crm.customer.controller.MemberRestController;
import cn.net.yzl.crm.customer.dto.MemberQuery;

/**
 * 单元测试类
 * 
 * @author zhangweiwei
 * @date 2021年2月5日,下午9:23:40
 */
@SpringBootTest
public class MemberRestControllerTests {
	@Resource
	private MemberRestController controller;

	@Test
	public void testQueryMemberCount() {
		try {
			MemberQuery mq = new MemberQuery();
			mq.setOrderTimeFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
			mq.setOrderTimeTo(LocalDateTime.of(2020, 2, 1, 23, 59, 59));
			System.err.println(JSON.toJSONString(this.controller.queryMemberCount(mq), true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
