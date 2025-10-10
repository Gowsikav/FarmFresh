package com.xworkz.farmfresh.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@Table(name = "supplier_bank_details")
public class SupplierBankDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Integer bankId;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_branch")
    private String bankBranch;

    @Column(name ="account_number" )
    private String accountNumber;

    @Column(name = "IFSC_code")
    private String IFSCCode;

    @Column(name = "account_type")
    private String accountType;

    @OneToOne
    @JoinColumn(name = "supplier_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SupplierEntity supplierEntity;

    @OneToOne(mappedBy = "supplierBankDetailsEntity",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.ALL)
    private SupplierBankDetailsAuditEntity supplierBankDetailsAuditEntity;
}
