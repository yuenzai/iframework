package cn.ecosync.iframework.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * @author yuenzai
 * @since 2024
 */
public class SerializationException extends NestedRuntimeException {
    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
