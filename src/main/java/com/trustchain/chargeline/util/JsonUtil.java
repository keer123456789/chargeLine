package com.trustchain.chargeline.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import com.trustchain.chargeline.domain.JsonResult;
import com.trustchain.chargeline.solidity.Friend.Friend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.tuples.generated.Tuple5;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
@Component
public class JsonUtil {

    @Autowired
    ContractUtil contractUtil;
    protected static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static boolean writeFile(Object object) {
        JSONWriter writer = null;
        for (int i = 1; i <= 3; i++) {
            String path = "./JsonData/" + i + ".json";
            try {
                logger.info("开始写入文件…………");
                writer = new JSONWriter(new FileWriter(path));
                writer.startArray();

                writer.writeValue(object);
                writer.endArray();
                writer.close();
                Thread.sleep(2000);

            } catch (Exception e) {
                logger.info("写入文件错误");
                return false;
            }
        }
        logger.info("写入成功");
        return true;
    }

    /**
     * 读文件
     *
     * @param path
     * @return
     */
    public static JSONObject readFile(String path) {
        JSONReader reader = null;
        try {
            reader = new JSONReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            logger.info("系统中找不到指定文件，path：" + path);
            return null;
        }
        reader.startArray();
        JSONObject jsonObject = new JSONObject();
        while (reader.hasNext()) {
            jsonObject = reader.readObject(JSONObject.class);

        }
        reader.endArray();
        reader.close();
        return jsonObject;

    }



    public static void main(String[] args) throws ParseException {

    }
}
