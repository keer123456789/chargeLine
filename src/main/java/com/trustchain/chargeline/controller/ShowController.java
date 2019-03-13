package com.trustchain.chargeline.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trustchain.chargeline.Service.ShowService;
import com.trustchain.chargeline.domain.JsonResult;
import com.trustchain.chargeline.solidity.Friend.Friend;
import com.trustchain.chargeline.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.*;

@RestController
public class ShowController {
    protected static final Logger logger = LoggerFactory.getLogger(ShowController.class);
    @Autowired
    ShowService showService;
    /**
     * 获得昨日信息
     * @return
     */
    @RequestMapping(value = {"/getCount"}, method = {RequestMethod.GET})
    @ResponseBody
    public JsonResult getYesterdayCount() throws ParseException {
        JsonResult jsonResult = new JsonResult();
        JSONArray friendArray = showService.getInfo("./JsonData/1.json","friendInfo");
        int friendTotalYest = 0;
        for (int i=0;i<friendArray.size();i++) {
            JSONObject jsonObject= (JSONObject) friendArray.get(i);
            if (TimeUtil.getCurrentTime(jsonObject.get("date").toString())) {
                friendTotalYest++;
            }
            logger.info(i+"")   ;
        }
        JSONArray scanArray = showService.getInfo("./JsonData/1.json","scanInfo");
        int scanTotalYest = 0;
        for (int i=0;i<scanArray.size();i++) {
            JSONObject jsonObject= (JSONObject) scanArray.get(i);
            if (TimeUtil.getCurrentTime(jsonObject.get("date").toString())) {
                scanTotalYest++;
            }
        }
        Map<String, String> data = new HashMap<>();
        data.put("scantotal", scanArray.size() + "");
        data.put("yesterdayScan", scanTotalYest + "");
        data.put("friendtotal", friendArray.size() + "");
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
    public JsonResult getCountFriend() throws ParseException {
        JsonResult jsonResult = new JsonResult();
        JSONArray friendArray = showService.getInfo("./JsonData/1.json","friendInfo");
        JSONArray scanArray = showService.getInfo("./JsonData/1.json","scanInfo");
        //获取信息的总数
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("FriendSize",friendArray.size());
        jsonObject.put("FriendInfo",friendArray);
        jsonObject.put("ScanSize",scanArray.size());
        jsonObject.put("ScanInfo",scanArray);

        jsonResult.setMessage("获取成功");
        jsonResult.setState(JsonResult.SUCCESS);
        jsonResult.setData(jsonObject);
        return jsonResult;
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
        JSONArray friendArray = showService.getInfo("./JsonData/2.json","friendInfo");
        int[] sumFriend = new int[7];
        for (int i=0;i<friendArray.size();i++) {
            Map jsonObject= (Map) friendArray.get(i);
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 1) {
                sumFriend[0]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 2) {
                sumFriend[1]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 3) {
                sumFriend[2]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 4) {
                sumFriend[3]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 5) {
                sumFriend[4]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 6) {
                sumFriend[5]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 7) {
                sumFriend[6]++;
            }
        }

        JSONArray scanArray = showService.getInfo("./JsonData/2.json","scanInfo");
        int[] sumscan = new int[7];
        for (int i=0;i<scanArray.size();i++) {
            Map jsonObject= (Map) scanArray.get(i);
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 1) {
                sumscan[0]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 2) {
                sumscan[1]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 3) {
                sumscan[2]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 4) {
                sumscan[3]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 5) {
                sumscan[4]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 6) {
                sumscan[5]++;
            }
            if (TimeUtil.getLastTimeInterval(jsonObject.get("date").toString()) == 7) {
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
     * 获得每月的信息
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
        JSONArray friendArray = showService.getInfo("./JsonData/3.json","friendInfo");
        for (int i=0;i<friendArray.size();i++) {
            JSONObject jsonObject= (JSONObject) friendArray.get(i);
            int month = TimeUtil.getMonth(jsonObject.get("date").toString());
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
        JSONArray scanArray = showService.getInfo("./JsonData/3.json","scanInfo");
        for (int i=0;i<scanArray.size();i++) {
            JSONObject jsonObject= (JSONObject) scanArray.get(i);
            int month = TimeUtil.getMonth(jsonObject.get("date").toString());
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



//    @GetMapping("/get")
//    @ResponseBody
//    public JsonResult get() throws ParseException {
//        JsonResult jsonResult=new JsonResult();
//        JSONArray jsonArray=showService.getAllFriendInfo("./JsonData/1.json");
//        jsonResult.setData(jsonArray);
//        jsonResult.setMessage("粉丝总数："+jsonArray.size());
//        return jsonResult;
//    }
}
