package com.xworkz.farmfresh.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "collect_milk_details")
@NamedQuery(name = "getAllDetailsByDate",query = "select a from CollectMilkEntity a " +
        "JOIN FETCH a.supplier where a.collectedDate between :fromDate and :toDate order by a.collectMilkId DESC")
@NamedQuery(name = "getAllDetailsByEmail",query = "select a from CollectMilkEntity a JOIN FETCH a.supplier where a.supplier.email=:email " +
        "and a.supplier.isActive=true order by a.collectedDate desc")
@NamedQuery(name="getMilkDetailsCountByEmail",query = "select count(a) from CollectMilkEntity a JOIN a.supplier where a.supplier.email=:email and a.supplier.isActive=true")
public class CollectMilkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collect_milk_id")
    private Integer collectMilkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id",nullable = false)
    private SupplierEntity supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id",nullable = false)
    private AdminEntity admin;

    @Column(name = "type_of_milk")
    private String typeOfMilk;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "collected_date")
    private LocalDate collectedDate;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "collectMilkEntity")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CollectMilkAuditEntity collectMilkAuditEntity;

}
