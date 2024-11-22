package cn.ecosync.iframework.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yuenzai
 * @since 2024
 */
@MappedSuperclass
public abstract class IdentifiedDomainObject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    protected Integer id;
}
