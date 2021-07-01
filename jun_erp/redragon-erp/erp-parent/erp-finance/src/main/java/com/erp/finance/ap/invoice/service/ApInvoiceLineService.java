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
package com.erp.finance.ap.invoice.service;

import java.math.BigDecimal;
import java.util.List;

import com.erp.finance.ap.invoice.dao.model.ApInvoiceLine;
import com.erp.finance.ap.invoice.dao.model.ApInvoiceLineCO;
import com.framework.api.DaoCRUDIF;
import com.framework.dao.model.Pages;


public interface ApInvoiceLineService extends DaoCRUDIF<ApInvoiceLine, ApInvoiceLineCO> {
    
    //获取行列表分页（根据头code）
    public abstract List<ApInvoiceLine> getApInvoiceLineListByHeadCode(Pages pages, ApInvoiceLineCO paramObj);
    
    //获取采购订单历史付款金额
    public abstract BigDecimal getHISApInvoiceAmountForPO(String poHeadCode, String headCode);
    
    //删除付款行（根据头）
    public abstract void deleteLineByHeadCode(String headCode);
    
    //获取付款单总金额
    public abstract BigDecimal getApInvoiceAmountByHeadCode(String headCode);
    
    //获取发票行不含税金额和税额汇总
    public abstract BigDecimal[] getInvoiceLineAmountSumByHeadCode(String headCode);
    
    //获取列表行（根据头code）
    public abstract List<ApInvoiceLine> getApInvoiceLineListByHeadCode(String headCode);
    
    //获取采购订单行已开票数量
    public abstract Double getMadeInvoiceQuantityByPoLine(String poLineCode, Integer invoiceLineId);
    
}
