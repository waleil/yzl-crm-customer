package cn.net.yzl.crm.customer.dto;


import cn.net.yzl.common.entity.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel(value="分页参数类",description="参数类" )
@Data
public class PageDTO implements Serializable {

    @ApiModelProperty(value = "当前页码",required = true)
    protected Integer currentPage=1;

    @ApiModelProperty(value = "每页条数",required = true)
    protected Integer pageSize=20;

    @ApiModelProperty(value = "查询的开始行数",hidden  = true)
    protected  Integer fromLine;

    public  int getFromLine(){
        return (this.currentPage-1)*this.pageSize;
    }



    public <T> Page<T> toPage(List<T> list,Integer totalCount) {
        Page<T> pageInfo = new Page<>();// 分页返回的总对象类型
        cn.net.yzl.common.entity.PageParam pageParam = new cn.net.yzl.common.entity.PageParam();// 分页信息
        pageParam.setPageNo(currentPage);// 当前页码
        pageParam.setPageSize(pageSize);// 每页显示条目
        pageParam.setTotalCount((int) totalCount);// 总记录数
        int pageTotal = 0;
        if (pageSize >= 0) {
            pageTotal = (int) Math.ceil((double) (int) totalCount / (double) pageSize);// 计算总页数
        }
        pageParam.setPageTotal(pageTotal);
        pageParam.setNextPage(pageTotal == currentPage ? 0 : currentPage + 1);// 下一页，没有下一页为0
        pageParam.setPreviousPage(currentPage == 0 ? 0 : currentPage - 1);// 上一页，没有上一页为0
        pageInfo.setItems(list);
        pageInfo.setPageParam(pageParam);
        return pageInfo;
    }
}
