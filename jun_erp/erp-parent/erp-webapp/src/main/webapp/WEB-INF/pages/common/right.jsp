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

<div id="right-sidebar">
	<div class="sidebar-container">

		<ul class="nav nav-tabs navs-3">
			<%-- 
			<li><a class="nav-link active" data-toggle="tab" href="#tab-1">
					Notes </a></li>
			<li><a class="nav-link" data-toggle="tab" href="#tab-2">
					Projects </a></li>
			--%>		
			<li><a class="nav-link" data-toggle="tab" href="#tab-3"> <i
					class="fa fa-gear"></i>配置
			</a></li>
		</ul>

		<div class="tab-content">

			<%-- 

			<div id="tab-1" class="tab-pane active">

				<div class="sidebar-title">
					<h3>
						<i class="fa fa-comments-o"></i> Latest Notes
					</h3>
					<small><i class="fa fa-tim"></i> You have 10 new message.</small>
				</div>

				<div>

					<div class="sidebar-message">
						<a href="#">
							<div class="float-left text-center">
								<img alt="image" class="rounded-circle message-avatar"
									src="img/a1.jpg">

								<div class="m-t-xs">
									<i class="fa fa-star text-warning"></i> <i
										class="fa fa-star text-warning"></i>
								</div>
							</div>
							<div class="media-body">

								There are many variations of passages of Lorem Ipsum available.
								<br> <small class="text-muted">Today 4:21 pm</small>
							</div>
						</a>
					</div>
					<div class="sidebar-message">
						<a href="#">
							<div class="float-left text-center">
								<img alt="image" class="rounded-circle message-avatar"
									src="img/a2.jpg">
							</div>
							<div class="media-body">
								The point of using Lorem Ipsum is that it has a more-or-less
								normal. <br> <small class="text-muted">Yesterday
									2:45 pm</small>
							</div>
						</a>
					</div>
					<div class="sidebar-message">
						<a href="#">
							<div class="float-left text-center">
								<img alt="image" class="rounded-circle message-avatar"
									src="img/a3.jpg">

								<div class="m-t-xs">
									<i class="fa fa-star text-warning"></i> <i
										class="fa fa-star text-warning"></i> <i
										class="fa fa-star text-warning"></i>
								</div>
							</div>
							<div class="media-body">
								Mevolved over the years, sometimes by accident, sometimes on
								purpose (injected humour and the like). <br> <small
									class="text-muted">Yesterday 1:10 pm</small>
							</div>
						</a>
					</div>
					<div class="sidebar-message">
						<a href="#">
							<div class="float-left text-center">
								<img alt="image" class="rounded-circle message-avatar"
									src="img/a4.jpg">
							</div>

							<div class="media-body">
								Lorem Ipsum, you need to be sure there isn't anything
								embarrassing hidden in the <br> <small class="text-muted">Monday
									8:37 pm</small>
							</div>
						</a>
					</div>
					<div class="sidebar-message">
						<a href="#">
							<div class="float-left text-center">
								<img alt="image" class="rounded-circle message-avatar"
									src="img/a8.jpg">
							</div>
							<div class="media-body">

								All the Lorem Ipsum generators on the Internet tend to repeat. <br>
								<small class="text-muted">Today 4:21 pm</small>
							</div>
						</a>
					</div>
					<div class="sidebar-message">
						<a href="#">
							<div class="float-left text-center">
								<img alt="image" class="rounded-circle message-avatar"
									src="img/a7.jpg">
							</div>
							<div class="media-body">
								Renaissance. The first line of Lorem Ipsum, "Lorem ipsum dolor
								sit amet..", comes from a line in section 1.10.32. <br> <small
									class="text-muted">Yesterday 2:45 pm</small>
							</div>
						</a>
					</div>
					<div class="sidebar-message">
						<a href="#">
							<div class="float-left text-center">
								<img alt="image" class="rounded-circle message-avatar"
									src="img/a3.jpg">

								<div class="m-t-xs">
									<i class="fa fa-star text-warning"></i> <i
										class="fa fa-star text-warning"></i> <i
										class="fa fa-star text-warning"></i>
								</div>
							</div>
							<div class="media-body">
								The standard chunk of Lorem Ipsum used since the 1500s is
								reproduced below. <br> <small class="text-muted">Yesterday
									1:10 pm</small>
							</div>
						</a>
					</div>
					<div class="sidebar-message">
						<a href="#">
							<div class="float-left text-center">
								<img alt="image" class="rounded-circle message-avatar"
									src="img/a4.jpg">
							</div>
							<div class="media-body">
								Uncover many web sites still in their infancy. Various versions
								have. <br> <small class="text-muted">Monday 8:37 pm</small>
							</div>
						</a>
					</div>
				</div>

			</div>

			<div id="tab-2" class="tab-pane">

				<div class="sidebar-title">
					<h3>
						<i class="fa fa-cube"></i> Latest projects
					</h3>
					<small><i class="fa fa-tim"></i> You have 14 projects. 10
						not completed.</small>
				</div>

				<ul class="sidebar-list">
					<li><a href="#">
							<div class="small float-right m-t-xs">9 hours ago</div>
							<h4>Business valuation</h4> It is a long established fact that a
							reader will be distracted.

							<div class="small">Completion with: 22%</div>
							<div class="progress progress-mini">
								<div style="width: 22%;"
									class="progress-bar progress-bar-warning"></div>
							</div>
							<div class="small text-muted m-t-xs">Project end: 4:00 pm -
								12.06.2014</div>
					</a></li>
					<li><a href="#">
							<div class="small float-right m-t-xs">9 hours ago</div>
							<h4>Contract with Company</h4> Many desktop publishing packages
							and web page editors.

							<div class="small">Completion with: 48%</div>
							<div class="progress progress-mini">
								<div style="width: 48%;" class="progress-bar"></div>
							</div>
					</a></li>
					<li><a href="#">
							<div class="small float-right m-t-xs">9 hours ago</div>
							<h4>Meeting</h4> By the readable content of a page when looking
							at its layout.

							<div class="small">Completion with: 14%</div>
							<div class="progress progress-mini">
								<div style="width: 14%;" class="progress-bar progress-bar-info"></div>
							</div>
					</a></li>
					<li><a href="#"> <span
							class="label label-primary float-right">NEW</span>
							<h4>The generated</h4> <!--<div class="small float-right m-t-xs">9 hours ago</div>-->
							There are many variations of passages of Lorem Ipsum available.
							<div class="small">Completion with: 22%</div>
							<div class="small text-muted m-t-xs">Project end: 4:00 pm -
								12.06.2014</div>
					</a></li>
					<li><a href="#">
							<div class="small float-right m-t-xs">9 hours ago</div>
							<h4>Business valuation</h4> It is a long established fact that a
							reader will be distracted.

							<div class="small">Completion with: 22%</div>
							<div class="progress progress-mini">
								<div style="width: 22%;"
									class="progress-bar progress-bar-warning"></div>
							</div>
							<div class="small text-muted m-t-xs">Project end: 4:00 pm -
								12.06.2014</div>
					</a></li>
					<li><a href="#">
							<div class="small float-right m-t-xs">9 hours ago</div>
							<h4>Contract with Company</h4> Many desktop publishing packages
							and web page editors.

							<div class="small">Completion with: 48%</div>
							<div class="progress progress-mini">
								<div style="width: 48%;" class="progress-bar"></div>
							</div>
					</a></li>
					<li><a href="#">
							<div class="small float-right m-t-xs">9 hours ago</div>
							<h4>Meeting</h4> By the readable content of a page when looking
							at its layout.

							<div class="small">Completion with: 14%</div>
							<div class="progress progress-mini">
								<div style="width: 14%;" class="progress-bar progress-bar-info"></div>
							</div>
					</a></li>
					<li><a href="#"> <span
							class="label label-primary float-right">NEW</span>
							<h4>The generated</h4> <!--<div class="small float-right m-t-xs">9 hours ago</div>-->
							There are many variations of passages of Lorem Ipsum available.
							<div class="small">Completion with: 22%</div>
							<div class="small text-muted m-t-xs">Project end: 4:00 pm -
								12.06.2014</div>
					</a></li>

				</ul>

			</div>
			--%>

			<div id="tab-3" class="tab-pane active">

				<div class="sidebar-title">
					<h3>
						<i class="fa fa-gears"></i> Settings
					</h3>
					<small><i class="fa fa-tim"></i>全局配置，请谨慎操作！</small>
				</div>

				<div class="setings-item">
					<span> 清除所有缓存 </span>
					<div class="switch">
						<div class="onoffswitch">
							<input type="checkbox" name="collapsemenu"
								class="onoffswitch-checkbox" id="clearCacheButton"> <label
								class="onoffswitch-label" for="clearCacheButton"> <span
								class="onoffswitch-inner"></span> <span
								class="onoffswitch-switch"></span>
							</label>
						</div>
					</div>
				</div>

			</div>
		</div>

	</div>

</div>

<script>
$(document).ready(function(){
	//清除所有缓存
	$("#clearCacheButton").change(function(){
		$.ajax({
			type: "post",
			url: "web/main/clearCache",
			data: {},
			async: false,
			dataType: "json",
			cache: false,
			success: function(data){
				if(data.errCode==0){
					redragonJS.alert("缓存清除成功");
					setTimeout("resetClearCacheButton()", 1000);
				}else{
					redragonJS.alert(data.errMsg);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				redragonJS.alert(textStatus);
			}
		});
	});
});

//重置清除缓存按钮效果
function resetClearCacheButton(){
	$("#clearCacheButton").prop("checked", false);
}
</script>