package com.trustchain.chargeline.controller;

import com.alibaba.fastjson.JSONObject;
import com.trustchain.chargeline.domain.JsonResult;
import com.trustchain.chargeline.util.HttpPostUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

//@Controller
//@RequestMapping({"heisha"})
public class ScanRelationController {
    @Resource(name = "httpPostUtil")
    HttpPostUtil httpPostUtil;

    /**
     * 增加好友
     *
     * @param request
     */
//    @RequestMapping(value = {"/callbackAddUser"}, method = {RequestMethod.POST})
//    @ResponseBody
    public void callbackAddUser(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        jsonResult = this.httpPostUtil.readReqStr(request);
        if (jsonResult.getState() == 0) {
            String data = ((String[]) (String[]) jsonResult.getData())[0];
            JSONObject obj = JSONObject.parseObject(data);
            String myaccount = "";
            String toaccount = "";
            int count = 0;
            int num = 0;
            if (obj.containsKey("my_account")) {
                myaccount = (String) obj.get("my_account");
                count = 1;
            }
            if (obj.containsKey("to_account")) {
                num = 1;
                toaccount = (String) obj.get("to_account");
            }
            if ((count > 0) && (num > 0)) {
                if(obj.containsKey("data")){
                    String datachild = (String)obj.get("data");
                    net.sf.json.JSONObject objchild = net.sf.json.JSONObject.fromObject(datachild);

                }
            }
        }
    }
}
