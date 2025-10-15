package com.xworkz.farmfresh.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "admin_info")
@NamedQuery(name = "getDetailsByEmail", query = "select a from AdminEntity a where a.email=:email")
@NamedQuery(name ="getAll",query = "select a from AdminEntity a")
public class AdminEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "admin_name")
    private String adminName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "confirm_password")
    private String confirmPassword;

    @Column(name = "profile_path")
    private String profilePath;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @OneToMany(mappedBy = "adminEntity", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AdminAuditEntity> auditLogs = new ArrayList<>();

    @OneToMany(mappedBy = "admin")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CollectMilkEntity> collectedMilkList;

}
