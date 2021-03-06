package org.springrain.weixin.sdk.xcx.api;

import java.util.List;

import org.springrain.weixin.sdk.common.api.IWxXcxConfig;
import org.springrain.weixin.sdk.common.exception.WxErrorException;
import org.springrain.weixin.sdk.mp.bean.template.WxMpTemplate;
import org.springrain.weixin.sdk.mp.bean.template.WxMpTemplateIndustry;
import org.springrain.weixin.sdk.mp.bean.template.WxMpTemplateMessage;

/**
 * <pre>
 * 模板消息接口
 * http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
 * Created by springrain on 2017/1/8.
 * @author Wujun
 * @author Wujun
 * </pre>
 */
public interface IWxXcxTemplateMsgService {

  /**
   * <pre>
   * 设置所属行业
   * 官方文档中暂未告知响应内容
   * 详情请见：http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
   * </pre>
   *
   * @return 是否成功
   */
  boolean setIndustry(IWxXcxConfig wxxcxconfig,WxMpTemplateIndustry wxMpIndustry) throws WxErrorException;

  /***
   * <pre>
   * 获取设置的行业信息
   * 详情请见：http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
   * </pre>
   *
   * @return wxMpIndustry
   */
  WxMpTemplateIndustry getIndustry(IWxXcxConfig wxxcxconfig) throws WxErrorException;

  /**
   * <pre>
   * 发送模板消息
   * 详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
   * </pre>
   *
   * @return 消息Id
   */
  String sendTemplateMsg(IWxXcxConfig wxxcxconfig,WxMpTemplateMessage templateMessage) throws WxErrorException;

  /**
   * <pre>
   * 获得模板ID
   * 从行业模板库选择模板到帐号后台，获得模板ID的过程可在MP中完成
   * 详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
   * 接口地址格式：https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN
   * </pre>
   * @param shortTemplateId 模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式
   * @return templateId 模板Id
   */
  String addTemplate(IWxXcxConfig wxxcxconfig,String shortTemplateId) throws WxErrorException;

  /**
   * <pre>
   * 获取模板列表
   * 获取已添加至帐号下所有模板列表，可在MP中查看模板列表信息，为方便第三方开发者，提供通过接口调用的方式来获取帐号下所有模板信息
   * 详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
   * 接口地址格式：https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @return templateId 模板Id
   */
  List<WxMpTemplate> getAllPrivateTemplate(IWxXcxConfig wxxcxconfig) throws WxErrorException;

  /**
   * <pre>
   * 删除模板
   * 删除模板可在MP中完成，为方便第三方开发者，提供通过接口调用的方式来删除某帐号下的模板
   * 详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
   * 接口地址格式：https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @param templateId 模板Id
   */
  boolean delPrivateTemplate(IWxXcxConfig wxxcxconfig,String templateId) throws WxErrorException;
}
