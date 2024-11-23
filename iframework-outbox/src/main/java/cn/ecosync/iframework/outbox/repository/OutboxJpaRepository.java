package cn.ecosync.iframework.outbox.repository;

import cn.ecosync.iframework.outbox.domain.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yuenzai
 * @since 2024
 */
public interface OutboxJpaRepository extends JpaRepository<Outbox, Integer> {
}
