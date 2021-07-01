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
/**
 * @description ApPayHeadDaoImpl.java
 * @author dongbin
 * @version 
 * @copyright
 */

package com.erp.common.ap.pay.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.framework.dao.BasicDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.erp.common.ap.pay.dao.ApPayHeadDao;

/**
 * @description
 * @date 2020年10月8日
 * @author dongbin
 * 
 */
@Repository("apPayHeadDaoCommon")
public class ApPayHeadDaoImpl implements ApPayHeadDao {
    
    //注入basicDao工具类
    @Autowired
    private BasicDao basicDao;
    
    @Override
    public boolean isExistApPayRelateApInvoice(String headCode) {
        String sql = "select count(*) from ap_pay_line h where h.invoice_head_code = :headCode";
        
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("headCode", headCode);
        
        List list = this.basicDao.selectDataSqlCount(sql, args);
        if(list.size()>0) {
            int count = this.basicDao.convertSQLCount(list.get(0));
            if(count>0) {
                return true;
            }
        }
        
        return false;
    }

}
