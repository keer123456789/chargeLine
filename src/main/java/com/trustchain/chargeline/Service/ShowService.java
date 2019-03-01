package com.trustchain.chargeline.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trustchain.chargeline.domain.JsonResult;
import com.trustchain.chargeline.solidity.Friend.Friend;
import com.trustchain.chargeline.solidity.Scan.Scan;
import com.trustchain.chargeline.util.ContractUtil;
import com.trustchain.chargeline.util.JsonUtil;
import com.trustchain.chargeline.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.tuples.generated.Tuple5;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
@Service
public class ShowService {

    protected static final Logger logger = LoggerFactory.getLogger(ShowService.class);


    @Autowired
    ContractUtil contractUtil;

    public JSONArray getInfo(String path,String type) throws ParseException {
        JSONObject jsonObject = JsonUtil.readFile(path);
        JSONArray jsonArray = (JSONArray) jsonObject.get(type);
        return getAllFriendInfo(jsonArray);
    }

    public JSONArray getAllFriendInfo(JSONArray jsonArray) throws ParseException {
        Friend friend = contractUtil.FriendLoad();
        BigInteger total = null;
        try {
            total = friend.getFriendTotal().send();
            logger.info("成功粉丝总数：" + total);
        } catch (Exception e) {
            logger.error("获取粉丝总数失败");
            e.printStackTrace();
        }


        Map map= (Map) jsonArray.get(jsonArray.size()-1);
        String lastData=map.get("date").toString();

        for (int i = total.intValue(); i >= 1; i--) {
            Tuple5<Boolean, String, String, String, String> info = null;
            try {
                info = friend.getFriendById(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("获取id为" + i + "的粉丝信息失败");
            }
            if (info.getValue1() && TimeUtil.compaireTime(lastData,info.getValue5())) {
                Map<String, String> map1 = new HashMap();
                map1.put("openId", info.getValue3());
                map1.put("username", info.getValue4());
                map1.put("date", info.getValue5());
                jsonArray.add(map1);

            } else {

                return jsonArray;
            }
        }
        return jsonArray;
    }

    public JSONArray getAllScanInfo(JSONArray jsonArray) throws ParseException {
        Scan scan = contractUtil.ScanLoad();
        BigInteger total = null;
        try {
            total = scan.getInfoTotal().send();
            logger.info("成功扫码总数：" + total);
        } catch (Exception e) {
            logger.error("获取粉丝总数失败");
            e.printStackTrace();
        }

        Map map= (Map) jsonArray.get(jsonArray.size()-1);
        String lastData=map.get("date").toString();

        for (int i = total.intValue(); i >= 1; i--) {
            Tuple5<Boolean, String, String, String, String> info = null;
            try {
                info = scan.getInfoByID(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("获取id为" + i + "的粉丝信息失败");
            }
            if (info.getValue1() && TimeUtil.compaireTime(lastData,info.getValue5())) {
                Map<String, String> map1 = new HashMap();
                map1.put("ipaddress", info.getValue3());
                map1.put("macvalue", info.getValue4());
                map1.put("date", info.getValue5());
                jsonArray.add(map1);
            } else {
                return jsonArray;
            }
        }
        return jsonArray;

    }
}
