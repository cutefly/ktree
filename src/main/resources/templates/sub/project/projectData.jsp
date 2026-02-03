<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="kr.co.kpcard.ktree.database.dao.ResultProjectScore" %>
<%@ page import="kr.co.kpcard.ktree.database.dao.ProjectScore" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ include file="/WEB-INF/views/sub/login/session.jsp" %>
<%
	//프로젝트 점수(Request)
	List<ResultProjectScore> projectScoreList = (List<ResultProjectScore>)request.getAttribute("projectScoreList");
	List<ProjectScore> scoreHistoryList  = (List<ProjectScore>)request.getAttribute("scoreHistoryList");

	//오늘날짜(day)
	int todaydd = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

	ResourceBundle bundle = ResourceBundle.getBundle("runtime.config.spring.context.app");
	//int limitDay = Integer.parseInt(bundle.getString("limit.date"));

	// 평가가 가능한지 여부
	Boolean isAvailable = (Boolean)request.getAttribute("isAvailable");

	//합계점수, 항목별점수, 상태
//	int tot, score, myScore;	// 소수점을 허용하면서 미사용
	int status, useAuthLevel = 0;

	float totF = 0;
	float scoreF = 0;
	float myScoreF = 0;

	//상태 text
	String statusText="";

	boolean useAuth = false, useDissent = false;


%>
<span class="col-lg-10 pull-left" style="text-align:left;"><label> * 관리입력 : PJ업무를 제외한 회사의 공통업무 또는 해피머니와 관련된 업무</span>
<table class="table" id="dataTables-example">
	<thead>
		<tr>
			<td width="3%" rowspan="2"><b>No</b></td>
			<td width="3%" rowspan="2"><input type="checkbox" name="allSeq" onClick="allCheck('idx[]');"/></td>
			<td width="6%" rowspan="2"><b>이름</b></td>
			<td width="*" rowspan="2"><b>부서</b></td>
			<td width="8%" rowspan="2"><b>팀</b></td>
			<td rowspan="2"><b>상태</b></td>
			<td width="7%" colspan="6"><b>직접비</b></td>
			<td width="7%" colspan="2"><b>공통비</b></td>
			<td width="7%"><b>합계</b></td>
		</tr>
		<tr>
			<td width="7%"><b>캐시박스</b></td>
			<td width="7%" title="갤럭시스토어, 스캔잇 포함"><b><u>POSA(주)</u></b></td>
			<td width="7%"><b>POP</b></td>
			<td width="7%"><b>Palrago</b></td>
			<td width="7%" title="제주올레 포함"><b><u>T-Grid(주)</u></b></td>
			<td width="7%"><b>Topping</b></td>
			<td width="7%"><b>관리(입력)</b></td>
			<td width="7%"><b>관리(기본)</b></td>
		</tr>
		<% for(int i=0; i<projectScoreList.size(); i++){
				useAuthLevel = (sessionEmployeId.equals(projectScoreList.get(i).getConfirm1()))? 1 : (sessionEmployeId.equals(projectScoreList.get(i).getConfirm2()))? 2 : 0;

				useAuth = useCheckBox(sessionEmployeId,
									  projectScoreList.get(i).getConfirm1(),
									  projectScoreList.get(i).getConfirm2(),
					   				  projectScoreList.get(i).getEmployeId(),
					   				  projectScoreList.get(i).getStatus());

				useDissent = useDissent(sessionEmployeId,
										projectScoreList.get(i).getConfirm1(),
						  				projectScoreList.get(i).getConfirm2(),
						   				projectScoreList.get(i).getEmployeId(),
						   				projectScoreList.get(i).getStatus());

				status = projectScoreList.get(i).getStatus();
				//out.print("use Level "+ useAuthLevel);

		%>
		<tr <%= ((projectScoreList.get(i).getStatus()==4 || projectScoreList.get(i).getStatus()==5) && useDissent)? "style=\"background-color:#FF9090\"" : "" %>>
			<td><%=(i+1)%></td>
			<td>
				<%if(useAuth && status<3){%><input type="checkbox" name="idx[]" value="<%=i%>"/><%}%>
				<input type="hidden" name="seq" id="seq_<%=i%>" value="<%=projectScoreList.get(i).getSeq()%>" />
				<input type="hidden" name="employeId" id="employeId_<%=i%>" value="<%=projectScoreList.get(i).getEmployeId()%>" />
				<input type="hidden" name="confirmNumber" id="confirmNumber_<%=i%>" value="<%= useAuthLevel %>" />
			</td>
			<td><%=projectScoreList.get(i).getEmployeName()%></td>
			<td><%=projectScoreList.get(i).getDivisionName()%></td>
			<td><%=projectScoreList.get(i).getTeamName()%></td>
			<td>
				<%
					switch(projectScoreList.get(i).getStatus()){
						case 0: statusText = "<font color='red'>미등록</font>"; break;
						case 1: statusText = "<font color='green'>상신</font>"; break;
						case 2: statusText = "<font color='green'>1차승인</font>"; break;
						case 3: statusText = "<font color='blue'>설정 완료</font>"; break;
						case 4: statusText = "<font color='red'>이의 제기</font>"; break;
						case 5: statusText = "<font color='green'>이의 제기(1차)</font>"; break;
						case 6: statusText = "<font color='blue'>설정 완료</font>"; break;
					}
					out.print(statusText);
				%>
				<% if( useDissent && isAvailable ){ %>
					</br><button type="button" class="btn btn-danger btn-xs" id="searchBtn" name="searchBtn" onClick="openDissentPop('<%=projectScoreList.get(i).getEmployeId()%>','<%=projectScoreList.get(i).getMonth()%>');">이의제기<%=(projectScoreList.get(i).getStatus()>=4)? "확인":"" %> </button>
				<%}%>
			</td>
			<!-- 점수출력 (S)-->
			<%
				totF=0;
				for(int k=1; k<=8; k++)
				{
					scoreF = 0;
					myScoreF = 0;
					//평가자 등록 점수
					switch(k){
						case 1 :
							scoreF = projectScoreList.get(i).getScore1();
							break;
						case 2 :
							scoreF = projectScoreList.get(i).getScore2();
							break;
						case 3 :
							scoreF = projectScoreList.get(i).getScore3();
							break;
						case 4 :
							scoreF = projectScoreList.get(i).getScore4();
							break;
						case 5 :
							scoreF = projectScoreList.get(i).getScore5();
							break;
						case 6 :
							scoreF = projectScoreList.get(i).getScore6();
							break;
						case 7 :
							scoreF = projectScoreList.get(i).getScore7();
							break;
						case 8 :
							scoreF = projectScoreList.get(i).getScore8();
							break;
					}
					totF += scoreF;

					//피평가자 등록점수
					for(ProjectScore scoreHistory : scoreHistoryList)
					{
						if(projectScoreList.get(i).getEmployeId().equals(scoreHistory.getEmployeId())){
							myScoreF = 0;
							switch(k){
								case 1 :
									myScoreF = scoreHistory.getScore1();
									break;
								case 2 :
									myScoreF = scoreHistory.getScore2();
									break;
								case 3 :
									myScoreF = scoreHistory.getScore3();
									break;
								case 4 :
									myScoreF = scoreHistory.getScore4();
									break;
								case 5 :
									myScoreF = scoreHistory.getScore5();
									break;
								case 6 :
									myScoreF = scoreHistory.getScore6();
									break;
								case 7 :
									myScoreF = scoreHistory.getScore7();
									break;
								case 8 :
									myScoreF = scoreHistory.getScore8();
									break;
							}
						}
					}
			%>
				<td width="7%">
					<%if(useAuth && status<3 && isAvailable){%>
						<%if(k<8){%>
							<input type="text" size="2" name="projectScoreList[<%=i%>].score<%=k%>" id="score<%=i%>_<%=k%>" maxlength="3" value="<%=scoreF%>" onKeyDown="numberChk(event)" onKeyUp="checkLimit(this, event)" onFocus="onFocusBlank(this);" onBlur="outFocusValue(this);" style="ime-mode:disabled;">
						<%}else{%>
							<%=scoreF%>
							<input type="hidden" size="2" name="projectScoreList[<%=i%>].score<%=k%>" id="score<%=i%>_<%=k%>" maxlength="3" value="<%=scoreF%>">
						<%}%>
					<% }else{
						out.print(scoreF);
						if(k<8 && projectScoreList.get(i).getEmployeId().equals(sessionEmployeId)){
							out.print("<font style='font-size:12px;'>("+myScoreF+")</font>");
							if(scoreF > myScoreF){
								out.print("<font color='red'>▲</font>");
							}else if(scoreF < myScoreF){
								out.print("<font color='blue'>▼</font>");
							}else{
								out.print("<font color='green'>■</font>");
							}
						} %>
					<% } %>
				</td>
			<% } %>
			<!-- 점수출력 (S)-->
			<td width="7%"><b><input type="text" size="2" id="totalscore<%=i%>" value="<%=totF%>" readonly /></b></td>
		</tr>
	<% } %>
	</thead>
	<tbody>
		<!-- tr class="even gradeA">
			<td colspan="12" align="center"><strong>검색 결과가 존재하지 않습니다.</strong></td>
		</tr-->
	</tbody>
</table>
<%!
	public boolean useCheckBox(String sessionId, String confirm1, String confirm2, String emplyeId, int status){
		boolean useAuth = false;
		if(sessionId.equals(emplyeId) && status==0){
			useAuth = true;
		}else if(status==1 && confirm1.equals(sessionId)){
			useAuth = true;
		}else if(status==2 && confirm2.equals(sessionId)){
			useAuth = true;
		}
		return useAuth;
	}

	public boolean useDissent(String sessionId, String confirm1, String confirm2, String emplyeId, int status){

		boolean useDissent = false;
		if(sessionId.equals(emplyeId) && status>=3){
			useDissent = true;
		}else if(status==4 && confirm1.equals(sessionId)){
			useDissent = true;
		}else if(status==5 && confirm2.equals(sessionId)){
			useDissent = true;
		}else if(
			(sessionId.equals(emplyeId) || sessionId.equals(confirm1) || sessionId.equals(confirm2)) && status==6){
			useDissent = true;
		}
		return useDissent;
	}
%>