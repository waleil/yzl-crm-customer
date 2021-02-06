package cn.net.yzl.crm.customer.test;

import cn.net.yzl.crm.customer.CrmCustomerPlatformAppApplication;
import cn.net.yzl.crm.customer.dao.MemberActionRelationMapper;
import cn.net.yzl.crm.customer.dao.MemberDiseaseMapper;
import cn.net.yzl.crm.customer.dao.MemberMapper;
import cn.net.yzl.crm.customer.dao.MemberProductEffectMapper;
import cn.net.yzl.crm.customer.dao.mongo.MemberLabelDao;
import cn.net.yzl.crm.customer.model.mogo.ActionDict;
import cn.net.yzl.crm.customer.model.mogo.MemberDisease;
import cn.net.yzl.crm.customer.model.mogo.MemberLabel;
import cn.net.yzl.crm.customer.model.mogo.MemberProduct;
import cn.net.yzl.crm.customer.utils.MongoDateHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lichanghong
 * @version 1.0
 * @title: MemberLabelServiceTest
 * @description MongoDB数据测试
 * @date: 2021/1/25 10:47 下午MemberLabelServiceMongoImpl
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CrmCustomerPlatformAppApplication.class)
public class MemberLabelServiceTest {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberDiseaseMapper memberDiseaseMapper;
    @Autowired
    private MemberActionRelationMapper memberActionRelationMapper;
//    @Autowired
//    private MemberOrderStatMapper memberOrderStatMapper;
    @Autowired
    private MemberLabelDao memberLabelDao;//保存
//    @Autowired
//    private MemberAmountDao memberAmountDao;
    @Autowired
    private MemberProductEffectMapper memberProductEffectMapper;
    private static String member_label = "member_label";
//    @Test
    public void syncProcess() {

        int id = 0;
        int pageSize = 1000;
        while (true){
            List<MemberLabel> list = memberMapper.queryAllMemberByPage(id, pageSize);
            if (!CollectionUtils.isEmpty(list)) {
               System.out.println(String.format("同步顾客数据至MongoDB,当前页数:%s,分页大小:%s,数据大小:%s", id, pageSize, list.size()));
                //处理顾客编号
                List<String> memberCodes = list.stream().map(MemberLabel::getMemberCard)
                        .collect(Collectors.toList());
                //查询相关病症信息
                List<MemberDisease> memberDiseaseList = memberDiseaseMapper.queryByMemberCodes(memberCodes);
                Map<String, List<MemberDisease>> memberDiseaseListMap = memberDiseaseList.stream()
                        .collect(Collectors.groupingBy(MemberDisease::getMemberCard));
                //查询综合行为
                List<ActionDict> actionDictList = memberActionRelationMapper.queryByMemberCodes(memberCodes);
                Map<String, List<ActionDict>> actionDictListMap = actionDictList.stream()
                        .collect(Collectors.groupingBy(ActionDict::getMemberCard));
                //通过会员卡号查询顾客服用效果
                List<MemberProduct> memberProducts=memberProductEffectMapper.queryByMemberCodes(memberCodes);
                Map<String, List<MemberProduct>> memberProductsMap = memberProducts.stream()
                        .collect(Collectors.groupingBy(MemberProduct::getMemberCard));
                for (MemberLabel memberLabel : list) {
                    //是否有QQ
                    if (StringUtils.hasText(memberLabel.getQq())) {
                        memberLabel.setHasQQ(true);
                    } else {
                        memberLabel.setHasQQ(false);
                    }
                    //是否有邮箱
                    if (StringUtils.hasText(memberLabel.getEmail())) {
                        memberLabel.setHasEmail(true);
                    } else {
                        memberLabel.setHasEmail(false);
                    }
                    //是否有微信
                    if (StringUtils.hasText(memberLabel.getWechat())) {
                        memberLabel.setHasWechat(true);
                    } else {
                        memberLabel.setHasWechat(false);
                    }
                    //生日月份
                    if (StringUtils.hasText(memberLabel.getBirthday())) {
                        memberLabel.setMemberMonth(getMonth(memberLabel.getBirthday()));
                    }
                    //是否有余额
                    if(memberLabel.getTotalMoney()>0){
                        memberLabel.setHasMoney(true);
                    }else{
                        memberLabel.setHasMoney(false);
                    }
                    //处理创建时间，修改时间，首次下单时间，最后一个下单时间
                    if(Objects.nonNull(memberLabel.getCreateTime())){
                        memberLabel.setCreateTime(MongoDateHelper.getMongoDate(memberLabel.getCreateTime()));
                    }
                    if(Objects.nonNull(memberLabel.getLastSignTime())){
                        memberLabel.setLastSignTime(MongoDateHelper.getMongoDate(memberLabel.getLastSignTime()));
                    }
                    if(Objects.nonNull(memberLabel.getUpdateTime())){
                        memberLabel.setUpdateTime(MongoDateHelper.getMongoDate(memberLabel.getUpdateTime()));
                    }
                    if(Objects.nonNull(memberLabel.getFirstOrderTime())){
                        memberLabel.setFirstOrderTime(MongoDateHelper.getMongoDate(memberLabel.getFirstOrderTime()));

                    }
                    if(StringUtils.isEmpty(memberLabel.getFirstOrderNo()) && Objects.isNull(memberLabel.getFirstOrderTime())){
                        memberLabel.setHaveOrder(0);
                    }else{
                        memberLabel.setHaveOrder(1);
                    }
                    if(Objects.nonNull(memberLabel.getLastOrderTime())){
                        memberLabel.setLastOrderTime(MongoDateHelper.getMongoDate(memberLabel.getLastOrderTime()));
                    }
                    if(StringUtils.hasText(memberLabel.getFirstBuyProductCod())){
                        Set<String> set = StringUtils.commaDelimitedListToSet(memberLabel.getFirstBuyProductCod());
                        memberLabel.setFirstBuyProductCodes(new ArrayList<>(set));
                    }
                    if(StringUtils.hasText(memberLabel.getLastBuyProductCode())){
                        Set<String> set = StringUtils.commaDelimitedListToSet(memberLabel.getLastBuyProductCode());
                        memberLabel.setLastBuyProductCodes(new ArrayList<>(set));
                    }
                    memberLabel.set_id(memberLabel.getMemberCard());
                    String memberCard = memberLabel.getMemberCard();
                    //获取对应会员卡号的顾客的服用效果下信息
                    List<MemberProduct> products = memberProductsMap.get(memberCard);
                    //设置顾客服用效果
                    if(!CollectionUtils.isEmpty(products)){
                        memberLabel.setMemberProductList(products);
                    }
                    //todo 是否有积分、红包、优惠券要从DMC获取
                    //获取当前顾客的综合行为
                    List<ActionDict> actionDicts =actionDictListMap.get(memberCard);
                    //设置当前顾客的综合行为
                    if(!CollectionUtils.isEmpty(actionDicts)){
                        Map<Integer,List<ActionDict>> temp=actionDicts.stream().filter(s->s.getType()!=null).collect(Collectors.groupingBy(ActionDict::getType));
                        //方便接电话时间
                        if(CollectionUtils.isEmpty(temp.get(1))){
                            memberLabel.setPhoneDictList(temp.get(1));
                        }
                        //2性格偏好
                        if(CollectionUtils.isEmpty(temp.get(2))){
                            memberLabel.setMemberCharacterList(temp.get(2));
                        }
                        //3响应时间
                        if(CollectionUtils.isEmpty(temp.get(3))){
                            memberLabel.setMemberResponseTimeList(temp.get(3));
                        }
                        //综合行为
                        if(CollectionUtils.isEmpty(temp.get(5))){
                            memberLabel.setComprehensiveBehaviorList(temp.get(5));
                        }
                        //下单行为
                        if(CollectionUtils.isEmpty(temp.get(6))){
                            memberLabel.setOrderBehaviorList(temp.get(6));
                        }
                    }
                    List<MemberDisease> diseaseList = memberDiseaseListMap.get(memberCard);
                    if(CollectionUtils.isEmpty(diseaseList)){
                        memberLabel.setMemberDiseaseList(diseaseList);
                    }
                    if(memberLabel.getDbId()>id){
                        id = memberLabel.getDbId();
                    }
                    memberLabelDao.save(memberLabel);
                }
                if(list.size()<pageSize){
                    break;
                }
                //批量保存至MongoDB
                //memberLabelDao.bathSave(list);
                list.clear();

            } else {
                break;
            }
        }


    }

    private static Integer getMonth(String date) {
        String[] str = date.split("-");
        if(str.length>2){
            return Integer.parseInt(str[1]);
        }
        return 0;
    }
}
