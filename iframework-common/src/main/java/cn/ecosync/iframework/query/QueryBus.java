package cn.ecosync.iframework.query;

/**
 * @author yuenzai
 * @since 2024
 */
public interface QueryBus {
    <T extends Query<R>, R> R execute(T query);
}
