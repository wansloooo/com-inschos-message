package com.inschos.message.access.http.controller.bean;

import com.inschos.message.annotation.CheckParamsKit;

import java.util.List;

public class BaseResponse {
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_FAILURE = 500;
    public static final int CODE_PARAM_ERROR = 500;
    public static final int CODE_VERSION_FAILURE = 501;
    public static final int CODE_ACCESS_FAILURE = 502;
    public static final int CODE_VERIFY_CODE = 600;
    public int code;
    public List<CheckParamsKit.Entry<String, String>> message;
    public Object data;
    public PageBean page;

}
