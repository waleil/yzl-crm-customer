package cn.net.yzl.crm.customer.utils.mongo;

import cn.net.yzl.common.entity.Page;
import cn.net.yzl.common.entity.PageParam;

import java.util.List;

/**
 * @Description mongo分页，为了和公司其他mybatis分页保持一致，用此类转换一下
 * @Author xuwei
 * @Date 2021/1/6 2:21 下午
 */
public class PageUtil {
    public PageUtil(){}

    public static <T> Page<T> resultAssembler(List<T> list, Integer pageNo, Integer pageSize,Integer totalCount){
        Page pageInfo=new Page();//分页返回的总对象类型
        PageParam pageParam =new PageParam();//分页信息
        pageParam.setPageNo(pageNo);//当前页码
        pageParam.setPageSize(pageSize);//每页显示条目
        pageParam.setTotalCount((int)totalCount);//总记录数
        int pageTotal = 0;
        if(pageSize >= 0) {
            pageTotal=(int)Math.ceil((double)(int)totalCount / (double)pageSize);//计算总页数
        }
        pageParam.setPageTotal(pageTotal);
        pageParam.setNextPage(pageTotal == pageNo ? 0 : pageNo+1);//下一页，没有下一页为0
        pageParam.setPreviousPage(pageNo == 0 ? 0:pageNo-1);//上一页，没有上一页为0
        pageInfo.setItems(list);
        pageInfo.setPageParam(pageParam);
        return pageInfo;
    }
}
