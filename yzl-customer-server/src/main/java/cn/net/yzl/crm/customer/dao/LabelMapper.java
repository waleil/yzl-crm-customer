//package cn.net.yzl.crm.customer.dao;
//
//import cn.net.yzl.crm.customer.model.Label;
//import cn.net.yzl.crm.customer.model.LabelGroup;
//import cn.net.yzl.crm.customer.model.LabelType;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Select;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Deprecated
//@Mapper
//public interface LabelMapper {
//
//     List<LabelGroup> getLabelGroups();
//
//     @Select("SELECT id, pid, `name`, field_name fieldName, `code`, value1, value2, label_type labelType,limit_dn limitDn, limit_up limitUp,\n" +
//             "  sort, url, check_box checkBox, group_id groupId, route from  label")
//     List<Label> getCustomerLabels();
//
//     @Select("SELECT id,`name` as labelName from  label_type_base")
//     List<LabelType> selectAllLabelTypes();
//
//}
