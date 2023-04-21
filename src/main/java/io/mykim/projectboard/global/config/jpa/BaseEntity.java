package io.mykim.projectboard.global.config.jpa;

import io.mykim.projectboard.user.entity.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {
    @CreatedBy
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "created_by_user_id", nullable = false, updatable = false)
    private User createdBy;

    @LastModifiedBy
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "last_modified_by_user_id", nullable = false)
    private User lastModifiedBy;
}
