package cn.ecosync.iframework.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author yuenzai
 * @since 2024
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Entity extends IdentifiedDomainObject {
    @CreatedDate
    private Long createdDate;

    @LastModifiedDate
    private Long lastModifiedDate;

    public Long getCreatedDate() {
        return createdDate;
    }

    public Long getLastModifiedDate() {
        return lastModifiedDate;
    }
}
