package com.trustchain.chargeline.controller;

import com.trustchain.chargeline.domain.JsonResult;
import com.trustchain.chargeline.solidity.ChargingLine.ChargingLine;
import com.trustchain.chargeline.util.ContractUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
public class ShowDataController {
    protected static final Logger logger = LoggerFactory.getLogger(ShowDataController.class);
    @Autowired
    ContractUtil contractUtil;


    @GetMapping("/LineTotal")
    public JsonResult getLineTotal()  {
        JsonResult jsonResult =new JsonResult();
        ChargingLine chargingLine=contractUtil.ChargingLineLoad();
        try {
            BigInteger total=chargingLine.SharedLineNumber().send();
            jsonResult.setState(JsonResult.SUCCESS);
            jsonResult.setMessage("线的总量："+ total.toString());
            jsonResult.setData(total.intValue());
            logger.info("获取线的总量成功，线的总量："+total.toString());
            return jsonResult;

        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setState(JsonResult.ERROR);
            jsonResult.setMessage("获取线的总量失败");
            jsonResult.setData(null);
            logger.error("获取线的总量失败！！！！！！！！！");
            return jsonResult;
        }


    }
}
