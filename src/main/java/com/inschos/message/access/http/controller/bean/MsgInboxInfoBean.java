package com.inschos.message.access.http.controller.bean;

import java.util.List;

public class MsgInboxInfoBean {

    public long id;

    public String title;

    public List<String> content;

    public int messageType;//消息类型

    public String messageTypeText;//消息类型文案

    public long readFlag;//读取标识

    public long time;//最新消息时间

    public String timeTxt;//最新消息时间文案

    public List<MsgToBean> msgToBean;//收件人详细信息
}
