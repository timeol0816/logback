package com.logback.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public abstract class  DefaultLogger {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLogger.class);

    protected static String getThreadId() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        return threadId;
    }

    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = "";
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    protected static Object[] params(Object[] args) {

        int index = 0;
        Object [] params = new Object[args.length + 1];
        try {
            params[index] = getThreadId();
            for (Object arg : args) {
                if(BeanUtils.isSimpleValueType(arg.getClass())) {
                    params[++index] = arg;
                }else {
                    params[++index] = toJson(arg);
                }
            }

        }catch (Exception ex) {
            logger.warn("activity 로깅 시 예외가 발생하여 예외로그를 남긴다. 단, 비즈니스 로직에는 영향을 주지 않는다.", ex);
        }
        return params;
    }

    public static void error(String pattern, Object... args) {}

    public static void warn(String pattern, Object... args) {}

    public static void info(String pattern, Object... args) {}

    public static void debug(String pattern, Object... args) {}

    public static void trace(String pattern, Object... args) {}
}