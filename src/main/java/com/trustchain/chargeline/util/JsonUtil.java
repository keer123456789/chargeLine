package com.trustchain.chargeline.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class JsonUtil {
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
        return true;
    }

    public static JSONArray readFile(String path) {
        JSONReader reader = null;
        try {
            reader = new JSONReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            logger.info("系统中找不到指定文件，path："+path);
            return null;
        }
        reader.startArray();
        JSONArray jsonArray = new JSONArray();
        while (reader.hasNext()) {
            jsonArray = reader.readObject(JSONArray.class);

        }
        reader.endArray();
        reader.close();
        return jsonArray;

    }

}
