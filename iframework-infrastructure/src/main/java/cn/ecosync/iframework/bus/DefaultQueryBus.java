package cn.ecosync.iframework.bus;

import cn.ecosync.iframework.query.Query;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.query.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yuenzai
 * @since 2024
 */
public class DefaultQueryBus implements QueryBus {
    private static final Logger log = LoggerFactory.getLogger(DefaultQueryBus.class);

    private final Map<Class<?>, QueryHandler<?, ?>> queryHandlers;

    public DefaultQueryBus(List<QueryHandler<?, ?>> queryHandlers) {
        this.queryHandlers = queryHandlers.stream()
                .map(in -> new AbstractMap.SimpleImmutableEntry<>(getQueryType(in), in))
                .filter(in -> in.getKey() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Query<R>, R> R execute(T query) {
        Assert.notNull(query, "query is null");
        QueryHandler<?, ?> queryHandler = queryHandlers.get(query.getClass());
        Assert.notNull(queryHandler, "query handler not found: " + query.getClass().getCanonicalName());
        Method handleMethod = ReflectionUtils.findMethod(queryHandler.getClass(), "handle", query.getClass());
        Assert.notNull(handleMethod, "handle method not found: " + queryHandler.getClass().getCanonicalName());
        return (R) ReflectionUtils.invokeMethod(handleMethod, queryHandler, query);
    }

    private Class<?> getQueryType(QueryHandler<?, ?> queryHandler) {
        return ResolvableType.forClass(QueryHandler.class, queryHandler.getClass())
                .resolveGeneric(0);
    }

    public void logging() {
        String logContent = this.queryHandlers.entrySet().stream()
                .map(in -> in.getKey().getCanonicalName() + " - " + in.getValue().getClass().getCanonicalName())
                .collect(Collectors.joining("\n"));
        log.info("registered query handlers:\n{}", logContent);
    }
}
