package com.trustchain.chargeline.util;

import com.alibaba.fastjson.*;
import com.trustchain.chargeline.domain.JsonResult;
import com.trustchain.chargeline.solidity.Friend.Friend;
import com.trustchain.chargeline.solidity.Scan.Scan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.tuples.generated.Tuple5;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


@Component
public class Autobuild {
    protected static final Logger logger = LoggerFactory.getLogger(Autobuild.class);
    @Autowired
    ContractUtil contractUtil;

    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduled() {
        Scan scan = contractUtil.ScanLoad();
        BigInteger total = null;
        try {
            total = scan.getInfoTotal().send();
            logger.info("成功获取信息总数：" + total);
        } catch (Exception e) {
            logger.error("获取信息总数失败");
            e.printStackTrace();
        }
        JSONArray scanInfo = new JSONArray();
        for (int i = 1; i <= total.intValue(); i++) {
            Tuple5<Boolean, String, String, String, String> info = null;
            try {
                info = scan.getInfoByID(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("获取id为" + i + "的信息失败");

            }
            if (info.getValue1()) {
                JSONObject josnObject = new JSONObject();
                josnObject.put("ipaddress", info.getValue3());
                josnObject.put("macvalue", info.getValue4());
                josnObject.put("date", info.getValue5());
                scanInfo.add(josnObject);
            } else {
                logger.warn("没有id为" + i + "信息");
            }
        }




        Friend friend = contractUtil.FriendLoad();
        try {
            total = friend.getFriendTotal().send();
            logger.info("成功粉丝总数：" + total);
        } catch (Exception e) {
            logger.error("获取粉丝总数失败");
            e.printStackTrace();
        }
        JSONArray friendInfo=new JSONArray();
        for (int i = 1; i <= total.intValue(); i++) {
            Tuple5<Boolean, String, String, String, String> info = null;
            try {
                info = friend.getFriendById(BigInteger.valueOf(i)).send();
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("获取id为" + i + "的信息失败");

            }
            if (info.getValue1()) {
                JSONObject josnObject = new JSONObject();
                josnObject.put("openId", info.getValue3());
                josnObject.put("username", info.getValue4());
                josnObject.put("date", info.getValue5());
                friendInfo.add(josnObject);
            } else {
                logger.warn("没有id为" + i + "信息");
            }
        }

        Map<String,Object> map=new HashMap();
        map.put("scanInfo",scanInfo);
        map.put("friendInfo",friendInfo);

        JsonUtil.writeFile(map);

    }


    public static void main(String[] args) {

    }
}
