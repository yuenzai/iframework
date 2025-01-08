package cn.ecosync.iframework.autoconfigure;

import cn.ecosync.iframework.autoconfigure.serde.SerdeConfiguration;
import cn.ecosync.iframework.bus.DefaultCommandBus;
import cn.ecosync.iframework.bus.DefaultEventBus;
import cn.ecosync.iframework.bus.DefaultQueryBus;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.command.CommandHandler;
import cn.ecosync.iframework.event.EventBus;
import cn.ecosync.iframework.event.repository.EventRepository;
import cn.ecosync.iframework.query.QueryBus;
import cn.ecosync.iframework.query.QueryHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * @author yuenzai
 * @since 2024
 */
@AutoConfiguration
@Import(SerdeConfiguration.class)
public class IFrameworkAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CommandBus.class)
    public DefaultCommandBus defaultCommandBus(List<CommandHandler<?>> commandHandlers) {
        DefaultCommandBus defaultCommandBus = new DefaultCommandBus(commandHandlers);
        defaultCommandBus.logging();
        return defaultCommandBus;
    }

    @Bean
    @ConditionalOnMissingBean(QueryBus.class)
    public DefaultQueryBus defaultQueryBus(List<QueryHandler<?, ?>> queryHandlers) {
        DefaultQueryBus defaultQueryBus = new DefaultQueryBus(queryHandlers);
        defaultQueryBus.logging();
        return defaultQueryBus;
    }

    @Bean
    @ConditionalOnMissingBean(EventBus.class)
    public DefaultEventBus defaultEventBus(ObjectProvider<EventRepository> eventRepositoryProvider) {
        return new DefaultEventBus(eventRepositoryProvider.getIfAvailable());
    }
}
