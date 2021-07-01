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
package com.erp.finance.ap.pay.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.framework.dao.BasicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import com.framework.annotation.Cache;
import com.framework.annotation.Permissions;
import com.framework.annotation.Permissions.PermissionType;
import com.framework.annotation.SqlParam;
import com.framework.dao.model.Pages;
import com.framework.util.DaoUtil;
import com.erp.finance.ap.invoice.dao.model.ApInvoiceLine;
import com.erp.finance.ap.pay.dao.ApPayLineDao;
import com.erp.finance.ap.pay.dao.model.ApPayLine;
import com.erp.finance.ap.pay.dao.model.ApPayLineCO;

@Repository
public class ApPayLineDaoImpl implements ApPayLineDao{ 

    //注入basicDao工具类
    @Autowired
    private BasicDao basicDao;
    
    @Override
    public void insertDataObject(ApPayLine obj) {
        this.basicDao.insertDataTransaction(obj);
    }

    @Override
    public void updateDataObject(ApPayLine obj) {
        this.basicDao.updateDataTransaction(obj);
    }
    
    @Override
    public void insertOrUpdateDataObject(ApPayLine obj) {
        this.basicDao.insertOrUpdateDataTransaction(obj);
    }

    @Override
    public void deleteDataObject(ApPayLine obj) {
        this.basicDao.deleteDataTransactionJPA(obj);
    }

    @Override
    public List<ApPayLine> getDataObjects() {
        return this.basicDao.getDataAllObject(ApPayLine.class);
    }

    @Override
    public ApPayLine getDataObject(int id) {
        return (ApPayLine)this.basicDao.getDataObject(ApPayLine.class, id);
    }
    
    @Override
    public ApPayLine getDataObject(String code) {
        return null;
    }
    
    @Override
    public List<ApPayLine> getDataObjects(ApPayLineCO paramObj) {
        return null;
    }
    
    @Override
    public List<ApPayLine> getDataObjects(Pages pages) {
        return null;
    }
    
    @Override
    public List<ApPayLine> getDataObjects(Pages pages, ApPayLineCO paramObj) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getDataObjectsArray(Pages pages, ApPayLineCO paramObj) {
        return null;
    }
    
    @Override
    @Permissions(PermissionType.DATA_AUTH)
    public List<ApPayLine> getDataObjectsForDataAuth(@SqlParam String dataAuthSQL, Pages pages, ApPayLineCO paramObj) {
        return null;
    }
    
    @Override
    public List<ApPayLine> getPayLineListByHeadCode(Pages pages, ApPayLineCO paramObj) {
        String sql = "select l.* from ap_pay_line l where l.pay_head_code=:payheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("payheadcode", paramObj.getPayHeadCode());
        sql = sql + " order by l.pay_line_id";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("l", ApPayLine.class);
        
        return this.basicDao.getDataSql(sql, entity, args, pages);
    }
    
    @Override
    public void deleteLineByHeadCode(String headCode) {
        String sql = "delete from ap_pay_line where pay_head_code = :payheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("payheadcode", headCode);
        
        this.basicDao.executeSQLTransaction(sql, args);
    }
    
    @Override
    public List<ApPayLine> getApPayLineListByHeadCode(String headCode) {
        String sql = "select l.* from ap_pay_line l where l.pay_head_code=:payheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("payheadcode", headCode);
        sql = sql + " order by l.pay_line_id";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("l", ApPayLine.class);
        
        return this.basicDao.selectData(sql, entity, args);
    }
    
    @Override
    public Double getInvoicePaidAmount(String invoiceHeadCode, Integer payLineId) {
        String sql = "select sum(l.invoice_pay_amount) from ap_pay_line l where l.invoice_head_code = :code and l.pay_line_id <> :id";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("code", invoiceHeadCode);
        args.put("id", payLineId);
        
        List list = this.basicDao.selectDataSqlCount(sql, args);
        if(list!=null&&list.size()>0) {
            if(list.get(0)!=null) {
                return Double.valueOf(String.valueOf(list.get(0)));
            }
        }
        
        return 0D;
    }
    
}
