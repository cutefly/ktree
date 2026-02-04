<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="kr.co.kpcard.ktree.database.dao.DivisionInfo" %>
<%@ include file="/WEB-INF/views/include/header.html" %>
<%@ include file="/WEB-INF/views/sub/login/session.jsp" %>
<%@ include file="/WEB-INF/views/common/function.jsp" %>
<%
	List<DivisionInfo> divisionList = (List<DivisionInfo>)request.getAttribute("divisionList");
%>
<body>
	<div id="wrapper">
		<%@ include file="/WEB-INF/views/include/navigator.html" %>
		<!-- /#page-wrapper -->
		<div id="page-wrapper">
			<div class="row">
				<div><h2 class="page-header">프로젝트 기여도</h2></div>
				<div class="panel panel-red">
					<div class="panel-heading">프로젝트 기여도</div>
					<div class="panel-body tabbable tabs-below" id="searchDiv">
						<table class="table" id="formTable">
							<tr>
								<td class="col-lg-10">
									<div class="form-group col-lg-3">
										<div class="col-xs-10">
											<label class="form-inline">조회일자 - </label>
										</div>

										<div class="col-xs-10">
											<input class="form-control date-picker" type="text" id="searchSDate" name="searchSDate" placeholder="조회시작일" value="${yyyyMM}" readonly/>
										</div>

										<div class="col-xs-1">
											<button type="button" class="btn btn-danger btn-sm" id="searchBtn" name="searchBtn" onClick="searchData('projectAdmin');">Search</button>
										</div>
									</div>
									<div class="form-group col-lg-8">
										<div class="col-xs-10">
											<label class="form-inline">조회 조건 - </label>
										</div>
										<div class="col-xs-3">
											<select class="form-control" name="divisionCode" id="divisionCode">
												<option value="">전체</option>
<% for(int i=0; i<divisionList.size(); i++){ %>
												<option value="<%=divisionList.get(i).getCode()%>"><%=divisionList.get(i).getName()%></option>
<% } %>
											</select>
										</div>
										<div class="col-xs-3" style="display:none;">
											<label>전체 보기 <input type="radio" id="searchOption" name=searchOption value="all" <%=(sessionAuthLevel>=1)? "checked" : "" %>/></label>
										</div>

									</div>

								</td>
							</tr>
						</table>
					</div>
					<div class="panel-body centerText" id="data-panel">
					</div>

					<div class="panel-body" align="right"><div class="col-xs-2 pull-right"><button type="button" class="btn btn-danger btn-sm btn-block" id="searchBtn" name="searchBtn" onClick="saveProjectData();">상신</button></div>

					   	<!-- <div class="col-xs-2 pull-right"><button type="button" class="btn btn-danger btn-outline btn-sm btn-block" id="searchBtn" name="searchBtn" onClick="projectDataSave('save');">저장</button></div> -->
					</div>
	  	 		</div>
			</div>
			<!-- /.row -->
		</div>
		<!-- /#page-wrapper -->
	</div>
	<!-- /#wrapper -->
</body>
<script>
	setDatePicker();

	$(document).ready(function(){
		searchData('projectAdmin');
	});
</script>

</html>
