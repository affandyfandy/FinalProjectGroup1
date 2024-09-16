package com.final_project_clinic.user.data.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Audit {

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 255)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_time", nullable = false)
    private Date createdTime;

    @LastModifiedBy
    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_time")
    private Date updatedTime;
}
