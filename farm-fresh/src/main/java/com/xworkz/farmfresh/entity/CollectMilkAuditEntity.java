package com.xworkz.farmfresh.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "collect_milk_audit_info")
public class CollectMilkAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collect_milk_audit_id")
    private Integer collectMilkAuditId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @OneToOne
    @JoinColumn(name = "collect_milk_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CollectMilkEntity collectMilkEntity;
}
