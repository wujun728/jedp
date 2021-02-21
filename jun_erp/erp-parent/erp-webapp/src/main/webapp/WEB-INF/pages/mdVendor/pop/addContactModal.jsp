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

<div class="modal" id="addContactDiv" tabindex="-1" role="dialog" aria-hidden="true">

	<div class="modal-dialog" role="document">

		<div class="modal-content animated bounceInRight">

			<div class="modal-header">
				<h4 class="modal-title">联系人编辑</h4>
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
			
				<form id="cform">
					<div class="form-group row">
						<label class="col-sm-3 col-form-label"><span class="text-danger">*</span>联系人</label>
						<div class="col-sm-9">
							<input id="contactName" name="contactName" type="text" class="form-control" value="${requestScope.mdVendorContact.contactName}">
						</div>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group row">
						<label class="col-sm-3 col-form-label"><span class="text-danger">*</span>联系电话</label>
						<div class="col-sm-9">
							<input id="contactTelephone" name="contactTelephone" type="text" class="form-control" value="${requestScope.mdVendorContact.contactTelephone}">
						</div>
					</div>
					<div class="hr-line-dashed"></div>
					
					<div class="form-group row">
						<label class="col-sm-3 col-form-label">联系人岗位</label>
						<div class="col-sm-9">
							<input id="contactPosition" name="contactPosition" type="text" class="form-control" value="${requestScope.mdVendorContact.contactPosition}">
						</div>
					</div>
					<div class="hr-line-dashed"></div>

					<div class="form-group row">
						<label class="col-sm-3 col-form-label"><span class="text-danger">*</span>状态</label>

						<div class="col-sm-9">
							<select class="form-control m-b" name="status" id="status">
								<option value="Y" selected="selected">有效</option>
								<option value="N">无效</option>
							</select>
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
					
					<input type="hidden" id="vendorCode" name="customerCode" value="${param.vendorCode}"> 
					<input type="hidden" id="contactId" name="contactId" value="${requestScope.mdVendorContact.contactId}"> 
					<input type="hidden" id="createdDate" name="createdDate" value="${requestScope.mdVendorContact.createdDate}"> 
					<input type="hidden" id="createdBy" name="createdBy" value="${requestScope.mdVendorContact.createdBy}">
				</form>
			</div>

		</div>

	</div>

</div>

<script>
	$(document).ready(function() {
		//初始化status
		if("${requestScope.mdVendorContact.status}"!=""){
			$("#status").val("${requestScope.mdVendorContact.status}");
		}
		
		var l = $('.ladda-button-demo').ladda();

		l.click(function() {
			$("#cform").valid();
			//l.ladda('stop');
		});

		$("#cform").validate({
			rules : {
				contactName : {
					required : true,
				},
				contactTelephone : {
					required : true,
				}
			},
			submitHandler: function(form) {
				l.ladda('start');
				editContact();
		    }
		});
		
	});
	
	//异步编辑联系人
	function editContact(){
		redragonJS.loading("tabDiv");
	
		$.ajax({
			type: "post",
			url: "web/mdVendorContact/editMdVendorContact",
			data: JSON.stringify({"contactName": $("#contactName").val(), "contactTelephone": $("#contactTelephone").val(), "contactPosition": $("#contactPosition").val(), 
								"status": $("#status").val(), "contactId": $("#contactId").val(), "createdDate": $("#createdDate").val(), "createdBy": $("#createdBy").val(), 
								"vendorCode": $("#vendorCode").val()}),
			async: false,
			dataType: "html",
			contentType: "application/json",
			cache: false,
			success: function(data){
				redragonJS.removeLoading("tabDiv");
				$('#addContactDiv').modal('hide');
				$('.ladda-button-demo').ladda('stop');
				
				var json = JSON.parse(data);
				if(json.result=="success"){
					getContactTab($("#vendorCode").val());
				}else{
					redragonJS.alert("编辑联系人错误");
				}
				
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				redragonJS.alert(textStatus);
			}
		});
	}
</script>
