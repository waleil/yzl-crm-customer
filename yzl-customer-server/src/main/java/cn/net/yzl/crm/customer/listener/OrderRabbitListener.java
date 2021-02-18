package cn.net.yzl.crm.customer.listener;

import cn.net.yzl.crm.customer.service.MemberService;
import cn.net.yzl.crm.customer.vo.order.OrderSignInfo4MqVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderRabbitListener implements ChannelAwareMessageListener {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MemberService memberService;


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
		try {
			//消息换成对象
			System.out.println(message.getBody());
			OrderSignInfo4MqVO order = this.objectMapper.readValue(message.getBody(), OrderSignInfo4MqVO.class);
			memberService.orderSignUpdateMemberData(order);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);// 正常消费后，手动确认消息
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);// 消费失败后，手动拒绝消息
		}
	}


}
