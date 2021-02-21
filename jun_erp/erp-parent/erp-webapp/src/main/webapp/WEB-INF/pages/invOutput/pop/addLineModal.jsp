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
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<div class="modal" id="addLineDiv" tabindex="-1" role="dialog" aria-hidden="true">

	<div class="modal-dialog" role="document">

		<div class="modal-content animated bounceInRight">

			<div class="modal-header">
				<h4 class="modal-title">出库行编辑</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			</div>

			<div class="modal-body" style="padding-bottom: 20px;">
				<%-- 导入提示信息框 --%>
			    <c:if test="${hint!=null&&hint!=''}">
			   		<jsp:include page="../../common/alert/alert.jsp">
			   			<jsp:param value="${hint}" name="alertType"/>
			   			<jsp:param value="${alertMessage}" name="alertMessage"/>
			   		</jsp:include>
			    </c:if>
			
				<form id="lineForm">
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">物料编码</label>
						<div class="col-sm-9">
							<input id="materialCode" type="text" class="form-control" value="${requestScope.invOutputLine.materialCode}" readonly="readonly">
						</div>
					</div>
					<div class="hr-line-dashed"></div>
					
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">物料</label>
						<div class="col-sm-9">
							<input id="materialName" type="text" class="form-control" value="${requestScope.invOutputLine.materialName}" readonly="readonly">
						</div>
					</div>
					<div class="hr-line-dashed"></div>
					
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">规格型号</label>
						<div class="col-sm-9">
							<input id="materialStandard" type="text" class="form-control" value="${requestScope.invOutputLine.materialStandard}" readonly="readonly">
						</div>
					</div>
					<div class="hr-line-dashed"></div>
				
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">单价</label>
						<div class="col-sm-9 input-group">
							<input id="price" type="text" class="form-control" value="${requestScope.invOutputLine.price}" readonly="readonly">
							<span class="input-group-addon">(元)</span>
						</div>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group row">
						<label class="col-sm-3 col-form-label">数量</label>
						<div class="col-sm-9 input-group">
							<input id="quantity" type="text" class="form-control" value="${requestScope.invOutputLine.quantity}" readonly="readonly">
							<span class="input-group-addon">(${requestScope.invOutputLine.unit})</span>
						</div>
					</div>
					<div class="hr-line-dashed"></div>
					
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">已出库数量</label>
						<div class="col-sm-9 input-group">
							<input id="outputedQuantity" type="text" class="form-control" value="${requestScope.invOutputLine.outputedQuantity}" readonly="readonly">
							<span class="input-group-addon">(${requestScope.invOutputLine.unit})</span>
						</div>
					</div>
					<div class="hr-line-dashed"></div>
					
					<div class="form-group row" style="margin-bottom: 0px;">
						<label class="col-sm-3 col-form-label"><span class="text-danger">*</span>出库数量</label>
						<div class="col-sm-9 input-group">
							<input id="outputQuantity" name="outputQuantity" type="text" class="form-control" value="${requestScope.invOutputLine.outputQuantity}">
						</div>
					</div>
					<div class="form-group row" style="margin-bottom: 0px;">
						<label class="col-sm-3 col-form-label"></label>
						<div class="col-sm-9 input-group" style="color: red;">
							库存数量：<label id="stockNumber" class="help-block m-b-none">${requestScope.stockNumber}</label>
						</div>
					</div>
					<div class="hr-line-dashed" style="margin-top: 0px;"></div>
					
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">出库摘要</label>
						<div class="col-sm-9">
							<input id="outputLineMemo" name="memo" type="text" class="form-control" value="${requestScope.invOutputLine.memo}">
						</div>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group row m-b-none">
						<div class="col-sm-12 col-sm-offset-2 text-right">
							<button class="btn btn-white btn-lg" type="button"
								data-dismiss="modal">返回</button>
							&nbsp;
							<button
								class="ladda-button ladda-button-demo btn btn-primary btn-lg"
								data-style="expand-right">
								&nbsp;&nbsp;确定&nbsp;&nbsp;<i class="fa fa-check-square-o"></i>
							</button>
						</div>
					</div>
					
					<input type="hidden" id="outputHeadCode" name="outputHeadCode" value="${param.outputHeadCode}"> 
					<input type="hidden" id="outputLineId" name="outputLineId" value="${requestScope.invOutputLine.outputLineId}"> 
					<input type="hidden" id="outputLineCode" name="outputLineCode" value="${requestScope.invOutputLine.outputLineCode}"> 
					<input type="hidden" id="outputSourceLineCode" name="outputSourceLineCode" value="${requestScope.invOutputLine.outputSourceLineCode}"> 
					<input type="hidden" id="createdDate" name="createdDate" value="${requestScope.invOutputLine.createdDate}"> 
					<input type="hidden" id="createdBy" name="createdBy" value="${requestScope.invOutputLine.createdBy}">
				</form>
			</div>

		</div>

	</div>

</div>


<script>
	$(document).ready(function() {
		var l = $('.ladda-button-demo').ladda();

		l.click(function() {
			$("#lineForm").valid();
			//l.ladda('stop');
		});

		$("#lineForm").validate({
			rules : {
				outputQuantity : {
					required : true,
					gtZero : true,
				},
			},
			submitHandler: function(form) {
				var submitFlag = "Y"
				
				//验证发票行数量 
				var quantity = parseFloat($("#quantity").val());
				var outputedQuantity = parseFloat($("#outputedQuantity").val());
				var outputQuantity = parseFloat($("#outputQuantity").val());
				if(outputQuantity>redragonJS.numberSub(quantity, outputedQuantity)){
					submitFlag = "N";
					redragonJS.alert("出库数量("+outputQuantity+")不能大于销售订单行数量("+quantity+")-已出库数量("+outputedQuantity+")");
				}
			
				//表单提交
				if(submitFlag=="Y"){
					//库存验证
					if(parseFloat($("#stockNumber").text())>0&&parseFloat($("#outputQuantity").val())<=parseFloat($("#stockNumber").text())){
						l.ladda('start');
						editLine();
					}else{
						redragonJS.alert("出库数量不能大于库存数量");
					}
				}
		    }
		});
		
	});
	
	//异步编辑付款行
	function editLine(){
		redragonJS.loading("ibox-content1");
	
		$.ajax({
			type: "post",
			url: "web/invOutputLine/editInvOutputLine",
			data: {"outputQuantity": $("#outputQuantity").val(), "memo": $("#outputLineMemo").val(), "outputHeadCode": $("#outputHeadCode").val(), "outputLineId": $("#outputLineId").val(),
				   "outputLineCode": $("#outputLineCode").val(), "outputSourceLineCode": $("#outputSourceLineCode").val(), "materialCode": $("#materialCode").val(),
				   "createdDate": $("#createdDate").val(), "createdBy": $("#createdBy").val()},
			async: false,
			dataType: "html",
			cache: false,
			success: function(data){
				redragonJS.removeLoading("ibox-content1");
				$('#addLineDiv').modal('hide');
				$('.ladda-button-demo').ladda('stop');
				
				var json = JSON.parse(data);
				if(json.result=="success"){
					getLineTab($("#outputHeadCode").val());
				}else{
					redragonJS.alert("编辑行信息错误");
				}
				
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				redragonJS.alert(textStatus);
			}
		});
	}
	
</script>
