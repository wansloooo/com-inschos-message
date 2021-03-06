package com.inschos.message.access.http.controller.request;

import com.inschos.message.access.http.controller.action.WorkOrderAction;
import com.inschos.message.access.http.controller.bean.ActionBean;
import com.inschos.message.annotation.GetActionBeanAnnotation;
import com.inschos.message.assist.kit.StringKit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * User: changyy
 * Date: 2018/05/19
 * Time: 17:31
 * 工单管理主要功能：创建工单，工单列表，工单详情，回复工单（关闭），工单状态流转
 */
@Controller
@RequestMapping("/web/work/")
public class WorkOrderController {
    private static final Logger logger = Logger.getLogger(WorkOrderController.class);
    @Autowired
    private WorkOrderAction workOrderAction;

    /**
     * 创建工单
     *
     * @params actionBean     工单
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation
    @RequestMapping(value = "/add/**",method = RequestMethod.POST)
    @ResponseBody
    public String addWork(ActionBean actionBean){
        return workOrderAction.addWork(actionBean);
    }

    /**
     * 工单，工单的气泡数量
     *
     * @param actionBean     工单
     * @return json
     * @access public
     */
    @GetActionBeanAnnotation
    @RequestMapping(value = "/untreated_count/**",method = RequestMethod.POST)
    @ResponseBody
    public String untreatedCount(ActionBean actionBean){
        return workOrderAction.untreatedCount(actionBean);
    }


    @GetActionBeanAnnotation
    @RequestMapping("/agent/my")
    @ResponseBody
    public String listOfAgentMy(ActionBean bean){
        return workOrderAction.listOfAgentMy(bean);
    }

    @GetActionBeanAnnotation
    @RequestMapping("/list/tome/**")
    @ResponseBody
    public String listToMe(ActionBean bean){
        String method = StringKit.splitLast(bean.url, "/");
        return workOrderAction.listToMe(bean,method);
    }

    @GetActionBeanAnnotation
    @RequestMapping("/categoryList")
    @ResponseBody
    public String categoryList(ActionBean bean){
        return workOrderAction.categoryList(bean);
    }

    @GetActionBeanAnnotation
    @RequestMapping("/reply")
    @ResponseBody
    public String reply(ActionBean bean){
        return workOrderAction.reply(bean);
    }


    @GetActionBeanAnnotation
    @RequestMapping("/score")
    @ResponseBody
    public String score(ActionBean bean){
        return workOrderAction.score(bean);
    }


    @GetActionBeanAnnotation
    @RequestMapping("/detail")
    @ResponseBody
    public String detail(ActionBean bean){
        return workOrderAction.detail(bean);
    }



}
