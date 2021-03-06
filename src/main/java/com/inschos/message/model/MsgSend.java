package com.inschos.message.model;

public class MsgSend {

    public String title;//'标题'

    public String content;//'内容'

    public String attachment;//'附件，上传附件的URL,可为空'

    public String type;//'消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/'

    public long from_id;//'发件人ID

    public int from_type;//'发件人类型，个人1/企业2/代理人3/业管4

    public MsgTo msgTo;//'收件人'

    public int status = 1;//'读取状态:标识消息 是否已被读取,未读1/已读2.避免重复向收件箱表插入数据,默认为1'

    public String send_time;//'发送时间:可为空。需要延时发送的，发送时间不为空'

    public int state = 1;//'删除标识:默认为0，1未删除'

    public long created_at;//'创建时间，毫秒'

    public long updated_at;//'更新时间，毫秒'

}
