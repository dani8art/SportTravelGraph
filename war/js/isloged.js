function isLoged(){
	var cookie_log = $.cookie("session");
	if(cookie_log){
		if(window.location.pathname == "/login.html"){
			window.location = "/management/index.html";
		}
	}else{
		if(window.location.pathname != "/login.html"){
			window.location= "../login.html";
		}
	}
}

function logOut(){
	$.cookie.json = true;
	var send = $.cookie("session");
	var request = $.ajax({
		url:"../api/v1/logout",
		type:"POST",
		data:JSON.stringify(send)
	});
	request.done(function(data,status,jqHXR){
		window.location= "../login.html";
	});
}

$(document).ready(function(){
	$("#logout").on("click",logOut);
});