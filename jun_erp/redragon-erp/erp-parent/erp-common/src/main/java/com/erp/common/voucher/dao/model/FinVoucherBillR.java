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
package com.erp.common.voucher.dao.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity(name="finVoucherBillRCommon")
@Table(name="fin_voucher_bill_r", schema="erp")
@DynamicInsert(true)
@DynamicUpdate(true)
public class FinVoucherBillR implements java.io.Serializable {

    //serialVersionUID
    private static final long serialVersionUID = 1L;

    //Constructors
    public FinVoucherBillR() {
    }
    
    //Fields
    
    //主键
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "vb_id", unique = true, nullable = false)
    private Integer vbId;
    
    public Integer getVbId() {
        return this.vbId;
    }
    public void setVbId(Integer vbId) {
        this.vbId = vbId;
    }
    
    //凭证头编码
    @NotBlank(message="凭证头编码不能为空")
    @Column(name = "voucher_head_code", unique = false, nullable = false, length = 45)
    private String voucherHeadCode;
    
    public String getVoucherHeadCode() {
        return this.voucherHeadCode;
    }
    public void setVoucherHeadCode(String voucherHeadCode) {
        this.voucherHeadCode = voucherHeadCode;
    }
    
    //单据类型(PAY，RECEIPT，INPUT，OUTPUT)
    @NotBlank(message="单据类型不能为空")
    @Column(name = "bill_type", unique = false, nullable = false, length = 45)
    private String billType;
    
    public String getBillType() {
        return this.billType;
    }
    public void setBillType(String billType) {
        this.billType = billType;
    }
    
    //单据头编码
    @NotBlank(message="单据头编码不能为空")
    @Column(name = "bill_head_code", unique = true, nullable = false, length = 45)
    private String billHeadCode;
    
    public String getBillHeadCode() {
        return this.billHeadCode;
    }
    public void setBillHeadCode(String billHeadCode) {
        this.billHeadCode = billHeadCode;
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
    
    
}