package com.kakarote.crm.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.crm.entity.SimpleCrmEntity;
import com.kakarote.core.servlet.BaseService;
import com.kakarote.core.servlet.upload.FileEntity;
import com.kakarote.crm.common.CrmModel;
import com.kakarote.crm.entity.BO.CrmChangeOwnerUserBO;
import com.kakarote.crm.entity.BO.CrmContractSaveBO;
import com.kakarote.crm.entity.BO.CrmSearchBO;
import com.kakarote.crm.entity.BO.CrmUpdateInformationBO;
import com.kakarote.crm.entity.PO.CrmReceivables;
import com.kakarote.crm.entity.VO.CrmInfoNumVO;
import com.kakarote.crm.entity.VO.CrmModelFiledVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 回款表 服务类
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-05-28
 */
public interface ICrmReceivablesService extends BaseService<CrmReceivables> {
    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    public List<CrmModelFiledVO> queryField(Integer id);

    public List<List<CrmModelFiledVO>> queryFormPositionField(Integer id);

    /**
     * 查询所有数据
     *
     * @param search 搜索对象
     * @return data
     */
    public BasePage<Map<String, Object>> queryPageList(CrmSearchBO search);

    public List<SimpleCrmEntity> querySimpleEntity(List<Integer> ids);
    /**
     * 查询字段配置
     *
     * @param id 主键ID
     * @return data
     */
    public CrmModel queryById(Integer id);

    /**
     * 保存或新增信息
     *
     * @param crmModel model
     */
    public void addOrUpdate(CrmContractSaveBO crmModel);

    /**
     * 删除回款数据
     *
     * @param ids ids
     */
    public void deleteByIds(List<Integer> ids);

    /**
     * 修改回款负责人
     *
     * @param changeOwnerUserBO   负责人变更BO
     */
    public void changeOwnerUser(CrmChangeOwnerUserBO changeOwnerUserBO);

    /**
     * 全部导出
     *
     * @param response resp
     * @param search   搜索对象
     */
    public void exportExcel(HttpServletResponse response, CrmSearchBO search);

    /**
     * 查询文件数量
     *
     * @param id id
     * @return data
     */
    public CrmInfoNumVO num(Integer id);

    /**
     * 查询文件列表
     * @param id id
     * @return file
     */
    public List<FileEntity> queryFileList(Integer id);

    /**
     * 查询详情信息
     * @param receivablesId 回款ID
     * @return data
     */
    public List<CrmModelFiledVO> information(Integer receivablesId);

    void updateInformation(CrmUpdateInformationBO updateInformationBO);

    BasePage<JSONObject> queryListByContractId(BasePage<JSONObject> page, Integer contractId, String conditions);
}
