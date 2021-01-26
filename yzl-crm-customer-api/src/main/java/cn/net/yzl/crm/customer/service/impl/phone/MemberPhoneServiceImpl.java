package cn.net.yzl.crm.customer.service.impl.phone;

import cn.hutool.core.collection.CollectionUtil;
import cn.net.yzl.common.entity.ComResponse;
import cn.net.yzl.common.enums.ResponseCodeEnums;
import cn.net.yzl.crm.customer.dao.MemberPhoneMapper;
import cn.net.yzl.crm.customer.model.Member;
import cn.net.yzl.crm.customer.model.db.MemberPhone;
import cn.net.yzl.crm.customer.service.MemberPhoneService;
import cn.net.yzl.crm.customer.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MemberPhoneServiceImpl implements MemberPhoneService {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberPhoneMapper memberPhoneMapper;

    @Override
    public ComResponse<String> getMemberCard(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"电话号不能为空!");
        }
        phoneNumber = phoneNumber.trim();
        //电话号码最少10位
        if (phoneNumber.length() < 10) {
            return ComResponse.fail(ResponseCodeEnums.PARAMS_ERROR_CODE.getCode(),"电话号格式不正确!");
        }

        String haveZeroNumber = "";
        String noZeroNumber = "";


        //是否以0开头 --> 去掉0
        if (phoneNumber.startsWith("0")){
            haveZeroNumber = phoneNumber;
            noZeroNumber = phoneNumber.substring(1);
        }else{
            haveZeroNumber = "0" + phoneNumber;
            noZeroNumber = phoneNumber;
        }
        /**
         * 1.校验手机号，电话号码是否正确
         */
        if (!isMobile(noZeroNumber) && !isPhone(haveZeroNumber)) {
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

        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add(haveZeroNumber);
        phoneNumbers.add(noZeroNumber);
        List<String> memberCards= memberPhoneMapper.getMemberCardByPhoneNumbers(phoneNumbers);
        String memberCard = "";
        if (CollectionUtil.isNotEmpty(memberCards)) {
            memberCard = memberCards.get(0);
        }else{
            //新建会员
            Member member = new Member();
            int result = memberService.insert(member);
            if (result == 1) {
                memberCard = member.getMember_card();
            }
            //保存member_phone记录
            MemberPhone memberPhone = new MemberPhone();
            memberPhone.setMemberCard(memberCard);
            memberPhone.setPhoneNumber(phoneNumber);
            memberPhoneMapper.insert(memberPhone);

        }

        /**
         * 3.接口返回member_card
         */
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
        p = Pattern.compile("^[1][3,4,5,7,8,9][0-9]{9}$"); // 验证手机号
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


}
