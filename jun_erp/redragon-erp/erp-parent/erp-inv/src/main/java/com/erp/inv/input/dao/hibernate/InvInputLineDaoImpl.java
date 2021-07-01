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
package com.erp.inv.input.dao.hibernate;

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
import com.erp.inv.input.dao.InvInputLineDao;
import com.erp.inv.input.dao.model.InvInputLine;
import com.erp.inv.input.dao.model.InvInputLineCO;

@Repository
public class InvInputLineDaoImpl implements InvInputLineDao{ 

    //注入basicDao工具类
    @Autowired
    private BasicDao basicDao;
    
    @Override
    public void insertDataObject(InvInputLine obj) {
        this.basicDao.insertDataTransaction(obj);
    }

    @Override
    public void updateDataObject(InvInputLine obj) {
        this.basicDao.updateDataTransaction(obj);
    }
    
    @Override
    public void insertOrUpdateDataObject(InvInputLine obj) {
        this.basicDao.insertOrUpdateDataTransaction(obj);
    }

    @Override
    public void deleteDataObject(InvInputLine obj) {
        this.basicDao.deleteDataTransactionJPA(obj);
    }

    @Override
    public List<InvInputLine> getDataObjects() {
        return this.basicDao.getDataAllObject(InvInputLine.class);
    }

    @Override
    public InvInputLine getDataObject(int id) {
        return (InvInputLine)this.basicDao.getDataObject(InvInputLine.class, id);
    }
    
    @Override
    public InvInputLine getDataObject(String code) {
        return null;
    }
    
    @Override
    public List<InvInputLine> getDataObjects(InvInputLineCO paramObj) {
        return null;
    }
    
    @Override
    public List<InvInputLine> getDataObjects(Pages pages) {
        return null;
    }
    
    @Override
    public List<InvInputLine> getDataObjects(Pages pages, InvInputLineCO paramObj) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getDataObjectsArray(Pages pages, InvInputLineCO paramObj) {
        return null;
    }
    
    @Override
    @Permissions(PermissionType.DATA_AUTH)
    public List<InvInputLine> getDataObjectsForDataAuth(@SqlParam String dataAuthSQL, Pages pages, InvInputLineCO paramObj) {
        return null;
    }
    
    @Override
    public List<InvInputLine> getInvInputLineListByInputHeadCode(Pages pages, InvInputLineCO paramObj) {
        String sql = "select l.* from inv_input_line l where input_head_code=:inputheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("inputheadcode", paramObj.getInputHeadCode());
        sql = sql + " order by l.input_line_id";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("l", InvInputLine.class);
        
        return this.basicDao.getDataSql(sql, entity, args, pages);
    }
    
    @Override
    public void deleteInvInputLineByInputHeadCode(String inputHeadCode) {
        String sql = "delete from inv_input_line where input_head_code = :inputheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("inputheadcode", inputHeadCode);
        
        this.basicDao.executeSQLTransaction(sql, args);
    }
    
    @Override
    public Double getInputQuantityByPoLineCode(String poLineCode) {
        String sql = "select sum(l.input_quantity) from inv_input_line l where l.input_source_line_code = :poLineCode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("poLineCode", poLineCode);
        
        List list = this.basicDao.selectDataSqlCount(sql, args);
        if(list!=null&&list.size()>0) {
            if(list.get(0)!=null) {
                return Double.valueOf(String.valueOf(list.get(0)));
            }
        }
        
        return 0D;
    }
    
    @Override
    public List<InvInputLine> getInvInputLineListByInputHeadCode(String inputHeadCode) {
        String sql = "select l.* from inv_input_line l where input_head_code=:inputheadcode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("inputheadcode", inputHeadCode);
        sql = sql + " order by l.input_line_id";
        
        Map<String, Class<?>> entity = new HashMap<String, Class<?>>();
        entity.put("l", InvInputLine.class);
        
        return this.basicDao.selectData(sql, entity, args);
    }
    
    @Override
    public Double getInputedQuantityByPoLine(String poLineCode, Integer inputLineId) {
        String sql = "select sum(l.input_quantity) from inv_input_line l where l.input_source_line_code = :code and l.input_line_id <> :id";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("code", poLineCode);
        args.put("id", inputLineId);
        
        List list = this.basicDao.selectDataSqlCount(sql, args);
        if(list!=null&&list.size()>0) {
            if(list.get(0)!=null) {
                return Double.valueOf(String.valueOf(list.get(0)));
            }
        }
        
        return 0D;
    }
    
}
