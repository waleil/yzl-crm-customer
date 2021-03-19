package cn.net.yzl.crm.customer.listener;

import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.crm.customer.model.db.MemberOrderSignHandle;
import cn.net.yzl.crm.customer.service.MemberOrderSignHandleService;
import cn.net.yzl.crm.customer.service.MemberService;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class OrderRabbitListener implements ChannelAwareMessageListener {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberOrderSignHandleService memberOrderSignHandleService;


	/**
	 * 接收订单签收消息
	 * @param message
	 * @param channel
	 * @throws Exception
	 */
	@Override
	@RabbitListener(queues = "qu.order.signinfo.customer", // 队列名
			concurrency = "1"// 消费者数量
	)
	public void onMessage(Message message, Channel channel) throws Exception {
		ComResponse<Boolean> response = null;
		OrderSignInfo4MqVO order = null;//订单签收消息对象
		boolean successFlag = false;//是否操作成功标识
		MemberOrderSignHandle error = new MemberOrderSignHandle();
		String exMsg = "";
		try {
			error.setCreatorNo("SYSTEM");
			error.setUpdatorNo("SYSTEM");
			error.setCreateTime(new Date());
			if (message.getBody() != null) {
				//error.setOrderData(StringEscapeUtils.unescapeJavaScript(new String(message.getBody())));
				error.setOrderData(JSON.toJSONString(new String(message.getBody())));
			}

			//消息换成对象
			order = this.objectMapper.readValue(message.getBody(), OrderSignInfo4MqVO.class);
			error.setMemberCard(order.getMemberCardNo());
			error.setOrderNo(order.getOrderNo());
			error.setOrderData(JSON.toJSONString(order));

			response = memberService.orderSignUpdateMemberData(order);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);// 正常消费后，手动确认消息
			if (response.getCode() == 200) {
				successFlag = true;
			}
		} catch (Exception e) {
			exMsg = e.getMessage();
			log.error("onMessage:订单签收时处理消息失败!" + exMsg);
			channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);// 消费失败后，手动拒绝消息
		}finally {
			//处理成功的消息
			if (successFlag) {
				error.setStatus(1);//成功
			}
			//处理消息失败
			else{
				error.setStatus(2);//失败
				if (response == null) {
					error.setErrorMsg("消息未处理!" + exMsg);
				}else{
					if (response.getCode() != null) {
						error.setErrorCode(response.getCode().toString());
					}
					error.setErrorMsg(response.getMessage());
				}
			}
			if (error != null) {
				ComResponse<Boolean> saveResult = memberOrderSignHandleService.saveDealErrorOrderData(error);
				if (saveResult.getCode() != 200) {
					log.error("onMessage:订单签收时处理消息失败且保存失败记录失败!"+ JSON.toJSONString(order));
				}
			}
		}
	}


}
