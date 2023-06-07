package com.damda.back.domain;


import com.damda.back.config.converter.TimeConverter;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {


     @CreationTimestamp
     @Column(nullable = false, updatable = false)
     private Timestamp createdAt;

     @UpdateTimestamp
     @Column(nullable = false)
     private Timestamp updatedAt;


}
