package com.trustchain.chargeline.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@RestController
public class AllController {
    protected static final Logger logger = LoggerFactory.getLogger(AllController.class);

    @Value("${web3_url}")
    private static String web3_url;
    @Value("${ChargingLine_address}")
    private static String ChargingLine_address;

    @GetMapping("/**")
    public void getAll(HttpServletRequest request) {
        logger.info(requestUri(request));
    }

    @PostMapping("/**")
    public void postAll(HttpServletRequest request, BufferedReader body) {
        logger.info(requestUri(request) + requestBody(body));
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



}
