<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="sy.util.base.SecurityUtil"%>
<%
	//考勤管理
	String contextPath = request.getContextPath();
	SecurityUtil securityUtil = new SecurityUtil(session);
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="../../inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	var addFun = function() {
		var dialog = parent.sy.modalDialog({
			title : '添加考勤信息',
			url : sy.contextPath + '/securityJsp/app/MedicalAttendForm.jsp',
			buttons : [ {
				text : '添加',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var showFun = function(id) {
		var dialog = parent.sy.modalDialog({
			title : '查看考勤信息',
			url : sy.contextPath + '/securityJsp/app/custDeptForm.jsp?id=' + id
		});
	};
	var editFun = function(id) {
		var dialog = parent.sy.modalDialog({
			title : '编辑考勤信息',
			url : sy.contextPath + '/securityJsp/app/custDeptForm.jsp?id=' + id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var removeFun = function(id) {
		parent.$.messager.confirm('询问', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(sy.contextPath + '/app/cust-dept!delete.sy', {
					deptId : id
				}, function() {
					grid.treegrid('reload');
				}, 'json');
			}
		});
	};
	var grantFun = function(id) {
		var dialog = parent.sy.modalDialog({
			title : '考勤授权',
			url : sy.contextPath + '/securityJsp/base/SyorganizationGrant.jsp?id=' + id,
			buttons : [ {
				text : '授权',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var redoFun = function() {
		var node = grid.treegrid('getSelected');
		if (node) {
			grid.treegrid('expandAll', node.id);
		} else {
			grid.treegrid('expandAll');
		}
	};
	var undoFun = function() {
		var node = grid.treegrid('getSelected');
		if (node) {
			grid.treegrid('collapseAll', node.id);
		} else {
			grid.treegrid('collapseAll');
		}
	};
	$(function() {
		grid = $('#grid').treegrid({
			title : '考勤数据',
			url : sy.contextPath + '/app/medical-attend!grid.sy',
			idField : 'deptId',
			rownumbers : true,
			pagination : false,
			sortName : 'createTime',
			sortOrder : 'asc',
			frozenColumns : [ [  {
				width : '100',
				title : '考勤ID',
				field : 'deptId'
			} ,{
				width : '100',
				title : '考勤名称',
				field : 'deptName'
			} ] ],
			columns : [ [
			   {
				width : '150',
				title : '考勤名称',
				field : 'customerInfo.customerId',
				formatter : function(value, row) {
					var str = '';
					return row.customerInfo.customerName + "["+row.customerInfo.customerId+"]";
				}
			},
			{
				width : '150',
				title : '图标名称',
				field : 'iCONCLS'
			}, {
				width : '150',
				title : '考勤编码',
				field : 'deptCode'
			}, {
				width : '200',
				title : '考勤地址',
				field : 'deptAddr'
			}, {
				width : '200',
				title : '考勤主管',
				field : 'deptMaster'
			}, {
				width : '150',
				title : '创建时间',
				field : 'createTime',
				hidden : true
			}, {
				width : '150',
				title : '修改时间',
				field : 'fireTime',
				hidden : true
			}, {
				width : '60',
				title : '排序',
				field : 'seq',
				hidden : true
			}, {
				title : '操作',
				field : 'action',
				width : '250',
				formatter : function(value, row) {
					var str = '';
					<%if (securityUtil.havePermission("/app/cust-dept!getById")) {%>
						var bt = systool.createFuncButton('ext-icon-note','查看','查看考勤资料','showFun(\'{0}\');');
						str +=  sy.formatString(bt,row.deptId);
					<%}%>
					<%if (securityUtil.havePermission("/app/cust-dept!update")) {%>
					var bt = systool.createFuncButton('ext-icon-note_edit','编辑','编辑考勤资料','editFun(\'{0}\');');
					str +=  sy.formatString(bt,row.deptId);	
					<%}%>
					<%if (securityUtil.havePermission("/app/cust-dept!grant")) {%>
						var bt = systool.createFuncButton('ext-icon-group_key','授权','授权考勤','grantFun(\'{0}\');');
						str +=  sy.formatString(bt,row.deptId);	
					<%}%>
					<%if (securityUtil.havePermission("/app/cust-dept!delete")) {%>
					var bt = systool.createFuncButton('ext-icon-note_delete','删除','删除考勤','removeFun(\'{0}\');');
					str +=  sy.formatString(bt,row.deptId);		
					<%}%>
					return str;
				}
			} ] ],
			toolbar : '#toolbar',
			onBeforeLoad : function(row, param) {
				parent.$.messager.progress({
					text : '数据加载中....'
				});
			},
			onLoadSuccess : function(row, data) {
				$('.iconImg').attr('src', sy.pixel_0);
				parent.$.messager.progress('close');
			}
		});
	});
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
		<table>
		<tr>
				<td>
					<form id="searchForm">
						<table>
							<tr>
								<td>考勤名称</td>
								<td><input name="QUERY_t#deptName_S_LK" style="width: 180px;" /></td>
								<td>考勤地址</td>
								<td><input name="QUERY_t#deptAddr_S_LK" style="width: 180px;" /></td>
								<td>创建时间</td>
								<td><input name="QUERY_t#createTime_D_GE" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" style="width: 120px;" />-<input name="QUERY_t#createTime_D_LE" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" style="width: 120px;" /></td>
								<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom',plain:true" onclick="grid.treegrid('load',sy.serializeObject($('#searchForm')));">过滤</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom_out',plain:true" onclick="$('#searchForm input').val('');grid.datagrid('load',{});">重置过滤</a></td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
			<tr>
				<%if (securityUtil.havePermission("/app/cust-dept!save")) {%>
				<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-note_add',plain:true" onclick="addFun();">添加</a></td>
				<%}%>
				<td><div class="datagrid-btn-separator"></div></td>
				<td><a onclick="redoFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'ext-icon-resultset_next'">展开</a><a onclick="undoFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'ext-icon-resultset_previous'">折叠</a></td>
				<td><div class="datagrid-btn-separator"></div></td>
				<td><a onclick="grid.treegrid('reload');" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'ext-icon-arrow_refresh'">刷新</a></td>
			</tr>
		</table>
	</div>
	 
	
	<div data-options="region:'center',fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
</body>
</html>