package cn.ecosync.iframework.query;

import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author yuenzai
 * @since 2024
 */
public interface QueryHandler<T extends Query<R>, R> {
    R handle(T query);

    static <T, U> Iterable<U> mapIterable(Iterable<T> iterable, Function<T, U> mapper) {
        if (iterable instanceof Page) {
            Page<T> page = (Page<T>) iterable;
            return page.map(mapper);
        } else if (iterable instanceof Collection) {
            Collection<T> collection = (Collection<T>) iterable;
            return collection.stream()
                    .map(mapper)
                    .collect(Collectors.toList());
        } else {
            return StreamSupport.stream(iterable.spliterator(), false)
                    .map(mapper)
                    .collect(Collectors.toList());
        }
    }
}
