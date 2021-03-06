package com.inschos.message.data.dao;

import com.inschos.message.data.mapper.*;
import com.inschos.message.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MsgIndexDAO {
    @Autowired
    private MsgIndexMapper msgIndexMapper;
    /**
     * 发送消息
     * @access public
     * @params title|标题
     * @params content|内容
     * @params attachment|附件:上传附件的URL,可为空
     * @params type|消息 类型:系统通知1/保单助手2/理赔进度3/最新任务4/客户消息5/活动消息6/顾问消息7/
     * @params from_id|发件人ID
     * @params from_type|发件人类型:个人用户1/企业用户2/业管用户等3
     * @params to_id|收件人id
     * @params to_type|收件人类型:个人用户1/企业用户2/业管用户等3
     * @params channel|渠道
     * @params status|读取状态:标识消息 是否已被读取,未读0/已读1.避免重复向收件箱表插入数据,默认为0
     * @params send_time|发送时间:默认为空。需要延时发送的，发送时间不为空
     * @return mixed
     *
     */
    public int addMsgSys(MsgSys msgSys){
        return msgIndexMapper.addMsgSys(msgSys);
    }

    public int addMessage(MsgSys msgSys){
            return msgIndexMapper.addMessage(msgSys);
    }

    public int addMessageRecord(MsgRecord msgRecord){
        return msgIndexMapper.addMessageRecord(msgRecord);
    }

    public RepeatCount findAddMsgRecordRepeat(MsgRecord msgRecord){
        return msgIndexMapper.findAddMsgRecordRepeat(msgRecord);
    }

    public int addMessageToRecord(MsgToRecord msgToRecord){
        return msgIndexMapper.addMessageToRecord(msgToRecord);
    }

    public RepeatCount findMessageToRecordRepeat(MsgToRecord msgToRecord){
        return msgIndexMapper.findMessageToRecordRepeat(msgToRecord);
    }

}
