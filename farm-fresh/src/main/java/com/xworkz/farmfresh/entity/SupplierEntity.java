package com.xworkz.farmfresh.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Data
@Entity
@Table(name = "supplier_details")
@NamedQuery(name = "getAllSuppliers",query = "select a from SupplierEntity a where a.isActive=true")
@NamedQuery(name = "checkEmail",query = "select a from SupplierEntity a where a.email=:email and a.isActive=true")
@NamedQuery(name = "checkPhoneNumber",query = "select a from SupplierEntity a where a.phoneNumber=:phoneNumber and a.isActive=true")
@NamedQuery(name="getSuppliersCount",query = "select count(a) from SupplierEntity a where a.isActive=true")
public class SupplierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name ="email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "type_of_milk")
    private String typeOfMilk;

    @OneToOne(mappedBy = "supplierEntity",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval=true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SupplierAuditEntity supplierAuditEntity;

    @Column(name = "is_active")
    private Boolean isActive;
}
