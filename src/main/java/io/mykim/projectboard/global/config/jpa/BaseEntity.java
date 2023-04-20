package io.mykim.projectboard.global.config.jpa;

import io.mykim.projectboard.user.entity.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Table(indexes = {
        @Index(columnList = "created_at"),
        @Index(columnList = "last_modified_at")
})
public class BaseEntity extends BaseTimeEntity {
    @CreatedBy
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "created_by_user_id", nullable = false, updatable = false)
    private User createdBy;

    @LastModifiedBy
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "last_modified_by_user_id", nullable = false, updatable = false)
    private User lastModifiedBy;
}
