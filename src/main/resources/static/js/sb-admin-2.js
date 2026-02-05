/*******************************************************************************
 * Copyright 2015 Brient Oh @ Pristine Core
 * boh@pristinecore.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

var historyVal=0;

$(function() {
	if($('#side-menu').length>0){
	    $('#side-menu').metisMenu();
	}
});

//Loads the correct sidebar on window load,
//collapses the sidebar on window resize.
// Sets the min-height of #page-wrapper to window size
$(function() {
    $(window).bind("load resize", function() {
        topOffset = 50;
        width = (this.window.innerWidth > 0) ? this.window.innerWidth : this.screen.width;
        if (width < 768) {
            $('div.navbar-collapse').addClass('collapse');
            topOffset = 100; // 2-row-menu
        } else {
            $('div.navbar-collapse').removeClass('collapse');
        }

        height = ((this.window.innerHeight > 0) ? this.window.innerHeight : this.screen.height) - 1;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $("#page-wrapper").css("min-height", (height) + "px");
        }
    });

    var url = window.location;
    var element = $('ul.nav a').filter(function() {
        return this.href == url || url.href.indexOf(this.href) == 0;
    }).addClass('active').parent().parent().addClass('in').parent();
    if (element.is('li')) {
        element.addClass('active');
    }
});

var moveConfirm = function(url, id, authLevel){
	var yyyyMM = $("#searchSDate").val();
	if(url!=''){
		document.location.href=url+"?employeId="+id+"&yyyyMM="+yyyyMM+"&authLevel="+authLevel;
	}
}

$(document).ready(function(){
	//setSelectYear(); /* 기본 년도 세팅 */
	//setSelectMonth();/* 기본 월 세팅 */
	//setSelectDate(); /*조회 날짜 세팅 */
	//setDatePicker(); /* DATEPICKER 생성*/
});

var setMerchantCode = function(code, name){
	$("#merchantCode").val(code);
	$("#merchantName").val(name);
}

var setDataTable = function(id){
	if($("#"+id).length>0){
		var option;
		switch(id){
			case "merchant_table":
				option = { "aoColumns": [null,null,null,{ "bSortable": false }] }
				break;
		}
		$('#'+id).DataTable(option);
	}
}

var setDatePicker = function(type){
	if($(".date-picker").length>0){
		var kr_monthName = ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'];
		/*월 선택달력*/
		var d = new Date();
		$(".date-picker").datepicker("destroy");
		$(".date-picker").monthpicker({
			pattern: 'yyyymm',
			selectedYear : d.getFullYear(),
			selectedMonth : (d.getMonth()),
			startYear: 2000,
			finalYear: d.getFullYear(),
			max : d.getFullYear(),
			monthNames: kr_monthName
		});

		/*일 선택달력*/
		/*
		$(".date-picker").datepicker({
			dateFormat : 'yy-mm-dd',
			monthNames : kr_monthName,
			monthNamesShort : kr_monthName,
			changeMonth: false,
			changeYear: false,
			showButtonPanel: false,
			showMonthAfterYear: true
		});
		 */
	}
}

/* SELECTBOX 년도 세팅 */
var setSelectYear = function(){
	if($("#selectYear").length>0){
		var d = new Date();
		for(var i=2000; i<=d.getFullYear(); i++){
			$("#selectYear").append("<option value='" + i + "' " + ((i==d.getFullYear())? "selected" : "") + ">" + i + "</option>");
		}
	}
}

/* SELECTBOX 월 세팅 */
var setSelectMonth = function(){
	if($("#selectMonth").length>0){
		var d = new Date();
		for(var i=1; i<=12; i++){
			$("#selectMonth").append("<option value='" + i + "' " + ((i==(d.getMonth()+1))? "selected" : "") + ">" + i + "</option>");
		}
	}
}

/* SELECT 날짜 설정 */
var setSelectDate = function(){
	if($("#searchSDate").val()=="" && $("#searchEDate").val()==""){
		var selectYear = $("#selectYear").val();
		var selectMonth = ($("#selectMonth").val()<10)? "0"+$("#selectMonth").val() : $("#selectMonth").val();
		var lastDate = ( new Date(selectYear, selectMonth, 0) ).getDate();
		$("#searchSDate").val(selectYear + "-" + selectMonth + "-01");
		$("#searchEDate").val(selectYear + "-" + selectMonth + "-" + lastDate);
	}
}

/* 체크박스 전체 선택,해제 */
var allCheck = function(name){
	$("[name='" + name + "']").prop("checked",$("[name='allSeq']").is(":checked"));
}

var searchData = function(type){
	var url,sDate,eDate,searchOption,divisionCode,useYn;

	sDate = $("#searchSDate");
	eDate = $("#searchEDate");
	searchOption = ($("#searchOption:checked").length>0)? $("#searchOption:checked").val() : "";
	divisionCode = ($("#divisionCode").length>0)? $("#divisionCode").val() : "";
	useYn = ($("#useYn").length>0)? $("#useYn").val() : "";
	if((eDate.val()=="" && eDate.length>0) || (sDate.val()=="" && sDate.length>0)){
		alert('조회날짜를 선택해주세요.');
		return;
	}

	if(type=='project'){url="projectData";}
	if(type=='projectAdmin'){url="projectDataAdmin";}
	if(type=='employeAdmin'){url="employeDataAdmin";}
	if(type=='performanceList'){url="performanceListData";}
	if(type=='performanceListAdmin'){url="performanceListDataAdmin";}

	$.ajax({
		url : url,
		type : 'post',
		dataType : 'text',
		data : {yyyyMM : sDate.val(), eyyyyMM:eDate.val(), searchOption:searchOption, divisionCode : divisionCode, useYn : useYn},
		success : function(data){
			$("#data-panel").html(data);
			$("#pageType").val(type);
		},error : function(request, status, error){
			var data = JSON.parse(request.responseText);
			//console.log(JSON.parse(request.responseText));
			alert(data.customMsg);
		}
	});
}

var numberChk = function(e) {
	//숫자만 입력
	 if ($.inArray(e.keyCode, [46, 8, 9, 13, 110]) !== -1 ||
	         // Allow: Ctrl+A
	    (e.keyCode == 65 && e.ctrlKey === true) ||
	     // Allow: Ctrl+C
	    (e.keyCode == 67 && e.ctrlKey === true) ||
	     // Allow: Ctrl+X
	    (e.keyCode == 88 && e.ctrlKey === true) ||
	     // Allow: home, end, left, right
	    (e.keyCode >= 35 && e.keyCode <= 39)) {
	         // let it happen, don't do anything
	         return;
	}
	console.log(e.keyCode);
	// Ensure that it is a number and stop the keypress
	if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105) && e.keyCode != 190 ) {
		console.log("prevented");
	    e.preventDefault();
	}
}

/*
 * (keyId>=48 && keyId<=57) || (keyId>=96 && keyId<=105) 숫자
 *  keyId==8 backspace
 *
 */
var checkLimit = function(attr, e){
	// formatting check
	var	attrValue = attr.value;
	var valuePattern = /^(\d{1,2})([.]\d{0,1}?)?$/;
	console.log("pattern test : ", attrValue, valuePattern.test(attrValue));
	if ( valuePattern.test(attrValue) != true ) {
		console.log("invalid patter : ^(\d{1})([.]\d{0,1}?)?$");
		attr.value = "";
		return;
	}

	e = e || window.event;
	var keyId = (event.witch)? event.witch : event.keyCode;
	if(!((keyId>=48 && keyId<=57) || (keyId>=96 && keyId<=105) ||
			keyId==8 ||	// backspace
			keyId==9 || // tabkey
			keyId ==190 // .
			)){
		e.preventDefault();
		return;
	}

//	if(!((keyId>=48 && keyId<=57) || (keyId>=96 && keyId<=105) || keyId==8 || keyId==9 || keyId ==190)){
//		e.preventDefault();
//		event.target.value="0";
//	}

	var totalLimit = 10;
	var id = attr.id.split("_")[0];
	var idLength = 8;//$('[id^="'+id+'"]').length;
	var total = 0;
	for(var i=1; i<=idLength; i++){
		total+=Number($("#"+id+"_"+i).val());
	}
	$("#total"+id+"").val(total.toFixed(1));

	if(total>totalLimit){
		alert("총 합계점수를 초과하였습니다.(총점 10)");
		$("#"+attr.id).val('');

		// 합계를 다시 계산
		total = 0;
		for(var i=1; i<=idLength; i++){
			total+=Number($("#"+id+"_"+i).val());
		}
		$("#total"+id+"").val(total.toFixed(1));

		return;
	}

}

var saveProjectData =  function(){
	var seq = [];	var employeId = []; var confirmNumber = [];
	var score1 = [];
	var score2 = [];
	var score3 = [];
	var score4 = [];
	var score5 = [];
	var score6 = [];
	var score7 = [];
	var score8 = [];
	var idx, yyyyMM, totalScore;
	yyyyMM = $("#searchSDate").val();

	if($("[name='idx[]']:checked").length<1){
		alert('상신 할 데이터를 선택해주세요.');
		return;
	}

	for(var i=0; i<$("[name='idx[]']:checked").length; i++){
		idx = $("[name='idx[]']:checked").eq(i).val();
		seq.push($("#seq_"+idx).val());
		employeId.push($("#employeId_"+idx).val());
		confirmNumber.push($("#confirmNumber_"+idx).val());
		score1.push($("#score"+idx+"_1").val());
		score2.push($("#score"+idx+"_2").val());
		score3.push($("#score"+idx+"_3").val());
		score4.push($("#score"+idx+"_4").val());
		score5.push($("#score"+idx+"_5").val());
		score6.push($("#score"+idx+"_6").val());
		score7.push($("#score"+idx+"_7").val());
		score8.push($("#score"+idx+"_8").val());
		totalScore = Number($("#score"+idx+"_1").val())
					+Number($("#score"+idx+"_2").val())
					+Number($("#score"+idx+"_3").val())
					+Number($("#score"+idx+"_4").val())
					+Number($("#score"+idx+"_5").val())
					+Number($("#score"+idx+"_6").val())
					+Number($("#score"+idx+"_7").val())
					+Number($("#score"+idx+"_8").val());
		if(totalScore != 10){
			alert('점수의 총 합이 `10`이 아닙니다.');
			return;
		}
	}

	if(!confirm('한번 상신한 데이터는 수정 할 수 없습니다. 상신하시겠습니까?')){ return; }

	$.ajax({
		url : "saveProjectScore",
		type : "post",
		dataType : "text",
		data : {
			seq : seq,
			month : yyyyMM,
			employeId : employeId,
			score1 : score1,
			score2 : score2,
			score3 : score3,
			score4 : score4,
			score5 : score5,
			score6 : score6,
			score7 : score7,
			score8 : score8,
			confirmNumber : confirmNumber
		},
		success : function(data){
			if(data=="S"){
				alert('등록이 완료되었습니다.');
				searchData($("#pageType").val());
			}else{
				alert('등록중 에러가 발생하였습니다. \n에러가 계속될경우 기술개발팀에 문의해주세요.');
			}
		},error : function(request){
			//console.log(e);
			var data = JSON.parse(request.responseText);
			//console.log(JSON.parse(request.responseText));
			alert(data.customMsg);
		}

	});
}

var SavePerformData = function(authLevel){
	var performScore = $("#performScore"+authLevel).val();
	var month = $("#month").val();
	var employeId = $("#employeId").val();
	var confirmNumber = authLevel;
	var valueComments = $("#valueComments"+authLevel).val();
	var performComments = $("#performComments"+authLevel).val();
	var dissentComments = "";
	var valueScore = [];
	var url = "savePerformScore";

	if($("#performScore"+authLevel).val()>10){
		alert("평가점수는 10점을 초과할 수 없습니다.!");
		$("#performScore"+authLevel).focus();
		return false;
	}else if($("#performScore").val()<1){
		alert("평가점수를 입력해 주세요.!");
		return false;
	}

	if(valueComments=="" && (parseInt(month.substr(4))%3==0 && parseInt(month.substr(4))!=1)){
		alert("가치평가 Comment를 작성해주세요.");
		return false;
	}

	if(performComments==""){
		alert("성과평가 Comment를 작성해주세요.");
		return false;
	}

	if($("#dissentComments"+authLevel).length>0){
		if($("#dissentComments"+authLevel).val()==""){
			alert("이의사항에 대한 Comment를 작성해주세요.");
			return false;
		}
		dissentComments = $("#dissentComments"+authLevel).val();
	}

	for(var i=0; i<$("[name='values"+authLevel+"[]']").length; i++){
		valueScore.push($("[name='values"+authLevel+"[]']").eq(i).is(":Checked"));
	}

	if(!confirm('입력한 내용으로 등록하시겠습니까? 평가가 종료될 경우 수정이 불가합니다.')){
		return false;
	}

	$.ajax({
		url : url,
		type : "post",
		dataType : "text",
		data : {
			performScore : performScore,
			month : month,
			employeId : employeId,
			valueScore : valueScore,
			confirmNumber : confirmNumber,
			valueComments : valueComments,
			performComments : performComments,
			dissentComments : dissentComments
		},
		success : function(res){
			if(res=="S"){
				alert('등록이 완료되었습니다.');
				document.location.href='performanceList?yyyyMM='+month;
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

var openDissentPop = function(id, month){
	window.open('popProjectDissent?yyyyMM='+month+'&employeId='+id,'dissentPop','width=750, height=400, scrollbars=no, resizable=no, status=no');
}

var onFocusBlank = function(obj){
	historyVal = obj.value;
	obj.value="";
}


var outFocusValue = function(obj){
	if(obj.value==""){
		obj.value = historyVal;
	}
}

var resetPassword = function(employeId) {
    if (confirm(employeId + ' 님의 비밀번호를 초기화 하시겠습니까? 초기화된 비밀번호는 기본 비밀번호로 변경됩니다.')) {
        $.ajax({
            url: '/reset-password', // This will be the new endpoint
            type: 'post',
            dataType: 'text',
            data: { employeId: employeId },
            success: function(response) {
                if (response === 'S') {
                    alert(employeId + ' 님의 비밀번호가 성공적으로 초기화되었습니다.');
                } else {
                    alert('비밀번호 초기화에 실패하였습니다.');
                }
            },
            error: function(request, status, error) {
                alert('비밀번호 초기화 중 오류가 발생하였습니다.');
                console.error("Error resetting password:", request.responseText);
            }
        });
    }
};
