/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.akila.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author minh
 */
public class JsonUtil {

    private static final JsonMapper jsonMapper = new JsonMapper();
    private static final Gson gson = new Gson();

    static {
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static JsonMapper getJsonMapper() {
        return jsonMapper;
    }

    public static Gson getGson() {
        return gson;
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        var jsonMapper = JsonUtil.getJsonMapper();
        return jsonMapper.writeValueAsString(obj);
    }

    public static <T> List<T> parseJson(String json) throws JsonProcessingException {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        var jsonMapper = JsonUtil.getJsonMapper();
        TypeReference<List<T>> type = new TypeReference<>() {
        };
        return jsonMapper.readValue(json, type);
    }
}
