<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ include file="/WEB-INF/views/include/header.html" %>
<%@ include file="/WEB-INF/views/sub/login/session.jsp" %>
<%@ include file="/WEB-INF/views/common/function.jsp" %>
<%
	String yyyyMM = getBeforeMonth(new SimpleDateFormat("yyyyMMdd").format(new Date()));
	
	//오늘날짜(day)
	int todaydd = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
	
	ResourceBundle bundle = ResourceBundle.getBundle("runtime.config.spring.context.app");
	int limitDay = Integer.parseInt(bundle.getString("limit.date"));
	
	int useAuthLevel = (request.getAttribute("confirm1").equals(sessionEmployeId))? 1 : (request.getAttribute("confirm2").equals(sessionEmployeId))? 2 : 0;  
	//읽기전용 여부
	String readonly = "readonly";
	
%>
<body>
	<div id="wrapper">
		<div class="col-md-4 col-md-offset-4">			
			<div class="panel panel-red">
			    <div class="panel-heading">
					<h3 class="panel-title">이의사항 등록</h3>
			    </div>
			    <div class="panel-body">
					<form class="form-signin" method="POST">
						<input type="hidden" id="employeId" name="employeId" value="<%=request.getAttribute("employeId")%>"/>
						<input type="hidden" id="yyyyMM" name="yyyyMM" value="<%=request.getAttribute("yyyyMM")%>"/>
						<input type="hidden" id="useAuthLevel" name="useAuthLevel" value="<%=useAuthLevel%>"/>
						<fieldset>
						<div><label>이의 사항 등록</label></div>
						<div class="form-group">
							<%if(request.getAttribute("employeId").equals(sessionEmployeId) 
								 && (Integer)request.getAttribute("status")<5){
								readonly = "";	
							}else{
								readonly = "readonly";
							}%>
							<textarea name="dissent" id="dissent" class="form-control" rows="2" style="margin-top: 0px; margin-bottom: 0px; height: 60px;" resizeable="false" <%=readonly%>><%=(request.getAttribute("dissent")!=null)? request.getAttribute("dissent") : "" %></textarea>
						</div>
						<div><label>1차평가자 Comment</label></div>
						<div class="form-group">
							<% if(useAuthLevel==1 && (Integer)request.getAttribute("status")<6){ 
								readonly = "";	
							}else{ 
								readonly = "readonly";
							} %>
							<textarea name="comments1" id="comments1" class="form-control" rows="2" style="margin-top: 0px; margin-bottom: 0px; height: 60px;" resizeable="false" <%=readonly%>><%=(request.getAttribute("comments1")!=null)? request.getAttribute("comments1") : "" %></textarea>
						</div>
						<div><label>2차평가자 Comment</label></div>
						<div class="form-group">
							<% if(useAuthLevel==2 && (Integer)request.getAttribute("status")<6){ 
								readonly = "";	
							}else{ 
								readonly = "readonly";
							} %>
							<textarea name="comments2" id="comments2" class="form-control" rows="2" style="margin-top: 0px; margin-bottom: 0px; height: 60px;" resizeable="false" <%=readonly%>><%=(request.getAttribute("comments2")!=null)? request.getAttribute("comments2") : "" %></textarea>
						</div>
						<% if((request.getAttribute("comments2")!=null && request.getAttribute("comments1") != null) || todaydd > limitDay){ %>
							<button class="btn btn-lg btn-danger btn-block btn-outline" type="button" onClick="javascript:window.open('abount.black','_self').close();">닫기</button>							
						<% }else{ %>
							<button class="btn btn-lg btn-danger btn-block" type="button" onClick="saveComments();">Comment 등록</button>
						<% } %>
						</fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>
	<!-- /#wrapper -->  
</body>
<script>
	function saveComments(){
		var dissent = $("#dissent").val();
		var comments1 = $("#comments1").val();
		var comments2 = $("#comments2").val();
		var employeId = $("#employeId").val();
		var yyyyMM = $("#yyyyMM").val();
		var useAuthLevel = $("#useAuthLevel").val();
		$.ajax({
			url : 'saveProjectDissent',
			type : 'post',
			dataType : 'text',
			data : {employeId : employeId,
					yyyyMM : yyyyMM,
					dissent : dissent,
					comments1 : comments1,
					comments2 : comments2,
					authLevel : useAuthLevel
					},
			success : function(res){
				if(res=="S"){
					alert('등록이 완료되었습니다.');
					opener.location.reload();
					window.open('abount.black','_self').close();					
				}else{
					alert('등록중 에러가 발생하였습니다. \n에러가 계속될경우 기술개발팀에 문의해주세요.');
				}	
			},error : function(request){
				var data = JSON.parse(request.responseText);
				//console.log(JSON.parse(request.responseText));						
				alert(data.customMsg);
			}
		});
	}
</script>
</html>

