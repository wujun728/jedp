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
package com.erp.finance.ap.pay.controller;

import java.math.BigDecimal;
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
import com.erp.common.voucher.service.FinVoucherBillRService;
import com.erp.common.voucher.service.FinVoucherHeadService;
import com.erp.common.voucher.service.FinVoucherModelHeadService;
import com.erp.dataset.service.DatasetCommonService;
import com.erp.finance.ap.invoice.dao.model.ApInvoiceHead;
import com.erp.finance.ap.pay.dao.data.DataBox;
import com.erp.finance.ap.pay.dao.model.ApPayHead;
import com.erp.finance.ap.pay.dao.model.ApPayHeadCO;
import com.erp.finance.ap.pay.dao.model.ApPayLine;
import com.erp.finance.ap.pay.service.ApPayHeadService;
import com.erp.finance.ap.pay.service.ApPayLineService;
import com.erp.finance.ar.invoice.dao.model.ArInvoiceLine;
import com.erp.hr.dao.model.HrStaffInfoRO;
import com.erp.hr.service.HrCommonService;
import com.erp.masterdata.common.service.MasterDataCommonService;
import com.erp.masterdata.vendor.service.MdVendorBankService;
import com.erp.order.po.dao.model.PoHead;

@Controller
@RequestMapping("/web/apPayHead")
public class ApPayHeadWebController extends ControllerSupport{
    
    //日志处理
    private Logger logger = LoggerFactory.getLogger(ApPayHeadWebController.class);
    
    //服务层注入
    @Autowired
    private ApPayHeadService apPayHeadService;
    @Autowired
    private ApPayLineService apPayLineService;
    @Autowired
    private DatasetCommonService datasetCommonService;
    @Autowired
    private HrCommonService hrCommonService;
    @Autowired
    private MasterDataCommonService masterDataCommonService;
    @Autowired
    private MdVendorBankService mdVendorBankService;
    @Autowired
    @Qualifier("finVoucherModelHeadServiceCommon")
    private FinVoucherModelHeadService finVoucherModelHeadService;
    @Autowired
    @Qualifier("finVoucherHeadServiceCommon")
    private FinVoucherHeadService finVoucherHeadService;
    @Autowired
    @Qualifier("finVoucherBillRServiceCommon")
    private FinVoucherBillRService finVoucherBillRService;
    
    
    
    @Override
    public String getExceptionRedirectURL() {
        return "redirect:getApPayHeadList";
    }
    
    
    
    /**
     * 
     * @description 查询数据列表
     * @date 2020-09-15 14:43:59
     * @author 
     * @param pages
     * @param apPayHeadCO
     * @param model
     * @return String
     *
     */
    @RequestMapping("getApPayHeadList")
    public String getApPayHeadList(Pages pages, ApPayHeadCO apPayHeadCO, Model model) {
        //分页查询数据
        if(pages.getPage()==0) {
            pages.setPage(1);
        }
        
        //分页查询数据
        List<ApPayHead> apPayHeadList = this.apPayHeadService.getDataObjects(pages, apPayHeadCO);
        
        //付款类型
        Map payTypeMap = DataBox.getApPayType();
        //状态
        Map payStatusMap = GlobalDataBox.getStatusMap();
        //获取供应商
        Map vendorMap = this.masterDataCommonService.getVendorMap();
        //审批状态
        Map approveStatusMap = GlobalDataBox.getApproveStatusMap();
        
        //循环设置职员和组织信息
        for(ApPayHead payHead: apPayHeadList) {
            payHead.setStaffName(this.hrCommonService.getHrStaff(payHead.getStaffCode()).getStaffName());
            payHead.setDepartmentName(this.hrCommonService.getHrDepartment(payHead.getDepartmentCode()).getDepartmentName());
        }
        
        //页面属性设置
        model.addAttribute("apPayHeadList", apPayHeadList);
        model.addAttribute("pages", pages);
        model.addAttribute("payTypeMap", payTypeMap);
        model.addAttribute("payStatusMap", payStatusMap);
        model.addAttribute("vendorMap", vendorMap);
        model.addAttribute("approveStatusMap", approveStatusMap);
        
        return "basic.jsp?content=apPay/apPayList";
    }
    
    
    
    /**
     * 
     * @description 查询单条数据
     * @date 2020-09-15 14:43:59
     * @author 
     * @param model
     * @return String
     *
     */
    @RequestMapping("getApPayHead")
    public String getApPayHead(ApPayHead payHead, Model model) {
        //查询要编辑的数据
        if(payHead!=null&&StringUtils.isNotBlank(payHead.getPayHeadCode())) {
            payHead = this.apPayHeadService.getDataObject(payHead.getPayHeadCode());
            //设置显示字段
            payHead.setStaffName(this.hrCommonService.getHrStaff(payHead.getStaffCode()).getStaffName());
            payHead.setDepartmentName(this.hrCommonService.getHrDepartment(payHead.getDepartmentCode()).getDepartmentName());
            //获取供应商信息
            payHead.setVendorName(this.masterDataCommonService.getVendorMap().get(payHead.getVendorCode()));
        }else {
            //初始化默认字段
            payHead.setStatus("NEW");
            
            //获取当前用户职员信息
            HrStaffInfoRO staffInfo = this.hrCommonService.getStaffInfo(ShiroUtil.getUsername());
            payHead.setStaffCode(staffInfo.getStaffCode());
            payHead.setDepartmentCode(staffInfo.getDepartmentCode());
            payHead.setStaffName(staffInfo.getStaffName());
            payHead.setDepartmentName(staffInfo.getDepartmentName());
        }
        
        //币种
        Map currencyMap = this.datasetCommonService.getCurrencyType();
        //付款来源类型
        Map payTypeMap = DataBox.getApPayType();
        //状态
        Map payStatusMap = GlobalDataBox.getStatusMap();
        //获取供应商
        Map vendorMap = this.masterDataCommonService.getVendorMap();
        //审批状态
        Map approveStatusMap = GlobalDataBox.getApproveStatusMap();
        
        //页面属性设置
        model.addAttribute("payHead", payHead);
        model.addAttribute("currencyMap", currencyMap);
        model.addAttribute("payTypeMap", payTypeMap);
        model.addAttribute("payStatusMap", payStatusMap);
        model.addAttribute("vendorMap", vendorMap);
        model.addAttribute("approveStatusMap", approveStatusMap);
        
        return "basic.jsp?content=apPay/apPayEdit";
    }
    
    
    
    /**
     * 
     * @description 编辑数据
     * @date 2020-09-15 14:43:59
     * @author 
     * @param apPayHead
     * @param bindingResult
     * @param model
     * @return String
     *
     */
    @RequestMapping("editApPayHead")
    public String editApPayHead(@Valid ApPayHead apPayHead, BindingResult bindingResult, Model model, RedirectAttributes attr) {
        //参数校验
        Map<String, String> errorMap = this.validateParameters(bindingResult, model);
        if(errorMap.size()>0) {
            return "forward:getApPayHead";
        }
        
        //对当前编辑的对象初始化默认的字段
        
        //保存编辑的数据
        this.apPayHeadService.insertOrUpdateDataObject(apPayHead);
        //提示信息
        attr.addFlashAttribute("hint", "success");
        attr.addAttribute("payHeadCode", apPayHead.getPayHeadCode());
        
        return "redirect:getApPayHead";
    }
    
    
    
    /**
     * 
     * @description 删除数据
     * @date 2020-09-15 14:43:59
     * @author 
     * @param apPayHead
     * @return String
     *
     */
    @RequestMapping("deleteApPayHead")
    public String deleteApPayHead(ApPayHead apPayHead, RedirectAttributes attr) {
        
        //删除数据前验证数据
        if(apPayHead!=null&&apPayHead.getPayHeadId()!=null) {
            if(apPayHead.getStatus().equals("NEW")) {
                //删除数据
                this.apPayHeadService.deleteDataObject(apPayHead);
                
                //提示信息
                attr.addFlashAttribute("hint", "success");
            }else {
                //提示信息
                attr.addFlashAttribute("hint", "hint");
                attr.addFlashAttribute("alertMessage", "非新建状态的付款不能删除");
            }
        }
        
        return "redirect:getApPayHeadList";
    }
    
    
    
    /**
     * 
     * @description 更新审批状态
     * @date 2020年8月4日
     * @author dongbin
     * @param code
     * @param approveStatus
     * @param attr
     * @return String
     *
     */
    @RequestMapping("updateApproveStatus")
    public String updateApproveStatus(String code, String approveStatus, RedirectAttributes attr) {
        
        if(StringUtils.isNotBlank(code)&&StringUtils.isNotBlank(approveStatus)) {
            if(approveStatus.equals("UNSUBMIT")) {
                boolean voucherFlag = this.finVoucherBillRService.isExistVoucherRelateArPay(code);
                if(voucherFlag) {
                    //提示信息
                    attr.addFlashAttribute("hint", "hint");
                    attr.addFlashAttribute("alertMessage", "当前付款已生成凭证不能变更");
                    attr.addAttribute("payHeadCode", code);
                    return "redirect:getApPayHead";
                }
            }
            
            //更新审核状态
            this.apPayHeadService.updateApproveStatus(code, approveStatus);
            //提示信息
            attr.addFlashAttribute("hint", "success");
            attr.addAttribute("payHeadCode", code);
        }else {
            //提示信息
            attr.addFlashAttribute("hint", "hint");
            attr.addFlashAttribute("alertMessage", "提交或审批数据错误");
            attr.addAttribute("payHeadCode", code);
        }
        
        return "redirect:getApPayHead";
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
            String voucherHeadCode = this.finVoucherBillRService.getVoucherHeadCodeByBillHeadCode("PAY", headCode);
            //删除凭证
            if(StringUtils.isNotBlank(voucherHeadCode)) {
                this.finVoucherHeadService.deleteFinVoucherHeadByVoucherHeadCode(voucherHeadCode);
            }
            
            //自动生成凭证和分录
            //计算分录的金额
            //获取入库行
            List<ApPayLine> lineList = this.apPayLineService.getApPayLineListByHeadCode(headCode);
            
            BigDecimal voucherAmount = new BigDecimal(0D);
            //循环获取加和
            for(ApPayLine line: lineList) {
                BigDecimal amount = new BigDecimal(line.getInvoicePayAmount());
                //计算合计金额
                voucherAmount = voucherAmount.add(amount);
            }
            
            //调用自动创建方法
            this.finVoucherModelHeadService.autoCreateVoucher(headCode, new Double[]{voucherAmount.doubleValue()}, "PAY");
            return JsonTextUtil.getErrorJson(0);
        }catch(Exception e) {
            return JsonTextUtil.getErrorJson(-1, "重新生成分录错误");
        }
    }
}
