//로그인  처리
var login = function(){
	if($("#employeId").val()==""){
		alert("아이디를 입력해주세요.");
		return;
	}else if($("#employePw").val()==""){
		alert("비밀번호를 입력해주세요.");
		return;
	}
	$.ajax({
		url : 'loginProc',
		type : 'post',
		dataType : 'text',
		data : {"employeId" : $("#employeId").val(), "employePw":$("#employePw").val()},
		success:function(data){
			var dataJson = JSON.parse(data);
			if ( dataJson.result == "S") {
				alert("'"+dataJson.message+"'님 로그인 되었습니다.");
				document.location.href='main';
			} else {
				alert(dataJson.message);
			}
		},error : function(request, status, error){
			var data = JSON.parse(request.responseText);
			console.log(JSON.parse(request.responseText));
			alert(data.customMsg);
		}
	});
}

//Enter Submit
$(function(){
	$("text").keydown(function(e){
		if(e.keyCode == 13){
			login();
			return false;
		}
	});
});