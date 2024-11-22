package cn.ecosync.iframework.util;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.*;

/**
 * @author yuenzai
 * @since 2024
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {
    public static <T> Collection<T> nullSafeOf(Collection<T> collection) {
        return isEmpty(collection) ? Collections.emptyList() : collection;
    }

    public static <T> List<T> nullSafeOf(List<T> list) {
        return isEmpty(list) ? Collections.emptyList() : list;
    }

    public static <T> Set<T> nullSafeOf(Set<T> set) {
        return isEmpty(set) ? Collections.emptySet() : set;
    }

    public static <K, V> Map<K, V> nullSafeOf(@Nullable Map<K, V> map) {
        return isEmpty(map) ? Collections.emptyMap() : map;
    }

    public static boolean notEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean notEmpty(@Nullable Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static Object oneOrMore(List<?> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.size() > 1 ? list : firstElement(list);
    }

    public static Pageable of(Integer page, Integer pageSize) {
        Pageable pageable;
        if (pageSize != null && page != null) {
            pageable = Pageable.ofSize(pageSize)
                    .withPage(page - 1);
        } else {
            pageable = Pageable.unpaged();
        }
        return pageable;
    }
}