package com.ttn.bootcamp.project.bootcampproject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@EqualsAndHashCode
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Audit<T> {
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @JsonIgnore
    protected Date dateCreated;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    protected Date lastUpdated;

    @CreatedBy
    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    protected User createdBy;

    @LastModifiedBy
    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    protected User modifiedBy;
}
