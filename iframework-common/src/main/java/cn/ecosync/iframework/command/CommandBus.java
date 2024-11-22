package cn.ecosync.iframework.command;

/**
 * @author yuenzai
 * @since 2024
 */
public interface CommandBus {
    void execute(Command command);
}
