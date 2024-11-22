package cn.ecosync.iframework.bus;

import cn.ecosync.iframework.command.Command;
import cn.ecosync.iframework.command.CommandBus;
import cn.ecosync.iframework.command.CommandHandler;
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
public class DefaultCommandBus implements CommandBus {
    private static final Logger log = LoggerFactory.getLogger(DefaultCommandBus.class);

    private final Map<Class<?>, CommandHandler<?>> commandHandlers;

    public DefaultCommandBus(List<CommandHandler<?>> commandHandlers) {
        this.commandHandlers = commandHandlers.stream()
                .map(in -> new AbstractMap.SimpleImmutableEntry<>(getCommandType(in), in))
                .filter(in -> in.getKey() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void execute(Command command) {
        Assert.notNull(command, "command is null");
        CommandHandler<?> commandHandler = commandHandlers.get(command.getClass());
        Assert.notNull(commandHandler, "command handler not found: " + command.getClass().getCanonicalName());
        Method handleMethod = ReflectionUtils.findMethod(commandHandler.getClass(), "handle", command.getClass());
        Assert.notNull(handleMethod, "handle method not found: " + commandHandler.getClass().getCanonicalName());
        command.validate();
        ReflectionUtils.invokeMethod(handleMethod, commandHandler, command);
    }

    private Class<?> getCommandType(CommandHandler<?> commandHandler) {
        return ResolvableType.forClass(CommandHandler.class, commandHandler.getClass())
                .resolveGeneric(0);
    }

    public void logging() {
        String logContent = this.commandHandlers.entrySet().stream()
                .map(in -> in.getKey().getCanonicalName() + " - " + in.getValue().getClass().getCanonicalName())
                .collect(Collectors.joining("\n"));
        log.info("registered command handlers:\n{}", logContent);
    }
}
