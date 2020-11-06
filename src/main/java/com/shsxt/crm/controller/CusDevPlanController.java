package com.shsxt.crm.controller;

import com.shsxt.crm.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    /**
     * 进入计划开发计划
     * @return
     */
    @RequestMapping("index")
    public String index(){

        return "cusDevPlan/cus_dev_plan";
    }
}
