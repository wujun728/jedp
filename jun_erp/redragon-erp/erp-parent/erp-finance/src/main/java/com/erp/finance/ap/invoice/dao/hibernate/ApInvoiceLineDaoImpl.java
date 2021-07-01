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
package com.erp.finance.ap.invoice.dao.hibernate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.framework.dao.BasicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.erp.finance.ap.invoice.dao.ApInvoiceLineDao;
import com.erp.finance.ap.invoice.dao.model.ApInvoiceLine;
import com.erp.finance.ap.invoice.dao.model.ApInvoiceLineCO;
import com.framework.annotation.Permissions;
import com.framework.annotation.Permissions.PermissionType;
import com.framework.annotation.SqlParam;
import com.framework.dao.model.Pages;

@Repository
public class ApInvoiceLineDaoImpl implements ApInvoiceLineDao{ 

    //注入basicDao工具类
    @Autowired
    private BasicDao basicDao;
    
    @Override
    public void insertDataObject(ApInvoiceLine obj) {
        this.basicDao.insertDataTransaction(obj);
    }

    @Override
    public void updateDataObject(ApInvoiceLine obj) {
        this.basicDao.updateDataTransaction(obj);
    }
    
    @Override
    public void insertOrUpdateDataObject(ApInvoiceLine obj) {
        this.basicDao.insertOrUpdateDataTransaction(obj);
    }

    @Override
    public void deleteDataObject(ApInvoiceLine obj) {
        this.basicDao.deleteDataTransactionJPA(obj);
    }

    @Override
    public List<ApInvoiceLine> getDataObjects() {
        return this.basicDao.getDataAllObject(ApInvoiceLine.class);
    }

    @Override
    public ApInvoiceLine getDataObject(int id) {
        return (ApInvoiceLine)this.basicDao.getDataObject(ApInvoiceLine.class, id);
    }
    
    @Override
    public ApInvoiceLine getDataObject(String code) {
        return null;
    }
    
    @Override
    public List<ApInvoiceLine> getDataObjects(ApInvoiceLineCO paramObj) {
        return null;
    }
    
    @Override
    public List<ApInvoiceLine> getDataObjects(Pages pages) {
        return null;
    }
    
    @Override
    public List<ApInvoiceLine> getDataObjects(Pages pages, ApInvoiceLineCO paramObj) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getDataObjectsArray(Pages pages, ApInvoiceLineCO paramObj) {
        return null;
    }
    
    @Override
    @Permissions(PermissionType.OWN)
    public List<ApInvoiceLine> getDataObjectsForDataAuth(@SqlParam String dataAuthSQL, Pages pages, ApInvoiceLineCO paramObj) {
        return null;
    }
    
    @Override
    public List<ApInvoiceLine> getApInvoiceLineListByHeadCode(Pages pages, ApInvoiceLineCO paramObj) {
        String sql = "select p.* from ap_invoice_line p where invoice_head_code=:payheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("payheadcode", paramObj.getInvoiceHeadCode());
        sql = sql + " order by p.invoice_line_id";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("p", ApInvoiceLine.class);
        
        return this.basicDao.getDataSql(sql, entity, args, pages);
    }
    
    @Override
    public BigDecimal getHISApInvoiceAmountForPO(String poHeadCode, String payHeadCode) {
        String sql = "select sum(l.amount) from ap_invoice_line l where l.invoice_source_line_code in (select pl.po_line_code from po_line pl where pl.po_head_code = :poheadcode) and l.invoice_head_code <> :payheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("poheadcode", poHeadCode);
        args.put("payheadcode", payHeadCode);
        
        List list = this.basicDao.selectDataSqlCount(sql, args);
        if(list.size()>0) {
            return (BigDecimal)list.get(0);
        }
        
        return new BigDecimal(0);
    }
    
    @Override
    public void deleteLineByHeadCode(String headCode) {
        String sql = "delete from ap_invoice_line where invoice_head_code = :payheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("payheadcode", headCode);
        
        this.basicDao.executeSQLTransaction(sql, args);
    }
    
    @Override
    public BigDecimal getApInvoiceAmountByHeadCode(String headCode) {
        String sql = "select sum(l.amount) from ap_invoice_line l where l.invoice_head_code = :payheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("payheadcode", headCode);
        
        List<BigDecimal> list = this.basicDao.selectDataSqlCount(sql, args);
        if(list.size()>0) {
            return list.get(0);
        }
        
        return new BigDecimal(0);
    }
    
    @Override
    public BigDecimal[] getInvoiceLineAmountSumByHeadCode(String headCode) {
        String sql = "select sum(amount) as amount,sum(tax_amount) as tax_amount from ap_invoice_line l where l.invoice_head_code = :headCode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("headCode", headCode);
        
        BigDecimal[] sumAmount = new BigDecimal[2];
        List<Object[]> list = this.basicDao.selectDataSqlCount(sql, args);
        if(list.size()>0) {
            if(list.get(0)[0]!=null) {
                sumAmount[0] = new BigDecimal(String.valueOf(list.get(0)[0]));
            }else {
                sumAmount[0] = new BigDecimal(0D);
            }
            
            if(list.get(0)[1]!=null) {
                sumAmount[1] = new BigDecimal(String.valueOf(list.get(0)[1]));
            }else {
                sumAmount[1] = new BigDecimal(0D);
            }
        }
        
        return sumAmount;
    }
    
    @Override
    public List<ApInvoiceLine> getApInvoiceLineListByHeadCode(String headCode) {
        String sql = "select p.* from ap_invoice_line p where invoice_head_code=:payheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("payheadcode", headCode);
        sql = sql + " order by p.invoice_line_id";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("p", ApInvoiceLine.class);
        
        return this.basicDao.selectData(sql, entity, args);
    }
    
    @Override
    public Double getMadeInvoiceQuantityByPoLine(String poLineCode, Integer invoiceLineId) {
        String sql = "select sum(l.quantity) from ap_invoice_line l where l.invoice_source_line_code = :code and l.invoice_line_id <> :id";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("code", poLineCode);
        args.put("id", invoiceLineId);
        
        List list = this.basicDao.selectDataSqlCount(sql, args);
        if(list!=null&&list.size()>0) {
            if(list.get(0)!=null) {
                return Double.valueOf(String.valueOf(list.get(0)));
            }
        }
        
        return 0D;
    }
    
}
