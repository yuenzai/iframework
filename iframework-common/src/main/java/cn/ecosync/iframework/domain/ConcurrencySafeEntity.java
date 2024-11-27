package cn.ecosync.iframework.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * @author yuenzai
 * @since 2024
 */
@MappedSuperclass
public abstract class ConcurrencySafeEntity extends Entity {
    /**
     * 乐观锁版本
     */
    @Version
    private Integer version;
}
