package cn.ecosync.iframework.command;

/**
 * @author yuenzai
 * @since 2024
 */
public interface CommandHandler<T extends Command> {
    void handle(T command);
}
