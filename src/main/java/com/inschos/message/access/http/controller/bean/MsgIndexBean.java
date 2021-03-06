package com.inschos.message.access.http.controller.bean;

import com.inschos.message.annotation.CheckParams;

import java.util.List;

public class MsgIndexBean {

    public String title;//'标题'

    @CheckParams(stringType = CheckParams.StringType.STRING, minLen = 1)
    public String content;//'内容'

    public String attachment;//'附件，上传附件的URL,可为空'

    public int type;//'消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'

    @CheckParams(stringType = CheckParams.StringType.NUMBER, minLen = 1)
    public long fromId;//'发件人ID

    @CheckParams(stringType = CheckParams.StringType.NUMBER, minLen = 1)
    public int fromType;//'发件人类型，个人1/企业2/代理人3/业管4

    public List<AddMsgToBean> toUser;

    public String sendTime;//'发送时间:可为空。需要延时发送的，发送时间不为空'

    public String businessId = "-1";//业务id

}
