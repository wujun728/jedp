/*
 * Copyright 2020-2021 redragon.dongbin
 *
 * This file is part of redragon-erp/赤龙ERP.

 * redragon-erp/赤龙ERP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.

 * redragon-erp/赤龙ERP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with redragon-erp/赤龙ERP.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.erp.finance.ap.invoice.dao.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="ap_invoice_head", schema="erp")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ApInvoiceHead implements java.io.Serializable {

    //serialVersionUID
    private static final long serialVersionUID = 1L;

    //Constructors
    public ApInvoiceHead() {
    }
    
    //Fields
    
    //发票头id
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "invoice_head_id", unique = true, nullable = false)
    private Integer invoiceHeadId;
    
    public Integer getInvoiceHeadId() {
        return invoiceHeadId;
    }
    public void setInvoiceHeadId(Integer invoiceHeadId) {
        this.invoiceHeadId = invoiceHeadId;
    }

    //发票头编码
    @NotBlank(message="发票头编码不能为空")
    @Column(name = "invoice_head_code", unique = true, nullable = false, length = 45)
    private String invoiceHeadCode;
    
    public String getInvoiceHeadCode() {
        return invoiceHeadCode;
    }
    public void setInvoiceHeadCode(String invoiceHeadCode) {
        this.invoiceHeadCode = invoiceHeadCode;
    }
    
    //应付类型
    @Column(name = "ap_type", unique = false, nullable = false, length = 45)
    private String apType;
    
    public String getApType() {
        return apType;
    }
    public void setApType(String apType) {
        this.apType = apType;
    }
    
    //发票类型
    @Column(name = "invoice_type", unique = false, nullable = false, length = 45)
    private String invoiceType;
    
    public String getInvoiceType() {
        return invoiceType;
    }
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    //发票来源类型（采购订单PO、入库单INPUT）
    @NotBlank(message="发票来源类型不能为空")
    @Column(name = "invoice_source_type", unique = false, nullable = false, length = 45)
    private String invoiceSourceType;
    
    public String getInvoiceSourceType() {
        return invoiceSourceType;
    }
    public void setInvoiceSourceType(String invoiceSourceType) {
        this.invoiceSourceType = invoiceSourceType;
    }

    //发票来源头编码（采购订单头编码、入库单头编码）
    @NotBlank(message="发票来源头编码不能为空")
    @Column(name = "invoice_source_head_code", unique = false, nullable = false, length = 45)
    private String invoiceSourceHeadCode;
    
    public String getInvoiceSourceHeadCode() {
        return invoiceSourceHeadCode;
    }
    public void setInvoiceSourceHeadCode(String invoiceSourceHeadCode) {
        this.invoiceSourceHeadCode = invoiceSourceHeadCode;
    }

    //付款方
    @Column(name = "payer", unique = false, nullable = false, length = 45)
    @NotBlank(message="付款方不能为空")
    private String payer;
    
    public String getPayer() {
        return this.payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    
    //收款方
    @NotBlank(message="收款方不能为空")
    @Column(name = "receiver", unique = false, nullable = false, length = 45)
    private String receiver;
    
    public String getReceiver() {
        return this.receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    //发票金额
    @NotNull(message="发票金额不能为空")
    @Column(name = "amount", unique = false, nullable = false)
    private Double amount;
    
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    //币种
    @NotBlank(message="币种不能为空")
    @Column(name = "currency_code", unique = false, nullable = false, length = 45)
    private String currencyCode;
    
    public String getCurrencyCode() {
        return this.currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    //发票参考号（纸质发票号）
    @Column(name = "reference_number", unique = false, nullable = true, length = 45)
    private String referenceNumber;
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    //发票时间
    @NotNull(message="发票时间不能为空")
    @Column(name = "invoice_date", unique = false, nullable = false)
    private Date invoiceDate;
    
    public Date getInvoiceDate() {
        return invoiceDate;
    }
    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    //付款方式
    @NotBlank(message="付款方式不能为空")
    @Column(name = "pay_mode", unique = false, nullable = false, length = 45)
    private String payMode;
    
    public String getPayMode() {
        return this.payMode;
    }
    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }
    
    //银行编码
    @Column(name = "bank_code", unique = false, nullable = true, length = 45)
    private String bankCode;
    
    public String getBankCode() {
        return this.bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    
    //分行编码
    @Column(name = "sub_bank_code", unique = false, nullable = true, length = 45)
    private String subBankCode;
    
    public String getSubBankCode() {
        return this.subBankCode;
    }
    public void setSubBankCode(String subBankCode) {
        this.subBankCode = subBankCode;
    }
    
    //银行账户
    @Column(name = "bank_account", unique = false, nullable = true, length = 45)
    private String bankAccount;
    
    public String getBankAccount() {
        return this.bankAccount;
    }
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    
    //摘要
    @Column(name = "memo", unique = false, nullable = true, length = 200)
    private String memo;
    
    public String getMemo() {
        return this.memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    //版本
    @Column(name = "version", unique = false, nullable = false)
    private Integer version;
    
    public Integer getVersion() {
        return this.version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    //状态（新建NEW，确认CONFIRM，取消CANCEL）
    @Column(name = "status", unique = false, nullable = false, length = 10)
    private String status;
    
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    //审批状态（未提交UNSUBMIT、已提交SUBMIT、已审批APPROVE、已驳回REJECT）
    @Column(name = "approve_status", unique = false, nullable = false, length = 10)
    private String approveStatus;
    
    public String getApproveStatus() {
        return this.approveStatus;
    }
    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }
    
    //付款状态（未付款N，已付款Y，部分付款PART）
    @Column(name = "paid_status", unique = false, nullable = true, length = 10)
    private String paidStatus;
    
    public String getPaidStatus() {
        return paidStatus;
    }
    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    //制单人
    @Column(name = "staff_code", unique = false, nullable = false, length = 45)
    private String staffCode;
    
    public String getStaffCode() {
        return this.staffCode;
    }
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
    
    //制单部门
    @Column(name = "department_code", unique = false, nullable = false, length = 45)
    private String departmentCode;
    
    public String getDepartmentCode() {
        return this.departmentCode;
    }
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
    
    //创建时间
    @Column(name = "created_date", unique = false, nullable = false)
    private Date createdDate;
    
    public Date getCreatedDate() {
        return this.createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    //创建人
    @Column(name = "created_by", unique = false, nullable = false, length = 45)
    private String createdBy;
    
    public String getCreatedBy() {
        return this.createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    //最后修改时间
    @Column(name = "last_updated_date", unique = false, nullable = true)
    private Date lastUpdatedDate;
    
    public Date getLastUpdatedDate() {
        return this.lastUpdatedDate;
    }
    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
    
    //最后修改人
    @Column(name = "last_updated_by", unique = false, nullable = true, length = 45)
    private String lastUpdatedBy;
    
    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    //组织机构
    @Column(name = "org_code", unique = false, nullable = false, length = 10)
    private String orgCode;
    
    public String getOrgCode() {
        return this.orgCode;
    }
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    
    
    
    /*
         * 用于展示的字段
     */
    @Transient
    private String staffName;
    @Transient
    private String departmentName;
    @Transient
    private String paySourceHeadName;
    @Transient
    private Double paySourceHeadAmount = 0D;
    @Transient
    private Double paySourceHeadHISAmount = 0D;
    @Transient
    private String receiverName;
    @Transient
    private String vendorAddress;
    @Transient
    private String vendorContact;
    @Transient
    private String vendorLicenseNumber;
    @Transient
    private String bankName;
    @Transient
    private Double taxAmount;

    public String getStaffName() {
        return staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public String getPaySourceHeadName() {
        return paySourceHeadName;
    }
    public void setPaySourceHeadName(String paySourceHeadName) {
        this.paySourceHeadName = paySourceHeadName;
    }
    public Double getPaySourceHeadAmount() {
        return paySourceHeadAmount;
    }
    public void setPaySourceHeadAmount(Double paySourceHeadAmount) {
        this.paySourceHeadAmount = paySourceHeadAmount;
    }
    public Double getPaySourceHeadHISAmount() {
        return paySourceHeadHISAmount;
    }
    public void setPaySourceHeadHISAmount(Double paySourceHeadHISAmount) {
        this.paySourceHeadHISAmount = paySourceHeadHISAmount;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public Double getTaxAmount() {
        return taxAmount;
    }
    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }
    public String getVendorAddress() {
        return vendorAddress;
    }
    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }
    public String getVendorContact() {
        return vendorContact;
    }
    public void setVendorContact(String vendorContact) {
        this.vendorContact = vendorContact;
    }
    public String getVendorLicenseNumber() {
        return vendorLicenseNumber;
    }
    public void setVendorLicenseNumber(String vendorLicenseNumber) {
        this.vendorLicenseNumber = vendorLicenseNumber;
    }
    
}