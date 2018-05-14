package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.*;
import com.inschos.message.data.dao.MsgInboxDAO;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class MsgInboxAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgInboxAction.class);
    @Autowired
    private MsgInboxDAO msgInboxDAO;
    private Page page;

    /**
     * 站内信收件箱列表
     *
     * @param user_id|int           用户id
     * @param user_type|string      用户类型:个人用户 3/代理人 2/企业用户 1/管理员
     * @param message_status|string 站内信状态:未读 0/已读 1/全部 2/删除 3 （非必传，默认为0）
     * @param page                  当前页码 ，可不传，默认为1
     * @param last_id               上一页最大id ，可不传，默认为
     * @param limit                 每页显示行数，可不传，默认为
     * @return json
     * <p>
     * 业管可以查看所有人的站内信
     * 站内信列表组成：站内信系统表里收件人id为0的（系统消息）+ 站内信系统表里收件人id为user_id的（订阅消息、私信）
     * 匹配站内信系统表和站内信收件箱表，向用户收件箱里插入相应的数据，并修改站内信系统表的状态
     * todo 只要用户接收站内信，系统表就默认已经读取了，不在插入
     * @access public
     */
    public String getMsgRecList(ActionBean actionBean) {
        MsgInboxBean.inboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.inboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgRec msgRec = new MsgRec();
        msgRec.page = setPage(request.last_id, request.page_num, request.limit);
        msgRec.sys_status = request.message_status;
        msgRec.user_id = request.user_id;
        msgRec.user_type = request.user_type;
        List<MsgInbox> msgInboxes = msgInboxDAO.getMsgRecList(msgRec);
        response.data = msgInboxes;
        if (msgInboxes != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }


    /**
     * 收取站内信（系统把站内信同步到用户收件箱,同时修改系统发件表的状态）
     *
     * @param user_id|用户ID(收件人)
     * @param user_type|发件人类型，个人用户1/企业用户2/管理员等
     * @return mixed
     * @access public
     */
    public String insertMsgRec(long user_id,int user_type) {
        BaseResponse response = new BaseResponse();
        //判空
        if (user_id == 0||user_type==0) {
            return json(BaseResponse.CODE_FAILURE, "user_id or user_type is empty", response);
        }
        //查询站内信系统表有没有未插入的数据，没有的话，返回执行结束，有的话继续执行（赋值，插入，改变状态）
        MsgRec msgRec = new MsgRec();
        msgRec.user_id = user_id;
        msgRec.user_type = user_type;
        List<MsgRec> msgRecs =  msgInboxDAO.getUserMsgRes(msgRec);
        //判断集合是否为空
        return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
    }

    /**
     * 站内信发件箱列表
     *
     * @param user_id|int           用户id
     * @param user_type|string      用户类型:个人用户 3/代理人 2/企业用户 1/管理员 0
     * @param message_status|string 站内信状态:未读 0/已读 1/全部 2/删除 3 （非必传，默认为0）
     * @param page                  当前页码 ，可不传，默认为1
     * @param last_id               上一页最大id ，可不传，默认为
     * @param limit                 每页显示行数，可不传，默认为
     * @return json
     * @access public
     */
    public String getMsgSysList(ActionBean actionBean) {
        MsgInboxBean.outboxListRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.outboxListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgSys msgSys = new MsgSys();
        msgSys.page = setPage(request.last_id, request.page_num, request.limit);
        msgSys.status = request.message_status;
        msgSys.from_id = request.user_id;
        msgSys.from_type = request.user_type;
        List<MsgSys> msgOutboxs = msgInboxDAO.getMsgSysList(msgSys);
        response.data = msgOutboxs;
        if (msgOutboxs != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 站内信详情
     *
     * @param message_id 站内信id
     * @return json
     * @access public
     */
    public String getMsgInfo(ActionBean actionBean) {
        MsgInboxBean.msgInfoRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.msgInfoRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        MsgInbox msgInbox = msgInboxDAO.getMsgInfo(request.message_id);
        response.data = msgInbox;
        if (msgInbox != null) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 操作站内信（收件箱 读取和删除）
     *
     * @param message_id   站内信id
     * @param operate_id   操作代码:默认为1（删除/已读），2（还原/未读）
     * @param operate_type 操作类型:read 更改读取状态，del 更改删除状态
     * @return json
     * @access public
     */
    public String updateMsgRec(ActionBean actionBean) {
        MsgInboxBean.msgUpdateRequest request = JsonKit.json2Bean(actionBean.body, MsgInboxBean.msgUpdateRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "params is empty", response);
        }
        //调用DAO
        MsgUpdate msgUpdate = new MsgUpdate();
        msgUpdate.msg_id = request.message_id;
        msgUpdate.operate_id = request.operate_id;
        msgUpdate.operate_type = request.operate_type;
        int updateRes = msgInboxDAO.updateMsgRec(msgUpdate);
        if (updateRes != 0) {
            return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }
}
