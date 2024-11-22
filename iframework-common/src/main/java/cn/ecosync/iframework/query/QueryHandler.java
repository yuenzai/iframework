package cn.ecosync.iframework.query;

/**
 * @author yuenzai
 * @since 2024
 */
public interface QueryHandler<T extends Query<R>, R> {
    R handle(T query);
}
