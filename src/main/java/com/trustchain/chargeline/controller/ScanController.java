package com.trustchain.chargeline.controller;

import com.trustchain.chargeline.domain.JsonResult;
import com.trustchain.chargeline.solidity.Friend.Friend;
import com.trustchain.chargeline.solidity.Scan.Scan;
import com.trustchain.chargeline.util.ContractUtil;
import com.trustchain.chargeline.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping({"weChatPublic"})
public class ScanController {
    protected static final Logger logger = LoggerFactory.getLogger(ScanController.class);
    @Autowired
    ContractUtil contractUtil;

    /**
     * 增加扫描信息
     *
     * @param code
     * @param macvalue
     * @param ipaddress
     * @return
     */
    @RequestMapping({"/setMacValue"})
    @ResponseBody
    public JsonResult addInfo(String code, String macvalue, String ipaddress) {
        JsonResult jsonResult = new JsonResult();
        Scan scan = contractUtil.ScanLoad();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        try {
            TransactionReceipt receipt = scan.AddInfo(ipaddress, macvalue, date).send();
            jsonResult.setMessage("successful!!!");
            jsonResult.setState(JsonResult.SUCCESS);
            logger.info("增加扫描信息成功: IP:" + ipaddress + "; macvalue:" + macvalue + "; date:" + date);
            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setMessage("fail!!!");
            jsonResult.setState(JsonResult.ERROR);
            logger.error("增加扫描信息失败");
            return jsonResult;
        }
    }

    /**
     * 获得全部扫描信息
     *
     * @return
     */
    @RequestMapping({"/getScanInfo"})
    @ResponseBody
    public JsonResult getInfo() {
        JsonResult jsonResult = new JsonResult();
        Scan scan = contractUtil.ScanLoad();
        //获取信息的总数
        BigInteger total = null;
        try {
            total = scan.getInfoTotal().send();
            logger.info("成功获取信息总数：" + total);
        } catch (Exception e) {
            logger.error("获取信息总数失败");
            e.printStackTrace();
            jsonResult.setState(JsonResult.ERROR);
            jsonResult.setMessage("获取充电线总户数失败");
            return jsonResult;
        }

        //获取全部详细信息
        List<Map> data = new ArrayList<>();
        for (int i = 1; i <= total.intValue(); i++) {
            Tuple5<Boolean, String, String, String, String> info = null;
            try {
                info = scan.getInfoByID(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("获取id为" + i + "的信息失败");

            }
            if (info.getValue1()) {
                Map<String, String> map = new HashMap();
                map.put("ipaddress", info.getValue3());
                map.put("macvalue", info.getValue4());
                map.put("date", info.getValue5());
                data.add(map);
            } else {
                logger.warn("没有id为" + i + "信息");
            }
        }
        jsonResult.setMessage("信息总数：" + total);
        jsonResult.setState(JsonResult.SUCCESS);
        jsonResult.setData(data);
        return jsonResult;
    }

    /**
     * 增加粉丝
     *
     * @param request
     * @return
     */
    @RequestMapping(value = {"/getXmlData"}, method = {RequestMethod.POST})
    @ResponseBody
    public JsonResult addFriend(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        try {
            logger.info("开始获取xml===========================");
            Map<String, String> map = XmlUtil.xmlToMap(request);
            String openid = map.get("FromUserName");
            String name = map.get("ToUserName");
            String msgType = (String) map.get("MsgType");
            Friend friend = contractUtil.FriendLoad();
            Boolean bool = friend.isExist(openid).send();
            if ("event".equals(msgType) && !bool) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(new Date());
                TransactionReceipt receipt = friend.addFriend(openid, name, date).send();


                jsonResult.setState(JsonResult.SUCCESS);
                jsonResult.setMessage("粉丝增加成功");
                logger.info("粉丝增加成功");
                return jsonResult;

            } else {
                jsonResult.setState(JsonResult.ERROR);
                jsonResult.setMessage("粉丝已经存在");
                logger.info("粉丝已经存在");
                return jsonResult;
            }

        } catch (Exception e) {
            jsonResult.setState(JsonResult.ERROR);
            jsonResult.setMessage("获取xml数据失败");
            logger.error("获取xml数据失败=======================");
            e.printStackTrace();
            return jsonResult;
        }
    }

    @RequestMapping(value = {"/getAllFriendInfo"}, method = {RequestMethod.GET})
    @ResponseBody
    public JsonResult getAllFriendInfo(){
        JsonResult jsonResult=new JsonResult();
        Friend friend = contractUtil.FriendLoad();
        //获取信息的总数
        BigInteger total = null;
        try {
            total = friend.getFriendTotal().send();
            logger.info("成功粉丝总数：" + total);
        } catch (Exception e) {
            logger.error("获取粉丝总数失败");
            e.printStackTrace();
            jsonResult.setState(JsonResult.ERROR);
            jsonResult.setMessage("获取粉丝总数失败");
            return jsonResult;
        }

        //获取全部详细信息
        List<Map> data = new ArrayList<>();
        for (int i = 1; i <= total.intValue(); i++) {
            Tuple5<Boolean, String, String, String, String> info = null;
            try {
                info = friend.getFriendById(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("获取id为" + i + "的粉丝信息失败");

            }
            if (info.getValue1()) {
                Map<String, String> map = new HashMap();
                map.put("openId", info.getValue3());
                map.put("username", info.getValue4());
                map.put("date", info.getValue5());
                data.add(map);
            } else {
                logger.warn("没有id为" + i + "的粉丝信息");
            }
        }
        jsonResult.setMessage("粉丝总数：" + total);
        jsonResult.setState(JsonResult.SUCCESS);
        jsonResult.setData(data);
        return jsonResult;


    }

    public static void main(String[] args) throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:7545"));
        TransactionManager clientTransactionManager = new ClientTransactionManager(web3j, "0x25Dd6542f6434e586845f097BE40D62480E96E6a");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        Scan scan = Scan.load("0x962fF726dce4C62c35547Ec677f06a77932DE7E0", web3j, clientTransactionManager, contractGasProvider.getGasPrice(), contractGasProvider.getGasLimit());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        TransactionReceipt receipt = scan.AddInfo("1212222", "111111111", df.format(new Date())).send();
        logger.info(scan.getAddInfoEventEvents(receipt).get(0).date.toString());

    }
}
