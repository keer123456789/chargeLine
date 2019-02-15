package com.trustchain.chargeline.util;



import com.trustchain.chargeline.domain.JsonResult;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
@Component("httpPostUtil")
public class HttpPostUtil {
    private static String MEHTOD_POST = "POST";
    private static String MEHTOD_GET = "GET";
    private Logger logger = Logger.getLogger(getClass().getName());

    public JsonResult HttpsendPost(String url, JSONObject params, Map<String, String> headerParam) {
        JsonResult jsonResult = new JsonResult();
        try {
            HttpPost postMethod = null;
            HttpResponse response = null;
            HttpClient httpClient = HttpClients.createDefault();
            postMethod = new HttpPost(url);

            postMethod.addHeader("Content-type", "application/x-www-form-urlencoded");
            String apikey = (String) headerParam.get("apikey");
            if ((apikey.equals("")) || (apikey.length() == 0) || (apikey == "")) {
                postMethod.addHeader("hswebtime", (String) headerParam.get("hswebtime"));
                postMethod.addHeader("token", (String) headerParam.get("token"));
            } else {
                postMethod.addHeader("hswebtime", (String) headerParam.get("hswebtime"));
                postMethod.addHeader("token", (String) headerParam.get("token"));
                postMethod.addHeader("apikey", apikey);
            }

            org.apache.http.entity.StringEntity stringEntity = new org.apache.http.entity.StringEntity(params.toString(), "UTF-8");
            postMethod.setEntity(stringEntity);
            response = httpClient.execute(postMethod);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("HTTP Status Code:" + statusCode);
            if (statusCode != 200) {
                System.out.println("HTTP请求未成功！HTTP Status Code:" + response.getStatusLine());
                jsonResult.setState(1);
                jsonResult.setMessage("请求失败!");
                return jsonResult;
            }
            HttpEntity httpEntity = response.getEntity();
            String reponseContent = EntityUtils.toString(httpEntity);
            System.out.println("响应内容：" + reponseContent);
            JSONObject obj = new JSONObject(reponseContent);
            jsonResult.setData(obj);
            jsonResult.setState(0);
            jsonResult.setMessage("请求成功!");
            EntityUtils.consume((org.apache.http.HttpEntity) httpEntity);
            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult.setState(1);
            jsonResult.setMessage("请求失败!");
        }
        return jsonResult;
    }

    public String getHswebtime() {
        String time = String.valueOf(new Date().getTime());
        System.out.print(time);
        String guidString = getGUID();
        String hswebtime = time + "_" + "40be312bd485340bd880ca7";
        System.out.print("hswebtime" + hswebtime);
        return hswebtime;
    }

    public Map<String, String> getParams() throws java.io.UnsupportedEncodingException, java.security.NoSuchAlgorithmException {
        Map<String, String> map = new java.util.HashMap();
        String hswebtime = getHswebtime();
        map.put("hswebtime", hswebtime);
        String param = hswebtime + "nh0a54f38b9eff2844c08bmf01524RTRH14eg6557ty6reE2mljhuftg1d45dfacvv";
        String token = getMD5(param);
        map.put("token", token);
        map.put("apikey", "");
        return map;
    }

    public static String getGUID() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    public JsonResult doHttpsPostMap(String url, Map<String, Object> params, Map<String, String> headerParam) {
        JsonResult jsonResult = new JsonResult();
        InputStream input = null;
        String postResp = null;
        HttpResponse response = null;
        HttpClient httpClient = HttpClients.createDefault();
        BufferedReader reader = null;
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        String apikey = (String) headerParam.get("apikey");
        if ((apikey.equals("")) || (apikey.length() == 0) || (apikey == "")) {
            post.addHeader("hswebtime", (String) headerParam.get("hswebtime"));
            post.addHeader("token", (String) headerParam.get("token"));
        } else {
            post.addHeader("hswebtime", (String) headerParam.get("hswebtime"));
            post.addHeader("token", (String) headerParam.get("token"));
            post.addHeader("apikey", apikey);
        }
        try {
            List<NameValuePair> nvp = getNameValuePairArr(params);
            post.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(nvp, "UTF-8"));
            response = httpClient.execute(post);
            int responseCode = 0;
            responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                HttpEntity entity = response.getEntity();
                String reponseContent = EntityUtils.toString(entity);
                System.out.println("响应内容：" + reponseContent);
                JSONObject obj = new JSONObject(reponseContent);
                jsonResult.setData(obj);
                jsonResult.setState(0);
                jsonResult.setMessage("请求成功!");
                input = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                String tempBf = null;
                StringBuffer xmlStr = new StringBuffer();
                while ((tempBf = reader.readLine()) != null) {
                    xmlStr.append(tempBf);
                }
                postResp = xmlStr.toString();
                reader.close();
                if (entity != null)
                    EntityUtils.consume(entity);
            }
            if (responseCode != 200) {
                response = httpClient.execute(post);
                if (response.getStatusLine().getStatusCode() != 200) {
                    post.abort();
                    jsonResult.setData(null);
                    jsonResult.setState(1);
                    jsonResult.setMessage("请求失败!请求失败的状态为" + response.getStatusLine().getStatusCode());
                    this.logger.info("request---------get------status" + response.getStatusLine().getStatusCode());
                } else {
                    HttpEntity entity = response.getEntity();
                    String reponseContent = EntityUtils.toString(entity);
                    System.out.println("响应内容：" + reponseContent);
                    JSONObject obj = new JSONObject(reponseContent);
                    jsonResult.setData(obj);
                    jsonResult.setState(0);
                    jsonResult.setMessage("请求成功!");
                    input = entity.getContent();
                    reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                    String tempBf = null;
                    StringBuffer xmlStr = new StringBuffer();
                    while ((tempBf = reader.readLine()) != null) {
                        xmlStr.append(tempBf);
                    }
                    postResp = xmlStr.toString();
                    reader.close();
                    if (entity != null) {
                        EntityUtils.consume(entity);
                    }
                }
            }
            return jsonResult;
        } catch (Exception e) {
            this.logger.info("errror--------------" + e.getMessage());
            post.abort();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (input != null) {
                    input.close();
                }
                return null;
            } catch (IOException e) {
                this.logger.info("IOExceptionerrror--------------" + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }

    public String doHttpsPostMapSend(String url, Map<String, Object> params, Map<String, String> headerParam) {
        JsonResult jsonResult = new JsonResult();
        InputStream input = null;

        HttpResponse response = null;
        HttpEntity entity = null;
        HttpClient httpClient = HttpClients.createDefault();
        BufferedReader reader = null;
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        String apikey = (String) headerParam.get("apikey");
        if ((apikey.equals("")) || (apikey.length() == 0) || (apikey == "")) {
            post.addHeader("hswebtime", (String) headerParam.get("hswebtime"));
            post.addHeader("token", (String) headerParam.get("token"));
        } else {
            post.addHeader("hswebtime", (String) headerParam.get("hswebtime"));
            post.addHeader("token", (String) headerParam.get("token"));
            post.addHeader("apikey", apikey);
        }
        try {
            List<NameValuePair> nvp = getNameValuePairArr(params);
            post.setEntity(new org.apache.http.client.entity.UrlEncodedFormEntity(nvp, "UTF-8"));
            response = httpClient.execute(post);
            entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() != 200) {
                post.abort();
                return null;
            }
            input = entity.getContent();
            System.out.println(entity.getContent());
            reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String tempBf = null;
            StringBuffer xmlStr = new StringBuffer();
            while ((tempBf = reader.readLine()) != null) {
                xmlStr.append(tempBf);
            }
            return xmlStr.toString();
        } catch (Exception e) {
            post.abort();
            return null;
        } finally {
            try {
                input.close();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public List<NameValuePair> getNameValuePairArr(Map<String, Object> parasMap) {
        List<NameValuePair> nvps = new java.util.ArrayList();
        for (Map.Entry<String, Object> parasEntry : parasMap.entrySet()) {
            String parasName = (String) parasEntry.getKey();
            String parasValue = String.valueOf(parasEntry.getValue());
            nvps.add(new org.apache.http.message.BasicNameValuePair(parasName, parasValue));
        }
        return nvps;
    }

    public JsonResult readReqStr(HttpServletRequest request) {
        JsonResult jsonResult = new JsonResult();
        String responseMsg = null;
        this.logger.info("......................................");
        try {
            StringBuffer sb = new StringBuffer(2000);
            InputStream is = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            this.logger.info("reader...." + reader);
            Map map = request.getParameterMap();
            if (map.get("data") != null) {
                jsonResult.setState(0);
                this.logger.info("map.get(data)............" + map.get("data"));
                jsonResult.setData(map.get("data"));
            }
            reader.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
            jsonResult.setState(1);
        }
        this.logger.info("sb........responseMsg........" + jsonResult);
        return jsonResult;
    }

    public String readReqStrs(HttpServletRequest request) {
        String responseMsg = null;
        try {
            StringBuffer sb = new StringBuffer(2000);
            InputStream is = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            Map map = request.getParameterMap();
            this.logger.info("reader...." + reader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("{")) {
                    sb.append(line);
                }
            }
            this.logger.info("reader.readLine...." + reader.readLine());
            responseMsg = sb.toString();
            reader.close();
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        this.logger.info("sb........responseMsg........" + responseMsg);
        return responseMsg;
    }


    public String getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new java.util.HashMap();
        Enumeration headerNames = request.getHeaderNames();
        String value;
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            value = request.getHeader(key);
            map.put(key, value);
        }

        String result = "";
        for (String key : map.keySet()) {
            result = result + "key= " + key + " and value= " + (String) map.get(key) + "\n";
        }
        return result;
    }

    public String getMD5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(str.getBytes());


            return new java.math.BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String dateBeforeTime(Integer second) {
        this.logger.info("dateBeforeTime++++++++++++++++++=====================");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(12, -second.intValue());
        Date beforeD = beforeTime.getTime();
        String time = sdf.format(beforeD);
        this.logger.info("beforeTimetimetime@@@@@@@@@@@@@@@@@@@@@@@@" + time);
        return time;
    }


    public static String stampToDateStr(String s)
            throws java.text.ParseException {
        if ((s == null) || (s.length() <= 0)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Long time = new Long(s);
        String d = format.format(time);
        return d;
    }
}