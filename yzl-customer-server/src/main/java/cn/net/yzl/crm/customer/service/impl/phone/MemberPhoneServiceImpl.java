package cn.net.yzl.crm.customer.service.impl.phone;

import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberPhoneMapper;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.MemberPhone;
import cn.net.yzl.crm.customer.service.MemberPhoneService;
import cn.net.yzl.crm.customer.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemberPhoneServiceImpl implements MemberPhoneService {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberPhoneMapper memberPhoneMapper;
    
    private static final String PREFIX_ZERO = "0";

    @Override
    @Transactional
    public ComResponse<String> getMemberCardByphoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"电话号不能为空!");
        }
        phoneNumber = phoneNumber.trim();
        //电话号码最少10位
        if (phoneNumber.length() < 10) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"电话号格式不正确!");
        }

        String noZeroNumber = "";
        String haveZeroNumber = "";

        //是否以0开头 --> 去掉0
        if (phoneNumber.startsWith(PREFIX_ZERO)){
            noZeroNumber = phoneNumber.substring(1);
            haveZeroNumber = phoneNumber;
        }else{
            noZeroNumber = phoneNumber;
            haveZeroNumber = PREFIX_ZERO + phoneNumber;
        }
        //校验手机号
        int phoneType = 0;
        if (isMobile(noZeroNumber)) {
            phoneType = 1;
        }
        //不是手机号时，要校验是否为电话号
        if (phoneType == 0 && isPhone(phoneNumber)){
            phoneType = 2;
        }
        if (phoneType == 0) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"电话号格式不正确!");
        }
        /**
         * 2.查询member_phone中电话号码是否存在
         *      1.存在时：
         *          说明已经绑定了
         *
         *      2.不存在时：
         *          判断入参传入的电话号码是座机还是手机号(phone_type: '1 移动电话，2座机')
         *          添加一条member_phne记录
         *          添加一条member记录
         */
        String memberCard= memberPhoneMapper.getMemberCardByPhoneNumber(Arrays.asList(haveZeroNumber,noZeroNumber));
        if (StringUtils.isNotEmpty(memberCard)) {
            log.info("通过phoneNumber：{}，查询到对应的会员信息,会员号为：{}", phoneNumber, memberCard);
        }else{
            log.info("通过phoneNumber：{}，没有查询到对应的会员信息。",phoneNumber);
            //新建会员
            Member member = new Member();
            member.setCreate_time(new Date());
            member.setUpdate_time(member.getCreate_time());
            member.setSource_type(0);
            int result = memberService.insert(member);
            if (result == 1) {
                //保存member_phone记录
                MemberPhone memberPhone = new MemberPhone();
                memberPhone.setPhone_number(phoneNumber);
                memberPhone.setPhone_type(phoneType);
                memberPhone.setMember_card(member.getMember_card());
                memberPhone.setCreator_no(member.getCreator_no());
                memberPhone.setCreate_time(new Date());
                memberPhone.setUpdator_no(memberPhone.getCreator_no());
                memberPhone.setUpdate_time(memberPhone.getCreate_time());
                if (memberPhone.getEnabled() == null) {
                    memberPhone.setEnabled(1);//默认可用
                }
                result = memberPhoneMapper.insert(memberPhone);
            }
            if (result == 1) {
                memberCard = member.getMember_card();
                log.info("通过phoneNumber：{}，新增会员信息,会员号为：{}",phoneNumber,memberCard);
            }else{
                log.error("通过phoneNumber：{}，新增会员异常");
            }
        }
        //3.接口返回member_card
        if (StringUtils.isEmpty(memberCard)) {
            return ComResponse.fail(ResponseCodeEnums.SYSTEM_ERROR_CODE.getCode(),"操作异常!");
        }else{
            return ComResponse.success(memberCard);
        }
    }

    /**
     * 手机号验证
     *
     * @param  str 手机号
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     * 加上区号11位。
     *
     * 如果区号是3位，电话du号码是8位。例如北京：010-XXXXXXXX。
     *
     * 如果区号是dao4位，zhuan电话号码是7位。例如长沙：0731-XXXXXXX。
     *
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }

        String phoneNum = str;
        if (str.startsWith("0")) {
            phoneNum = phoneNum.substring(1);
        }
        Pattern p;
        Matcher m;
        //先截取前两位
        String areaCode = phoneNum.substring(0, 2);
        if (Integer.valueOf(areaCode) > 30) {
            p = Pattern.compile("^0?\\d{2}\\d{8}$");
        }else{
            p = Pattern.compile("^0?\\d{3}\\d{7}$");
        }
        m = p.matcher(str);
        return m.matches();
    }

    @Override
    public ComResponse<Member> getMemberByphoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"电话号码不能为空!");
        }
        phoneNumber = phoneNumber.trim();

        String noZeroNumber = "";
        String haveZeroNumber = "";

        //是否以0开头 --> 去掉0
        if (phoneNumber.startsWith(PREFIX_ZERO)){
            noZeroNumber = phoneNumber.substring(1);
            haveZeroNumber = phoneNumber;
        }else{
            noZeroNumber = phoneNumber;
            haveZeroNumber = PREFIX_ZERO + phoneNumber;
        }
        String memberCard = memberPhoneMapper.getMemberCardByPhoneNumber(Arrays.asList(haveZeroNumber,noZeroNumber));
        Member memberEntity = null;
        if (StringUtils.isNotEmpty(memberCard)) {
            //根据会员号查询会员信息
            memberEntity = memberService.selectMemberByCard(memberCard);
        }
        return ComResponse.success(memberEntity);

    }


    /**
     * 获取顾客联系方式信息，包括手机号，座机号
     *
     * @param member_card
     * @return
     */
    @Override
    public List<MemberPhone> getMemberPhoneList(String member_card) {
        List<MemberPhone> memberPhoneList = memberPhoneMapper.getMemberPhoneList(member_card);
        if(CollectionUtils.isEmpty(memberPhoneList)){
            return Collections.emptyList();
        }
        List<MemberPhone> temp=  memberPhoneList.stream().filter(v->v.getEnabled()==1).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(temp)){
            return Collections.emptyList();
        }
        if(temp.size()==1){
            return temp;
        }
        Collections.sort(temp, new Comparator<MemberPhone>() {
            @Override
            public int compare(MemberPhone o1, MemberPhone o2) {
                if(o1.getUpdate_time()==null||o2.getUpdate_time()==null){
                    return 0;
                }
                return (int) ((int) o2.getUpdate_time().getTime()-o1.getUpdate_time().getTime());
            }
        });
        return temp;
    }

    @Override
    public List<String> getMemberCardByphoneNumbers(List<String> phoneNumbers) {
        List<String> memberCards = new ArrayList<>();
        List<String> list = formatPhoneNumber(phoneNumbers);
        if (CollectionUtil.isNotEmpty(list)) {
            memberCards = memberPhoneMapper.getMemberCardByPhoneNumbers(list);
        }
        return memberCards;
    }



    private List<String> formatPhoneNumber(List<String> phoneNumbers) {
        List<String> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(phoneNumbers)) {
            return list;
        }
        String noZeroNumber = "";
        String haveZeroNumber = "";
        for (String phoneNumber : phoneNumbers) {
            if (StringUtils.isEmpty(phoneNumber.trim())) {
                continue;
            }
            phoneNumber = phoneNumber.trim();
            //是否以0开头 --> 去掉0
            if (phoneNumber.startsWith(PREFIX_ZERO)){
                noZeroNumber = phoneNumber.substring(1);
                haveZeroNumber = phoneNumber;
            }else{
                noZeroNumber = phoneNumber;
                haveZeroNumber = PREFIX_ZERO + phoneNumber;
            }
            list.add(noZeroNumber);
            list.add(haveZeroNumber);
        }
        return list;
    }
}
