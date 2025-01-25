package org.httpsrv.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public final class Jackson {
    private static final ObjectMapper jacksonObjectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;

    public static String toJsonString(Object obj) throws JsonProcessingException {
        return jacksonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    public static <T> T fromJsonString(String jsonString, Class<T> objectClass) throws JsonProcessingException {
        return jacksonObjectMapper.readValue(jsonString, objectClass);
    }

    public static <T> T fromJsonString(File jsonString, Class<T> objectClass) throws IOException {
        return jacksonObjectMapper.readValue(jsonString, objectClass);
    }
}