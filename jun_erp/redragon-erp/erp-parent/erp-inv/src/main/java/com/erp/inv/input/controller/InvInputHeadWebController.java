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
package com.erp.inv.input.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import com.framework.controller.JsonTextUtil;
import com.framework.shiro.ShiroUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.framework.controller.ControllerSupport;
import com.framework.dao.data.GlobalDataBox;
import com.framework.dao.model.Pages;
import com.erp.common.ap.invoice.service.ApInvoiceHeadService;
import com.erp.common.voucher.service.FinVoucherBillRService;
import com.erp.common.voucher.service.FinVoucherHeadService;
import com.erp.common.voucher.service.FinVoucherModelHeadService;
import com.erp.dataset.service.DatasetCommonService;
import com.erp.hr.dao.model.HrStaffInfoRO;
import com.erp.hr.service.HrCommonService;
import com.erp.inv.input.dao.data.DataBox;
import com.erp.inv.input.dao.model.InvInputHead;
import com.erp.inv.input.dao.model.InvInputHeadCO;
import com.erp.inv.input.dao.model.InvInputLine;
import com.erp.inv.input.service.InvInputHeadService;
import com.erp.inv.input.service.InvInputLineService;
import com.erp.inv.stock.service.InvStockService;
import com.erp.inv.warehouse.dao.model.InvWarehouse;
import com.erp.inv.warehouse.service.InvWarehouseService;
import com.erp.masterdata.common.service.MasterDataCommonService;
import com.erp.masterdata.vendor.dao.model.MdVendor;
import com.erp.masterdata.vendor.dao.model.MdVendorContact;
import com.erp.masterdata.vendor.dao.model.MdVendorContactCO;
import com.erp.masterdata.vendor.service.MdVendorBankService;
import com.erp.masterdata.vendor.service.MdVendorContactService;
import com.erp.order.po.dao.model.PoHead;
import com.erp.order.po.dao.model.PoHeadCO;
import com.erp.order.po.dao.model.PoLine;
import com.erp.order.po.service.PoHeadService;
import com.erp.order.po.service.PoLineService;

@Controller
@RequestMapping("/web/invInputHead")
public class InvInputHeadWebController extends ControllerSupport{
    
    //日志处理
    private Logger logger = LoggerFactory.getLogger(InvInputHeadWebController.class);
    
    //服务层注入
    @Autowired
    private InvInputHeadService invInputHeadService;
    @Autowired
    private InvInputLineService invInputLineService;
    @Autowired
    private DatasetCommonService datasetCommonService;
    @Autowired
    private HrCommonService hrCommonService;
    @Autowired
    private MasterDataCommonService masterDataCommonService;
    @Autowired
    private PoHeadService poHeadService;
    @Autowired
    private PoLineService poLineService;
    @Autowired
    private InvWarehouseService invWarehouseService;
    @Autowired
    @Qualifier("finVoucherModelHeadServiceCommon")
    private FinVoucherModelHeadService finVoucherModelHeadService;
    @Autowired
    @Qualifier("finVoucherHeadServiceCommon")
    private FinVoucherHeadService finVoucherHeadService;
    @Autowired
    @Qualifier("finVoucherBillRServiceCommon")
    private FinVoucherBillRService finVoucherBillRService;
    @Autowired
    @Qualifier("apInvoiceHeadServiceCommon")
    private ApInvoiceHeadService apInvoiceHeadService;
    
    
    
    @Override
    public String getExceptionRedirectURL() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    
    /**
     * 
     * @description 查询数据列表
     * @date 2020-08-20 17:21:04
     * @author 
     * @param pages
     * @param invInputHeadCO
     * @param model
     * @return String
     *
     */
    @RequestMapping("getInvInputHeadList")
    public String getInvInputHeadList(Pages pages, InvInputHeadCO invInputHeadCO, Model model) {
        //分页查询数据
        if(pages.getPage()==0) {
            pages.setPage(1);
        }
        
        //分页查询数据
        List<InvInputHead> invInputHeadList = this.invInputHeadService.getDataObjectsForDataAuth("", pages, invInputHeadCO);
        
        //入库状态
        Map inputStatusMap = DataBox.getInputStatusMap();
        //来源类型
        Map inputSourceTypeMap = DataBox.getInputSourceTypeMap();
        //入库类型
        Map inputTypeMap = DataBox.getInputTypeMap();
        //审批状态
        Map approveStatusMap = GlobalDataBox.getApproveStatusMap();
        //仓库
        List<InvWarehouse> warehouseList = this.invWarehouseService.getDataObjects();
        Map<String, InvWarehouse> warehouseMap = new HashMap<String, InvWarehouse>();
        for(InvWarehouse invWarehouse: warehouseList) {
            warehouseMap.put(invWarehouse.getWarehouseCode(), invWarehouse);
        }
        
        //页面属性设置
        model.addAttribute("invInputHeadList", invInputHeadList);
        model.addAttribute("pages", pages);
        model.addAttribute("inputStatusMap", inputStatusMap);
        model.addAttribute("inputSourceTypeMap", inputSourceTypeMap);
        model.addAttribute("inputTypeMap", inputTypeMap);
        model.addAttribute("approveStatusMap", approveStatusMap);
        model.addAttribute("warehouseMap", warehouseMap);
        
        return "basic.jsp?content=invInput/invInputList";
    }
    
    
    
    /**
     * 
     * @description 查询单条数据
     * @date 2020-08-20 17:21:04
     * @author 
     * @param invInputHead
     * @param model
     * @return String
     *
     */
    @RequestMapping("getInvInputHead")
    public String getInvInputHead(InvInputHead invInputHead, Model model) {
        //入库状态
        Map inputStatusMap = DataBox.getInputStatusMap();
        //来源类型
        Map inputSourceTypeMap = DataBox.getInputSourceTypeMap();
        //入库类型
        Map inputTypeMap = DataBox.getInputTypeMap();
        //审批状态
        Map approveStatusMap = GlobalDataBox.getApproveStatusMap();
        //仓库
        List<InvWarehouse> warehouseList = this.invWarehouseService.getDataObjects();
        Map<String, InvWarehouse> warehouseMap = new HashMap<String, InvWarehouse>();
        for(InvWarehouse invWarehouse: warehouseList) {
            warehouseMap.put(invWarehouse.getWarehouseCode(), invWarehouse);
        }
        
        //查询要编辑的数据
        if(invInputHead!=null&&StringUtils.isNotBlank(invInputHead.getInputHeadCode())) {
            invInputHead = this.invInputHeadService.getDataObject(invInputHead.getInputHeadCode());
            //设置显示字段
            invInputHead.setStaffName(this.hrCommonService.getHrStaff(invInputHead.getStaffCode()).getStaffName());
            invInputHead.setDepartmentName(this.hrCommonService.getHrDepartment(invInputHead.getDepartmentCode()).getDepartmentName());
            //仓库地址
            invInputHead.setWarehouseAddress(warehouseMap.get(invInputHead.getWarehouseCode()).getWarehouseAddress());
            //获取采购订单信息
            if(invInputHead.getInputSourceType().equals("PO")) {
                PoHead poHead = this.poHeadService.getDataObject(invInputHead.getInputSourceHeadCode());
                invInputHead.setInputSourceHeadName(poHead.getPoName());
                //获取供应商信息
                MdVendor mdVendor = this.masterDataCommonService.getMdVendorInfoCache(poHead.getVendorCode());
                if(mdVendor!=null) {
                    invInputHead.setVendorName(mdVendor.getVendorName());
                    
                    //获取联系人信息
                    List<MdVendorContact> mdVendorContactList = mdVendor.getMdVendorContactList();
                    if(mdVendorContactList.size()>0) {
                        invInputHead.setVendorContactDesc(mdVendorContactList.get(0).getContactName()+"；电话"+mdVendorContactList.get(0).getContactTelephone());
                    }
                }
                
                
            }
            
        }else {
            //初始化默认字段
            invInputHead.setStatus("NEW");
            
            //获取当前用户职员信息
            HrStaffInfoRO staffInfo = this.hrCommonService.getStaffInfo(ShiroUtil.getUsername());
            invInputHead.setStaffCode(staffInfo.getStaffCode());
            invInputHead.setDepartmentCode(staffInfo.getDepartmentCode());
            invInputHead.setStaffName(staffInfo.getStaffName());
            invInputHead.setDepartmentName(staffInfo.getDepartmentName());
        }
        
        
        
        //页面属性设置
        model.addAttribute("invInputHead", invInputHead);
        model.addAttribute("inputStatusMap", inputStatusMap);
        model.addAttribute("inputSourceTypeMap", inputSourceTypeMap);
        model.addAttribute("inputTypeMap", inputTypeMap);
        model.addAttribute("approveStatusMap", approveStatusMap);
        model.addAttribute("warehouseMap", warehouseMap);
        
        return "basic.jsp?content=invInput/invInputEdit";
    }
    
    
    
    /**
     * 
     * @description 获取采购订单选择框
     * @date 2020年7月20日
     * @author dongbin
     * @return
     * @return String
     *
     */
    @RequestMapping("getSelectPOModal")
    public String getSelectPOModal(Pages pages, PoHeadCO poHeadCO, Model model) {
        //分页查询数据
        if(pages.getPage()==0) {
            pages.setPage(1);
        }
        
        //分页查询数据
        poHeadCO.setStatus("CONFIRM");
        poHeadCO.setApproveStatus("APPROVE");
        List<PoHead> poHeadList = this.poHeadService.getDataObjects(pages, poHeadCO);
        
        //采购订单类型
        Map poTypeMap = this.datasetCommonService.getPOType();
        //状态
        Map poStatusMap = com.erp.order.po.dao.data.DataBox.getPoStatusMap();
        //获取供应商
        Map vendorMap = this.masterDataCommonService.getVendorMap();
        //获取项目
        Map projectMap = this.masterDataCommonService.getProjectMap();
        
        //循环设置职员和组织信息
        for(PoHead poHead: poHeadList) {
            poHead.setStaffName(this.hrCommonService.getHrStaff(poHead.getStaffCode()).getStaffName());
            poHead.setDepartmentName(this.hrCommonService.getHrDepartment(poHead.getDepartmentCode()).getDepartmentName());
            //获取联系人信息
            MdVendor mdVendor = this.masterDataCommonService.getMdVendorInfoCache(poHead.getVendorCode());
            if(mdVendor!=null) {
                List<MdVendorContact> mdVendorContactList = mdVendor.getMdVendorContactList();
                if(mdVendorContactList.size()>0) {
                    poHead.setVendorContactDesc(mdVendorContactList.get(0).getContactName()+"；电话"+mdVendorContactList.get(0).getContactTelephone());
                }
            }
        }
        
        //页面属性设置
        model.addAttribute("poHeadList", poHeadList);
        model.addAttribute("pages", pages);
        model.addAttribute("poTypeMap", poTypeMap);
        model.addAttribute("poStatusMap", poStatusMap);
        model.addAttribute("vendorMap", vendorMap);
        model.addAttribute("projectMap", projectMap);
        
        return "invInput/pop/selectPOModal";
    }
    
    
    
    /**
     * 
     * @description 编辑数据
     * @date 2020-08-20 17:21:04
     * @author 
     * @param invInputHead
     * @param bindingResult
     * @param model
     * @return String
     *
     */
    @RequestMapping("editInvInputHead")
    public String editInvInputHead(@Valid InvInputHead invInputHead, BindingResult bindingResult, Model model, RedirectAttributes attr) {
        //参数校验
        Map<String, String> errorMap = this.validateParameters(bindingResult, model);
        if(errorMap.size()>0) {
            return "forward:getInvInputHead";
        }
        
        //对当前编辑的对象初始化默认的字段
        
        //保存编辑的数据
        this.invInputHeadService.insertOrUpdateDataObject(invInputHead);
        //提示信息
        attr.addFlashAttribute("hint", "success");
        attr.addAttribute("inputHeadCode", invInputHead.getInputHeadCode());
        
        return "redirect:getInvInputHead";
    }
    
    
    
    /**
     * 
     * @description 删除数据
     * @date 2020-08-20 17:21:04
     * @author 
     * @param invInputHead
     * @return String
     *
     */
    @RequestMapping("deleteInvInputHead")
    public String deleteInvInputHead(InvInputHead invInputHead, RedirectAttributes attr) {
        //删除数据前验证数据
        if(invInputHead!=null&&invInputHead.getInputHeadId()!=null) {
            if(invInputHead.getStatus().equals("NEW")) {
                //删除数据
                this.invInputHeadService.deleteDataObject(invInputHead);
                
                //提示信息
                attr.addFlashAttribute("hint", "success");
            }else {
                //提示信息
                attr.addFlashAttribute("hint", "hint");
                attr.addFlashAttribute("alertMessage", "非新建状态的入库单不能删除");
            }
        }
        
        return "redirect:getInvInputHeadList";
    }
    
    
    
    /**
     * 
     * @description 更新审批状态
     * @date 2020年8月4日
     * @author dongbin
     * @param code
     * @param approveStatus
     * @param attr
     * @return
     * @return String
     *
     */
    @RequestMapping("updateApproveStatus")
    public String updateApproveStatus(String code, String approveStatus, RedirectAttributes attr) {
        
        if(StringUtils.isNotBlank(code)&&StringUtils.isNotBlank(approveStatus)) {
            if(approveStatus.equals("UNSUBMIT")) {
                InvInputHead invInputHead = this.invInputHeadService.getDataObject(code);
                boolean invoiceFlag = this.apInvoiceHeadService.isExistApInvoiceRelatePO(invInputHead.getInputSourceHeadCode());
                if(invoiceFlag) {
                    //提示信息
                    attr.addFlashAttribute("hint", "hint");
                    attr.addFlashAttribute("alertMessage", "当前入库关联的采购订单已开票不能变更");
                    attr.addAttribute("inputHeadCode", code);
                    return "redirect:getInvInputHead";
                }
            }
            
            //更新审核状态
            this.invInputHeadService.updateApproveStatus(code, approveStatus);
            //提示信息
            attr.addFlashAttribute("hint", "success");
            attr.addAttribute("inputHeadCode", code);
        }else {
            //提示信息
            attr.addFlashAttribute("hint", "hint");
            attr.addFlashAttribute("alertMessage", "提交或审批数据错误");
            attr.addAttribute("inputHeadCode", code);
        }
        
        return "redirect:getInvInputHead";
    }
    
    
    
    /**
     * 
     * @description 自动创建凭证分录
     * @date 2020年9月25日
     * @author dongbin
     * @param headCode
     * @return
     * @return String
     *
     */
    @RequestMapping("autoCreateVoucherEntry")
    @ResponseBody
    public String autoCreateVoucherEntry(String headCode){
        try {
            //删除自动生成的凭证和分录
            //根据单据头code获取凭证头code
            String voucherHeadCode = this.finVoucherBillRService.getVoucherHeadCodeByBillHeadCode("INPUT", headCode);
            //删除凭证
            if(StringUtils.isNotBlank(voucherHeadCode)) {
                this.finVoucherHeadService.deleteFinVoucherHeadByVoucherHeadCode(voucherHeadCode);
            }
            
            //自动生成凭证和分录
            //计算分录的金额
            //获取入库行
            List<InvInputLine> inputLineList = this.invInputLineService.getInvInputLineListByInputHeadCode(headCode);
            
            BigDecimal voucherAmount = new BigDecimal(0D);
            //循环获取加和入库数量和物料单价
            for(InvInputLine invInputLine: inputLineList) {
                BigDecimal quantity = new BigDecimal(invInputLine.getInputQuantity());
                //获取采购订单行
                PoLine poLine = this.poLineService.getDataObject(invInputLine.getInputSourceLineCode());
                BigDecimal price = new BigDecimal(poLine.getPrice());
                //计算行金额
                BigDecimal lineAmount = quantity.multiply(price);
                //计算合计金额
                voucherAmount = voucherAmount.add(lineAmount);
            }
            
            //调用自动创建方法
            this.finVoucherModelHeadService.autoCreateVoucher(headCode, new Double[]{voucherAmount.doubleValue()}, "INPUT");
            
            return JsonTextUtil.getErrorJson(0);
        }catch(Exception e) {
            return JsonTextUtil.getErrorJson(-1, "重新生成分录错误");
        }
    }
}
