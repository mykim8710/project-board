package io.mykim.projectboard.global.config.jpa;

import lombok.Getter;
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
public class BaseTimeEntity {
    @CreatedDate
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;
    @CreatedBy
    @Column(nullable = false, length = 100, name = "created_by")
    private String createdBy;
    @LastModifiedDate
    @Column(nullable = false,  name = "last_modified_at")
    private LocalDateTime lastModifiedAt;
    @LastModifiedBy
    @Column(nullable = false, length = 100, name = "last_modified_by")
    private String lastModifiedBy;

}
