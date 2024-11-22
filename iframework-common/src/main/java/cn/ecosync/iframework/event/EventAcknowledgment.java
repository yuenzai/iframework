package cn.ecosync.iframework.event;

import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author yuenzai
 * @since 2024
 */
@NoRepositoryBean
public interface EventAcknowledgment {
    void acknowledge(String eventId);
}
