package com.cq.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/27 10:53
 * @Description:
 */
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 转换为json字符串
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Json转对象
     * @param string
     * @param classType
     * @return
     */
    public static Object fromJson(String string,Class classType) {
        try {
            return objectMapper.readValue(string,classType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
