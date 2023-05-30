package com.damda.back.domain;


import com.damda.back.config.converter.TimeConverter;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
    @Convert(converter = TimeConverter.class) private String createdAt;
    @Convert(converter = TimeConverter.class) private String updatedAt;
}
