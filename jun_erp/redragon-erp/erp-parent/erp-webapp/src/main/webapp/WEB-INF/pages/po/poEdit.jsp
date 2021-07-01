<%--

    Copyright 2020-2021 redragon.dongbin
 
    This file is part of redragon-erp/赤龙ERP.

    redragon-erp/赤龙ERP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License, or
    (at your option) any later version.

    redragon-erp/赤龙ERP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with redragon-erp/赤龙ERP.  If not, see <https://www.gnu.org/licenses/>.
	
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%-- 导入面包屑 --%>
<jsp:include page="../common/nav.jsp"></jsp:include>

<div class="wrapper wrapper-content animated fadeInRight">

	<%-- 导入提示信息框 --%>
    <c:if test="${hint!=null&&hint!=''}">
   		<jsp:include page="../common/alert/alert.jsp">
   			<jsp:param value="${hint}" name="alertType"/>
   			<jsp:param value="${alertMessage}" name="alertMessage"/>
   		</jsp:include>
    </c:if>

	<div class="row">
		<div class="col-lg-12">
			<div class="ibox ">
				<div class="ibox-title btn-success btn-outline panel-success collapse-link" title="展开/收起">
					<h5>采购订单头信息&nbsp;<span style="color: black;">（<i class="fa fa-tag"></i>${requestScope.approveStatusMap[requestScope.poHead.approveStatus]}）</span></h5>
					<div class="ibox-tools">
						<%-- 
                        <button type="button" class="btn btn-sm btn-white text-success" title="工具栏"> <i class="fa fa-list btn-redragon-tools"></i> </button>
                        <button type="button" class="btn btn-sm btn-white text-success" title="工具栏"> <i class="fa fa-print btn-redragon-tools"></i> </button>
                        <button type="button" class="btn btn-sm btn-white text-success" title="工具栏"> <i class="fa fa-cogs btn-redragon-tools"></i> </button>
                        --%>                
						<a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
                            <button type="button" class="btn btn-sm btn-white text-success" title="工具栏"> <i class="fa fa-wrench btn-redragon-tools"></i> 工具栏</button>
                        </a>
                        <ul class="dropdown-menu dropdown-user">
                        	<li><a href="javascript:void(0)" title="选择变更历史版本">
	                        		<strong>查看变更历史版本：</strong>&nbsp;
	                        		<select id="modifyHistorySelect">
	                        			<c:forEach begin="1" end="${requestScope.poHead.version==null?1:requestScope.poHead.version-1}" var="version">
	                        				<option value="${version}">v${version}</option>
	                        			</c:forEach>
	                        		</select>&nbsp;&nbsp;<i id="openModifyHistory" class="fa fa-arrow-circle-right text-default fa-lg btn-redragon-tools" title="打开版本"></i>
                        		</a>
                        	</li>
                        	<script>
                        	$(document).ready(function(){
                        		$("#modifyHistorySelect").click(function(e){
                        			return false;
                        		});
                        		
                        		$("#openModifyHistory").click(function(e){
                        			if($("#modifyHistorySelect").val()!=""&&$("#modifyHistorySelect").val()!=null){
                        				window.open(window.location.href+"&version="+$("#modifyHistorySelect").val());
                        			}else{
                        				redragonJS.alert("当前单据无变更历史版本");
                        			}
                        		});
                        	});
                        	</script>
                        </ul>
                        &nbsp;
						<i class="fa fa-chevron-up"></i>
					</div>
				</div>

				<div class="ibox-content border-bottom" style="padding-bottom: 0px;">
					<form id="form" action="web/poHead/editPoHead" method="post">
						<div class="form-group  row">
							<label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>订单编码</strong></label>
							<div class="col-sm-4">
								<input id="poHeadCode" name="poHeadCode" type="text" class="form-control" value="${requestScope.poHead.poHeadCode}">
							</div>
							
							<label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>订单名称</strong></label>
							<div class="col-sm-4">
								<input id="poName" name="poName" type="text" class="form-control" value="${requestScope.poHead.poName}">
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						
						<div class="form-group row">
	                        <label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>订单类型</strong></label>
	
	                        <div class="col-sm-4">
		                        <select class="form-control" name="poType" id="poType">
		                        	<option value="" selected="selected">请选择...</option>
		                        	<c:forEach items="${requestScope.poTypeMap}" var="poType">
		                        		<option value="${poType.key}">${poType.value}</option>
		                        	</c:forEach>
		                        </select>
	                        </div>
	                    
							<label class="col-sm-2 col-form-label"><strong>所属项目</strong></label>
							<div class="col-sm-4">
								<select id="projectCode" name="projectCode" class="select2 form-control">
		                        	<option value="" selected="selected">请选择...</option>
		                        	<c:forEach items="${requestScope.projectMap}" var="project">
		                        		<option value="${project.key}">${project.value}</option>
		                        	</c:forEach>
		                        </select>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						
						<div class="form-group  row">
							<label class="col-sm-2 col-form-label"><strong>订单备注</strong></label>
							<div class="col-sm-10">
								<textarea id="poDesc" name="poDesc" rows="3" class="form-control">${requestScope.poHead.poDesc}</textarea>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						
						<div class="form-group row">
	                        <label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>供应商</strong></label>
	
	                        <div class="col-sm-4">
		                        <select class="select2 form-control" name="vendorCode" id="vendorCode">
		                        	<option value="" selected="selected">请选择...</option>
		                        	<c:forEach items="${requestScope.vendorMap}" var="vendor">
		                        		<option value="${vendor.key}">${vendor.value}</option>
		                        	</c:forEach>
		                        </select>
	                        </div>

	                        <label class="col-sm-2 col-form-label"><strong>订单金额</strong></label>
	
	                        <div class="col-sm-4 input-group">
		                        <input id="amount" type="text" class="form-control" value="${requestScope.poHead.amount}" readonly="readonly">
	                        	<span class="input-group-addon">(元)</span>
	                        </div>
	                    </div>
	                    <div class="hr-line-dashed"></div>
	                    
	                    <div class="form-group row">
	                        <label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>币种</strong></label>
	
	                        <div class="col-sm-4">
	                        	<select class="form-control" name="currencyCode" id="currencyCode">
		                        	<c:forEach items="${requestScope.currencyTypeMap}" var="currencyType">
		                        		<option value="${currencyType.key}">${currencyType.value}</option>
		                        	</c:forEach>
		                        </select>
	                        </div>
	                    
							<label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>预付款金额</strong></label>
							<div class="col-sm-4 input-group">
								<input id="prepayAmount" name="prepayAmount" type="text" class="form-control" value="${requestScope.poHead.prepayAmount}">
								<span class="input-group-addon">(元)</span>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						
						<div class="form-group row">
	                        <label class="col-sm-2 col-form-label"><strong>订单开始时间</strong></label>
	
	                        <div class="col-sm-4">
	                        	<div class="input-group date">
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
									<input id="startDate" name="startDate" type="text" class="form-control" value="<fmt:formatDate value="${requestScope.poHead.startDate}" pattern="yyyy-MM-dd"/>" autocomplete="off">
								</div>
	                        </div>
	                    
							<label class="col-sm-2 col-form-label"><strong>订单结束时间</strong></label>
							<div class="col-sm-4">
								<div class="input-group date">
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
									<input id="endDate" name="endDate" type="text" class="form-control" value="<fmt:formatDate value="${requestScope.poHead.endDate}" pattern="yyyy-MM-dd"/>" autocomplete="off">
								</div>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
	                    
						<div class="form-group row">
							<label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>签订时间</strong></label>
	
	                        <div class="col-sm-4">
	                        	<div class="input-group date">
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
									<input id="signDate" name="signDate" type="text" class="form-control" value="<fmt:formatDate value="${requestScope.poHead.signDate}" pattern="yyyy-MM-dd"/>" autocomplete="off">
								</div>
	                        </div>
	                        
	                        <label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>状态</strong></label>
	
	                        <div class="col-sm-4">
	                        	<input type="text" class="form-control" value="${requestScope.poStatusMap[requestScope.poHead.status]}" readonly="readonly">
	                        </div>
	                    </div>
	                    <div class="hr-line-dashed"></div>
	                    
	                    <%--bbc 暂时取消 接收状态 付款状态
	                    <div class="form-group row">
							<label class="col-sm-2 col-form-label"><strong>接收状态</strong></label>
	
	                        <div class="col-sm-4">
	                        	<input type="text" class="form-control" value="${requestScope.receiveStatusMap[requestScope.poHead.receiveStatus]}" readonly="readonly">
	                        </div>
	                        
	                        <label class="col-sm-2 col-form-label"><strong>付款状态</strong></label>
	
	                        <div class="col-sm-4">
	                        	<input type="text" class="form-control" value="${requestScope.paymentStatusMap[requestScope.poHead.paymentStatus]}" readonly="readonly">
	                        </div>
	                    </div>
	                    <div class="hr-line-dashed"></div>
	                    --%>
	                    
	                    <div class="form-group row">
							<label class="col-sm-2 col-form-label"><strong>计税类型</strong></label>
	
	                        <div class="col-sm-4">
		                        <select class="form-control" name="taxType" id="taxType">
		                        	<option value="" selected="selected">请选择...</option>
		                            <c:forEach items="${requestScope.taxTypeMap}" var="taxType">
		                        		<option value="${taxType.key}">${taxType.value}</option>
		                        	</c:forEach>
		                        </select>
	                        </div>
	                        
	                        <label class="col-sm-2 col-form-label"><strong>税率</strong></label>
	
	                        <div class="col-sm-4 input-group">
	                        	<input id="taxPercent" name="taxPercent" type="text" class="form-control" value="${requestScope.poHead.taxPercent}">
	                        	<span class="input-group-addon">%</span>
	                        </div>
	                    </div>
	                    <div class="hr-line-dashed"></div>
	                    
	                    <div class="form-group  row">
							<label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>采购员</strong></label>
							<div class="col-sm-4">
								<input type="text" class="form-control" value="${requestScope.poHead.staffName}" readonly="readonly">
							</div>
							
							<label class="col-sm-2 col-form-label"><span class="text-danger">*</span><strong>采购部门</strong></label>
							<div class="col-sm-4">
								<input type="text" class="form-control" value="${requestScope.poHead.departmentName}" readonly="readonly">
							</div>
						</div>
						<div class="hr-line-dashed"></div>

						<div class="form-group row">
							<div class="col-sm-12 col-sm-offset-2 text-right">
								<button class="btn btn-white btn-lg" type="button" onclick="window.location.href='web/poHead/getPoHeadList'">返回</button>&nbsp;
								<c:if test="${param.poHeadCode==null||param.poHeadCode==''||requestScope.poHead.approveStatus=='UNSUBMIT'||requestScope.poHead.approveStatus=='REJECT' }">
									<button class="ladda-button ladda-button-demo btn btn-success btn-lg" data-style="expand-right">&nbsp;&nbsp;保存&nbsp;&nbsp;<i class="fa fa-save"></i></button>
								</c:if>
								
								<c:if test="${param.poHeadCode!=null&&param.poHeadCode!=''}">
									<c:if test="${requestScope.poHead.approveStatus=='UNSUBMIT'||requestScope.poHead.approveStatus=='REJECT' }">
										<button id="submitApproveButton" class="btn btn-primary btn-lg" type="button">&nbsp;&nbsp;提交&nbsp;&nbsp;<i class="fa fa-arrow-circle-right"></i></button>&nbsp;
									</c:if>
									<c:if test="${requestScope.poHead.approveStatus=='SUBMIT' }">
										<button class="btn btn-warning btn-lg btn-redragon-approve" type="button" onclick="approveData()">&nbsp;&nbsp;审核通过&nbsp;&nbsp;<i class="fa fa-check-circle"></i></button>&nbsp;
										<button class="btn btn-danger btn-lg btn-redragon-approve" type="button" onclick="window.location.href='web/poHead/updateApproveStatus?code=${requestScope.poHead.poHeadCode}&approveStatus=REJECT'">&nbsp;&nbsp;驳回&nbsp;&nbsp;<i class="fa fa-times-circle"></i></button>&nbsp;
									</c:if>
									<c:if test="${requestScope.poHead.approveStatus=='APPROVE' }">
										<button class="btn btn-success btn-lg" type="button" onclick="alterData()">&nbsp;&nbsp;变更&nbsp;&nbsp;<i class="fa fa-retweet"></i></button>&nbsp;
									</c:if>
								</c:if>
							</div>
						</div>
						
						<input type="hidden" id="status" name="status" value="${requestScope.poHead.status}">
						<input type="hidden" id="receiveStatus" name="receiveStatus" value="${requestScope.poHead.receiveStatus}">
						<input type="hidden" id="paymentStatus" name="paymentStatus" value="${requestScope.poHead.paymentStatus}">
						<input type="hidden" id="staffCode" name="staffCode" value="${requestScope.poHead.staffCode}">
						<input type="hidden" id="departmentCode" name="departmentCode" value="${requestScope.poHead.departmentCode}">
						<input type="hidden" id="poHeadId" name="poHeadId" value="${requestScope.poHead.poHeadId}">
						<input type="hidden" name="createdDate" value="${requestScope.poHead.createdDate}">
						<input type="hidden" name="createdBy" value="${requestScope.poHead.createdBy}">
					</form>
				</div>

				<!-- tab 开始 -->
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox ">
							<div class="ibox-title btn-info btn-outline panel-info">
								<h5>采购订单行信息</h5>
								<div class="ibox-tools">
								</div>
							</div>

							<div class="ibox-content border-bottom" style="padding-bottom: 0px;">
								<div class="tabs-container">
									<ul class="nav nav-tabs">
										<li><a class="nav-link active" data-toggle="tab" href="#lineTab" onclick="getLineTab('${requestScope.poHead.poHeadCode}')">订单行</a></li>
									</ul>
									<div id="tabDiv" class="tab-content">
									</div>
								</div>
							</div>
							
						</div>
					</div>
				</div>
				<!-- tab 结束 -->
				
			</div>
		</div>
	</div>
</div>

<!-- select2 -->
<script src="js/plugins/select2/select2.full.min.js"></script>

<script>
	$(document).ready(function() {
		//初始化poType
		if("${requestScope.poHead.poType}"!=""){
			$("#poType").val("${requestScope.poHead.poType}");
		}
		//初始化projectCode
		if("${requestScope.poHead.projectCode}"!=""){
			$("#projectCode").val("${requestScope.poHead.projectCode}");
		}
		//初始化vendorCode
		if("${requestScope.poHead.vendorCode}"!=""){
			$("#vendorCode").val("${requestScope.poHead.vendorCode}");
		}
		//初始化currencyCode
		if("${requestScope.poHead.currencyCode}"!=""){
			$("#currencyCode").val("${requestScope.poHead.currencyCode}");
		}
		//初始化taxType
		if("${requestScope.poHead.taxType}"!=""){
			$("#taxType").val("${requestScope.poHead.taxType}");
		}
		//初始化poHeadCode只读
		if("${requestScope.poHead.poHeadCode}"!=""){
			$("#poHeadCode").prop("readonly", true);
		}
		
		//初始化select2
		$('.select2').select2({width: "100%"});
		
		//设置日期插件
		$('#startDate').datepicker({
			todayBtn : "linked",
			keyboardNavigation : true,
			forceParse : true,
			calendarWeeks : false,
			autoclose : true,
			format: 'yyyy-mm-dd',
			language: 'zh-CN',
		});
		
		$('#endDate').datepicker({
			todayBtn : "linked",
			keyboardNavigation : true,
			forceParse : true,
			calendarWeeks : false,
			autoclose : true,
			format: 'yyyy-mm-dd',
			language: 'zh-CN',
		});
		
		$('#signDate').datepicker({
			todayBtn : "linked",
			keyboardNavigation : true,
			forceParse : true,
			calendarWeeks : false,
			autoclose : true,
			format: 'yyyy-mm-dd',
			language: 'zh-CN',
		});
	
		//提交审批
		$("#submitApproveButton").click(function(){
			var submitFlag = "Y";
				
			if($.isNumeric($("#prepayAmount").val())&&$.isNumeric($("#amount").val())){
				if(parseFloat($("#prepayAmount").val())>parseFloat($("#amount").val())){
					submitFlag = "N";
					redragonJS.alert("预付款金额不能大于订单金额");
				}
			}
			
			if($("#lineTab tbody tr").length==0){
				submitFlag = "N";
				redragonJS.alert("至少新增一行后，才能提交数据");
			}
			
			//提交
			if(submitFlag=="Y"){
				window.location.href='web/poHead/updateApproveStatus?code=${requestScope.poHead.poHeadCode}&approveStatus=SUBMIT';
			}
		});
		
	
	
	
		//表单提交
		var l = $('.ladda-button-demo').ladda();

		l.click(function() {
			$("#form").valid();
			//l.ladda('stop');
		});

		$("#form").validate({
			rules : {
				poHeadCode : {
					required : true,
					isCode : true,
				},
				poName : {
					required : true,
				},
				poType : {
					required : true,
				},
				vendorCode : {
					required : true,
				},
				currencyCode : {
					required : true,
				},
				signDate : {
					required : true,
				},
			},
			submitHandler: function(form) {
				var submitFlag = "Y";
				
				if($.isNumeric($("#prepayAmount").val())&&$.isNumeric($("#amount").val())){
					if(parseFloat($("#prepayAmount").val())>parseFloat($("#amount").val())){
						submitFlag = "N";
						redragonJS.alert("预付款金额不能大于订单金额");
					}
				}
				
				//提交
				if(submitFlag=="Y"){
					l.ladda('start');
		        	form.submit();
				}
				
		    }
		});
		
		//初始化tab
		getLineTab("${requestScope.poHead.poHeadCode}");
	});
	
	//获取行tab
	function getLineTab(code){
		$.ajax({
			type: "post",
			url: "web/poLine/getPoLineList",
			data: {"poHeadCode": code},
			async: false,
			dataType: "html",
			cache: false,
			success: function(data){
				if(data!=""){
					$("#tabDiv").html(data);
					$("#lineTab").addClass("active");
					//隐藏保存按钮
					if(($("#poHeadId").val()!=null&&$("#poHeadId").val()!=""&&"${requestScope.poHead.approveStatus}"!="UNSUBMIT"&&"${requestScope.poHead.approveStatus}"!="REJECT")||
						$("#poHeadId").val()==null||$("#poHeadId").val()==""){
						$("#tabDiv .btn").hide();
					}
					initControlAuth();
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				redragonJS.alert(textStatus);
			}
		});
	}
	
	//审批通过
	function approveData(){
		redragonJS.confirm("确认审批通过？", function(){
			window.location.href='web/poHead/updateApproveStatus?code=${requestScope.poHead.poHeadCode}&approveStatus=APPROVE';
		});
	}
	
	//数据变更
	function alterData(){
		redragonJS.confirm("确认变更数据？数据变更后将产生变更历史信息！", function(){
			window.location.href='web/poHead/updateApproveStatus?code=${requestScope.poHead.poHeadCode}&approveStatus=UNSUBMIT';
		});
	}
</script>