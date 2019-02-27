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


    @RequestMapping(value = {"/get"}, method = {RequestMethod.GET})
    @ResponseBody
    public JsonResult get() {
        JsonResult jsonResult = new JsonResult();
        Map<String, String> data = new HashMap<>();
        data.put("scantotal", "289");
        data.put("yesterdayScan", "20");
        data.put("friendtotal", "20");
        data.put("yesterdayFriend", "1023");

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
     * 获得昨日信息
     * @return
     */
    @RequestMapping(value = {"/getCount"}, method = {RequestMethod.GET})
    @ResponseBody
    public JsonResult getYesterdayCount() throws ParseException {
        JsonResult jsonResult = new JsonResult();
        List<Map> friedData = (List<Map>) getAllFriendInfo().getData();
        int friendTotalYest = 0;
        for (Map map : friedData) {
            if (getCurrentTime(map.get("date").toString())) {
                friendTotalYest++;
            }
        }
        List<Map> scanData = (List<Map>) getInfo().getData();
        int scanTotalYest = 0;
        for (Map map : friedData) {
            if (getCurrentTime(map.get("date").toString())) {
                scanTotalYest++;
            }
        }
        Map<String, String> data = new HashMap<>();
        data.put("scantotal", scanData.size() + "");
        data.put("yesterdayScan", scanTotalYest + "");
        data.put("friendtotal", friedData.size() + "");
        data.put("yesterdayFriend", friendTotalYest + "");

        jsonResult.setState(JsonResult.SUCCESS);
        jsonResult.setData(data);
        return jsonResult;
    }

    /**
     * 获得粉丝总数
     *
     * @return
     */
    @RequestMapping(value = {"/getCountFriend"}, method = {RequestMethod.GET})
    @ResponseBody
    public JsonResult getCountFriend() {
        JsonResult jsonResult = new JsonResult();
        Friend friend = contractUtil.FriendLoad();
        //获取信息的总数
        BigInteger total = null;
        try {
            total = friend.getFriendTotal().send();
            logger.info("粉丝总数：" + total);
            jsonResult.setData(total);
            jsonResult.setMessage("粉丝总数：" + total);
            jsonResult.setState(JsonResult.SUCCESS);
            return jsonResult;
        } catch (Exception e) {
            logger.error("获取粉丝总数失败");
            jsonResult.setState(JsonResult.ERROR);
            jsonResult.setMessage("获取粉丝总数失败");
            return jsonResult;
        }
    }

    /**
     * 获得上周的信息
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"/getLastWeekInfo"}, method = {RequestMethod.GET})
    @ResponseBody
    public JsonResult getLastWeekInfo() throws ParseException {
        JsonResult jsonResult = new JsonResult();
        List<Map> friedData = (List<Map>) getAllFriendInfo().getData();
        int[] sumFriend = new int[7];
        for (Map map : friedData) {
            if (getLastTimeInterval(map.get("date").toString()) == 1) {
                sumFriend[0]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 2) {
                sumFriend[1]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 3) {
                sumFriend[2]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 4) {
                sumFriend[3]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 5) {
                sumFriend[4]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 6) {
                sumFriend[5]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 7) {
                sumFriend[6]++;
            }
        }

        List<Map> scanData = (List<Map>) getInfo().getData();
        int[] sumscan = new int[7];
        for (Map map : scanData) {
            if (getLastTimeInterval(map.get("date").toString()) == 1) {
                sumscan[0]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 2) {
                sumscan[1]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 3) {
                sumscan[2]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 4) {
                sumscan[3]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 5) {
                sumscan[4]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 6) {
                sumscan[5]++;
            }
            if (getLastTimeInterval(map.get("date").toString()) == 7) {
                sumscan[6]++;
            }
        }

        Map<String, Object> friend = new HashMap<>();
        Map<String, Object> scan = new HashMap<>();

        String[] week = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        friend.put("name", "Friend");
        scan.put("name", "scan");

        for (int i = 0; i < 7; i++) {
            friend.put(week[i], sumFriend[i]);
            scan.put(week[i], sumscan[i]);
        }
        List<Map> maps = new ArrayList<>();
        maps.add(friend);
        maps.add(scan);

        jsonResult.setState(JsonResult.SUCCESS);
        jsonResult.setData(maps);

        return jsonResult;

    }

    /**
     * 获得上周的信息
     *
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = {"/getMonthInfo"}, method = {RequestMethod.GET})
    @ResponseBody
    public JsonResult getMonthInfo() throws ParseException {
        JsonResult jsonResult = new JsonResult();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int nowMonth = c.get(Calendar.MONTH) + 1;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        int[] friend = new int[12];
        List<Map> friedData = (List<Map>) getAllFriendInfo().getData();
        for (Map map : friedData) {
            int month = getMonth(map.get("date").toString());
            switch (month) {
                case 1:
                    friend[0]++;
                    break;
                case 2:
                    friend[1]++;
                    break;
                case 3:
                    friend[2]++;
                    break;
                case 4:
                    friend[3]++;
                    break;
                case 5:
                    friend[4]++;
                    break;
                case 6:
                    friend[5]++;
                    break;
                case 7:
                    friend[6]++;
                    break;
                case 8:
                    friend[7]++;
                    break;
                case 9:
                    friend[8]++;
                    break;
                case 10:
                    friend[9]++;
                    break;
                case 11:
                    friend[10]++;
                    break;
                case 12:
                    friend[11]++;
                    break;
            }
        }

        int[] scan = new int[12];
        List<Map> scanData = (List<Map>) getInfo().getData();
        for (Map map : scanData) {
            int month = getMonth(map.get("date").toString());
            switch (month) {
                case 1:
                    scan[0]++;
                    break;
                case 2:
                    scan[1]++;
                    break;
                case 3:
                    scan[2]++;
                    break;
                case 4:
                    scan[3]++;
                    break;
                case 5:
                    scan[4]++;
                    break;
                case 6:
                    scan[5]++;
                    break;
                case 7:
                    scan[6]++;
                    break;
                case 8:
                    scan[7]++;
                    break;
                case 9:
                    scan[8]++;
                    break;
                case 10:
                    scan[9]++;
                    break;
                case 11:
                    scan[10]++;
                    break;
                case 12:
                    scan[11]++;
                    break;
            }
        }

        List<Map> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            Map<String, Object> map = new HashMap<>();
            if (i < nowMonth) {
                map.put("month", months[i]);
                map.put("Friend", friend[i]);
                map.put("Scan", scan[i]);
                list.add(map);
            }
        }
        jsonResult.setData(list);
        jsonResult.setState(JsonResult.SUCCESS);
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

    /**
     * 判断时间是否为昨天，当前时间算
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean getCurrentTime(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.set(Calendar.HOUR, beforeTime.get(Calendar.HOUR) - 24);
        ;// 24小时之前的时间
        Date beforeD = beforeTime.getTime();
        Date date1 = sdf.parse(date);
        if (beforeD.before(date1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回周几
     *
     * @return
     */
    public static int getLastTimeInterval(String date) throws ParseException {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
        int offset1 = 1 - dayOfWeek;
        int offset2 = 7 - dayOfWeek;
        calendar1.add(Calendar.DATE, offset1 - 7);
        calendar2.add(Calendar.DATE, offset2 - 7);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // System.out.println(sdf.format(calendar1.getTime()));// last Monday
        Date lastBeginDate = calendar1.getTime();
        // System.out.println(sdf.format(calendar2.getTime()));// last Sunday
        Date lastEndDate = calendar2.getTime();
        Date date1 = sdf.parse(date);

        int dayForWeek = 0;
        if (lastBeginDate.before(date1) && lastEndDate.after(date1)) {
            Calendar c = Calendar.getInstance();
            c.setTime(date1);

            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
            return dayForWeek;
        } else {
            return 0;
        }
    }

    /**
     * 得到月份
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得年份
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getYear(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.YEAR);
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
