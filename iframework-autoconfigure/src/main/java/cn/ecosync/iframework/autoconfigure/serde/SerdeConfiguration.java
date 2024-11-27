package cn.ecosync.iframework.autoconfigure.serde;

import cn.ecosync.iframework.serde.JacksonSerde;
import cn.ecosync.iframework.serde.JsonSerde;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuenzai
 * @since 2024
 */
@Configuration(proxyBeanMethods = false)
public class SerdeConfiguration {
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ObjectMapper.class, JacksonSerde.class})
    public static class JsonSerdeConfiguration {
        @Bean
        @ConditionalOnMissingBean(JsonSerde.class)
        public JacksonSerde jsonSerde(ObjectMapper objectMapper) {
            return new JacksonSerde(objectMapper);
        }
    }
}
