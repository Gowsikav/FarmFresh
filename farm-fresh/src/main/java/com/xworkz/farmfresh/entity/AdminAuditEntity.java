package com.xworkz.farmfresh.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "admin_audit")
public class AdminAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Integer auditId;

    @Column(name = "audit_name")
    private String auditName;

    @OneToOne
    @JoinColumn(name = "admin_id",unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AdminEntity adminEntity;

    @Column(name = "login_time")
    private LocalDateTime loginTime;

}
