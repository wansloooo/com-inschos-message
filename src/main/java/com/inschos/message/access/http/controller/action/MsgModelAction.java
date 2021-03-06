package com.inschos.message.access.http.controller.action;

import com.inschos.message.access.http.controller.bean.*;
import com.inschos.message.data.dao.*;
import com.inschos.message.assist.kit.JsonKit;
import com.inschos.message.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class MsgModelAction extends BaseAction {
    private static final Logger logger = Logger.getLogger(MsgModelAction.class);
    @Autowired
    private MsgModelDAO msgModelDAO;
    private Page page;

    /**
     * 添加消息 模板
     *
     * @paramss modelName       模板名称（不能一样）
     * @paramss modelContent    模板内容
     * @paramss modelType       模板类型
     * @paramss createdUser     创建者姓名
     * @paramss createdUserType 创建者类型
     * @return json
     * @access public
     */
    public String addMsgModel(ActionBean actionBean) {
        MsgModelBean.AddRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.AddRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "参数解析失败", response);
        }
        MsgStatus msgStatus = new MsgStatus();
        if (actionBean.userType != msgStatus.USER_MANAGER) {//TODO 只有业管才能添加模板 ??
            return json(BaseResponse.CODE_FAILURE, "您没有权险执行这项操作", response);
        }
        //获取当前时间戳(毫秒值)
        long date = new Date().getTime();
        String code = getStringRandom(6);
        //赋值
        if(actionBean.managerUuid==null){
            actionBean.managerUuid = "-1";
        }
        MsgModel msgModel = new MsgModel();
        msgModel.model_code = code;
        msgModel.model_name = request.modelName;
        msgModel.model_content = request.modelContent;
        msgModel.model_type = request.modelType;
        msgModel.created_user =Integer.parseInt(actionBean.managerUuid);
        msgModel.created_user_type = actionBean.userType;
        msgModel.status = 0;
        msgModel.state = 1;
        msgModel.created_at = date;
        msgModel.updated_at = date;
        //调用DAO
        //判断模板是否重复
        MsgModel msgModelRepeat = msgModelDAO.findMsgModelRepeat(msgModel);
        if (msgModelRepeat != null) {
            return json(BaseResponse.CODE_FAILURE, "模板已存在，请检查模板名称", response);
        }
        int add_res = msgModelDAO.addMsgModel(msgModel);
        if (add_res == 1) {
            return json(BaseResponse.CODE_SUCCESS, "模板创建成功，等待审核", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "模板创建失败", response);
        }
    }

    /**
     * 消息 模板列表
     *
     * @paramss pageNum     当前页码 ，可不传，默认为1
     * @paramss lastId      上一页最大id ，可不传，默认为
     * @paramss pageSize       每页显示行数，可不传，默认为
     * @paramss modelStatus 模板状态（审核通过0/未通过1/已删除2）
     * @paramss modelSype   模板类型
     * @return json
     * @access public
     */
    public String listMsgModel(ActionBean actionBean) {
        MsgModelBean.ListRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.ListRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "参数解析失败", response);
        }
        //调用DAO
        MsgModelList msgModelList = new MsgModelList();
        msgModelList.page = setPage(request.lastId, request.pageNum, request.pageSize);
        MsgModel msgModel = new MsgModel();
        msgModel.status = request.modelStatus;
        msgModel.model_type = request.modelType;
        msgModelList.msgModel = msgModel;
        List<MsgModel> msgModels = msgModelDAO.findMsgModelList(msgModelList);
        List<MsgModelListBean> msgModelListBeans = new ArrayList<>();
        MsgModelListBean msgModelListBean = new MsgModelListBean();
        for (MsgModel model : msgModels) {
            msgModelListBean.id = model.id;
            msgModelListBean.modelCode = model.model_code;
            msgModelListBean.modelName = model.model_name;
            msgModelListBean.modelType = model.model_type;
            msgModelListBeans.add(msgModelListBean);
        }
        response.data = msgModelListBeans;
        if (msgModelListBeans != null) {
            return json(BaseResponse.CODE_SUCCESS, "获取列表成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "获取列表失败", response);
        }
    }

    /**
     * 消息 模板详情
     *
     * @paramss modelCode 模板代码
     * @return json
     * @access public
     */
    public String infoMsgModel(ActionBean actionBean) {
        MsgModelBean.InfoRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.InfoRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "参数解析错误", response);
        }
        //调用DAO
        MsgModel msgModel = new MsgModel();
        msgModel.model_code = request.modelCode;
        MsgModel modelInfo = msgModelDAO.findMsgModelInfo(msgModel);
        if (modelInfo == null) {
            response.data = "";
            return json(BaseResponse.CODE_FAILURE, "获取详情失败", response);
        }
        MsgModelInfoBean msgModelInfoBean = new MsgModelInfoBean();
        msgModelInfoBean.id = modelInfo.id;
        msgModelInfoBean.modelCode = modelInfo.model_code;
        msgModelInfoBean.modelName = modelInfo.model_name;
        msgModelInfoBean.modelContent = modelInfo.model_content;
        msgModelInfoBean.modelType = modelInfo.model_type;
        response.data = msgModelInfoBean;
        if (msgModelInfoBean != null) {
            return json(BaseResponse.CODE_SUCCESS, "获取详情成功", response);
        } else {
            return json(BaseResponse.CODE_FAILURE, "获取详情失败", response);
        }
    }

    /**
     * 消息 模板操作（审核、删除）
     *
     * @paramss modelCode 模板代码
     * @paramss status    模板状态（审核通过1，删除2）
     * @paramss modelType 模板类型
     * @return json
     * @access public
     */
    public String updateMsgModel(ActionBean actionBean) {
        MsgModelBean.UpdateRequest request = JsonKit.json2Bean(actionBean.body, MsgModelBean.UpdateRequest.class);
        BaseResponse response = new BaseResponse();
        //判空
        if (request == null) {
            return json(BaseResponse.CODE_FAILURE, "参数解析失败", response);
        }
        MsgStatus msgStatus = new MsgStatus();
        if (actionBean.userType != msgStatus.USER_MANAGER) {//TODO 只有业管用户才能操作消息 模板？？
            return json(BaseResponse.CODE_FAILURE, "您没有权限执行这箱操作", response);
        }
        //赋值
        MsgModelUpdate modelUpdate = new MsgModelUpdate();
        modelUpdate.model_code = request.modelCode;
        if (request.status == 0 && request.modelType == 0) {
            return json(BaseResponse.CODE_FAILURE, "请检查要操作的内容", response);
        }
        if (request.status != 0 || request.modelType == 0) {
            modelUpdate.status = request.status;
            //调用DAO
            int updateRes = msgModelDAO.updateMsgModelStatus(modelUpdate);
            if (updateRes != 0) {
                return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
            } else {
                return json(BaseResponse.CODE_FAILURE, "操作失败", response);
            }
        }
        if (request.modelType != 0 || request.status == 0) {
            modelUpdate.status = request.status;
            modelUpdate.model_type = request.modelType;
            //调用DAO
            int updateRes = msgModelDAO.updateMsgModelType(modelUpdate);
            if (updateRes != 0) {
                return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
            } else {
                return json(BaseResponse.CODE_FAILURE, "操作失败", response);
            }
        }
        if (request.status != 0 && request.modelType != 0) {
            modelUpdate.status = request.status;
            modelUpdate.model_type = request.modelType;
            //调用DAO
            int updateRes = msgModelDAO.updateMsgModel(modelUpdate);
            if (updateRes != 0) {
                return json(BaseResponse.CODE_SUCCESS, "操作成功", response);
            } else {
                return json(BaseResponse.CODE_FAILURE, "操作失败", response);
            }
        }else{
            return json(BaseResponse.CODE_FAILURE, "操作失败", response);
        }
    }

    /**
     * 生成随机数字和字母
     *
     * @paramss length
     * @return
     */
    public String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
