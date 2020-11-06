package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import com.shsxt.crm.base.ResultInfo;
import com.shsxt.crm.query.SaleChanceQuery;
import com.shsxt.crm.service.SaleChanceService;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.CookieUtil;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    /**
     * 多条件查询列表
     * @param saleChanceQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryByParams(SaleChanceQuery saleChanceQuery,HttpServletRequest request,Integer flag){
        //判断查询客户是否是开发状态
        if(null != flag && flag == 1){
            //得到用户ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            //设置指派人是当前用户登录
            saleChanceQuery.setAssignMan(userId);
        }

        return saleChanceService.queryByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }



    /**
     * 修改和编辑页面
     * @return
     */
    @RequestMapping("toAddOrUpdatePage")
    public String toAddOrUpdatePage(HttpServletRequest request , Integer saleChanceId){
        if(saleChanceId != null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(saleChanceId);
            request.setAttribute("saleChance",saleChance);
        }

        return "saleChance/add_update";
    }

    /**
     * 添加营销机会
     * @param saleChance
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance){
        //从cookie中获取用户名
       // String userName = CookieUtil.getCookieValue(request,"userName");

        saleChanceService.addSaleChance(saleChance);
        ResultInfo resultInfo = success();
        resultInfo.setCode(200);
        return resultInfo;
    }

    /**
     * 修改营销机会
     * @param saleChance
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){

        saleChanceService.updateSaleChance(saleChance);
        ResultInfo resultInfo = success();
        resultInfo.setCode(200);
        return resultInfo;
    }

    /**
     * 删除营销机会
     * @param ids
     * @return
     */
    @ResponseBody
    @PostMapping("delete")
    public ResultInfo deleteSaleChance(Integer[] ids){

        saleChanceService.deleteSaleChance(ids);
        ResultInfo resultInfo = success();
        resultInfo.setCode(200);
        return resultInfo;
    }
}
