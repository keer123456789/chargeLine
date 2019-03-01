package com.trustchain.chargeline.controller;

import com.trustchain.chargeline.domain.JsonResult;
import com.trustchain.chargeline.solidity.Friend.Friend;
import com.trustchain.chargeline.solidity.Scan.Scan;
import com.trustchain.chargeline.util.ContractUtil;
import com.trustchain.chargeline.util.TimeUtil;
import com.trustchain.chargeline.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.text.ParseException;
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
     * @param request
     * @return
     */
    @RequestMapping({"/setMacValue"})
    @ResponseBody
    public JsonResult addInfo(String code, String macvalue, HttpServletRequest request) {
        logger.info(requestUri(request));
        JsonResult jsonResult = new JsonResult();
        Scan scan = contractUtil.ScanLoad();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(new Date());
        String ipaddress = getIpAddr(request);
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
     * 获得扫码总数
     *
     * @return
     */
    @RequestMapping(value = {"/getCountScan"}, method = {RequestMethod.GET})
    @ResponseBody
    public JsonResult getCountScan() {
        JsonResult jsonResult = new JsonResult();
        Scan scan = contractUtil.ScanLoad();
        BigInteger total = null;
        try {
            total = scan.getInfoTotal().send();
            logger.info("成功获取信息总数：" + total);
            jsonResult.setState(JsonResult.SUCCESS);
            jsonResult.setMessage("成功获取信息总数：" + total);
            jsonResult.setData(total);
            return jsonResult;
        } catch (Exception e) {
            logger.error("获取信息总数失败");
            jsonResult.setState(JsonResult.ERROR);
            jsonResult.setMessage("获取充电线总户数失败");
            return jsonResult;
        }
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

    /**
     * 获得全部粉丝信息
     *
     * @return
     */
    @RequestMapping(value = {"/getAllFriendInfo"}, method = {RequestMethod.GET})
    @ResponseBody
    public JsonResult getAllFriendInfo() {
        JsonResult jsonResult = new JsonResult();


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


    /**
     * 获取请求IP
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    private String requestUri(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (request.getQueryString() != null) {
            requestUri += ("?" + request.getQueryString());
        }
        return "[" + request.getMethod() + "] " + requestUri;

    }

    private String requestBody(BufferedReader body) {
        String inputLine;
        String bodyStr = "";
        try {
            while ((inputLine = body.readLine()) != null) {
                bodyStr += inputLine;
            }
            body.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return "[body] " + bodyStr;
    }



    public static void main(String[] args) throws Exception {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间


        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.get(Calendar.YEAR);
        logger.info(String.valueOf(cal.get(Calendar.YEAR)));


    }
}
