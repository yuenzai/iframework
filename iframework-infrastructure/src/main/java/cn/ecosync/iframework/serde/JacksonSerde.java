package cn.ecosync.iframework.serde;

import cn.ecosync.iframework.exception.SerializationException;
import cn.ecosync.iframework.util.StringUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yuenzai
 * @since 2024
 */
public class JacksonSerde implements JsonSerde {
    private final ObjectMapper objectMapper;

    public JacksonSerde(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(Object in) {
        try {
            return objectMapper.writeValueAsString(in);
        } catch (Exception e) {
            throw new SerializationException("", e);
        }
    }

    @Override
    public <T> T deserialize(String in, Class<T> outType) {
        try {
            if (!StringUtils.hasText(in)) {
                return null;
            }
            return objectMapper.readValue(in, outType);
        } catch (Exception e) {
            throw new SerializationException("", e);
        }
    }

    @Override
    public <T> T deserialize(String in, TypeReference<T> outTypeRef) {
        try {
            if (!StringUtils.hasText(in)) {
                return null;
            }
            JavaType valueType = objectMapper.getTypeFactory().constructType(outTypeRef.getType());
            return objectMapper.readValue(in, valueType);
        } catch (Exception e) {
            throw new SerializationException("", e);
        }
    }

    @Override
    public <T> T convert(Object in, Class<T> outType) {
        try {
            return objectMapper.convertValue(in, outType);
        } catch (Exception e) {
            throw new SerializationException("", e);
        }
    }

    @Override
    public <T> T convert(Object in, TypeReference<T> outTypeRef) {
        try {
            JavaType valueType = objectMapper.getTypeFactory().constructType(outTypeRef.getType());
            return objectMapper.convertValue(in, valueType);
        } catch (Exception e) {
            throw new SerializationException("", e);
        }
    }
}
