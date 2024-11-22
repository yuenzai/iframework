package cn.ecosync.iframework.serde;

/**
 * @author yuenzai
 * @since 2024
 */
public interface Serde<S> {
    S serialize(Object in);

    <T> T deserialize(S in, Class<T> outType);

    <T> T deserialize(S in, TypeReference<T> outTypeRef);

    <T> T convert(Object in, Class<T> outType);

    <T> T convert(Object in, TypeReference<T> outTypeRef);
}
