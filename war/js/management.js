$(document).ready(function(){
	
	actLeagues();
	$('#league-management-form').on("submit", function(){
		var name = $(this).find("#name").val();
		var id = name.toLowerCase();
		var league = new Object();
		league.name = name;
		league.id = id.replace(/\s/g,'');
		var json = JSON.stringify(league);
		var postLeague = $.ajax({
			url: "../api/v1/league",
			type: "POST",
			data: json
		});
		postLeague.done(function(data, status, jqXHR){
			if(jqXHR.status == 200){
				actLeagues();
				actInput();
			}
		});
		return false;
	});
	$("#group-management-form").on("submit",function(){
		var name = $(this).find("#name").val();
		var idliga = $(this).find(".league option:selected").val();
		
		var id = name.length + Math.round( Math.random() * 1000 );
		var group = new Object();
		group.name = name;
		group.id = id;
		group.idliga = idliga;
		var json = JSON.stringify(group);
		var postgroup = $.ajax({
			url: "../api/v1/group",
			type: "POST",
			data: json
		});
		postgroup.done(function(data, status, jqXHR){
			if(jqXHR.status == 200){
				actLeagues();
				actGroup();
				actInput();
			}
		});	
		return false;
	});
	$(".league").on("change", actGroup);
	
	$("#team-management-form").on("submit", function(){
		var town = $(this).find("#town").val().replace(/\s/g,'+');	
		var postteam = $.ajax({
			url: "../api/v1/geo/"+town,
			type: "GET",
			data: null
		});
		postteam.done(function(data, status, jqXHR){
			if(jqXHR.status == 200){
					var ret = $.parseJSON(data);
				if(ret.status =="OK"){				
					var name = $("#team-management-form").find("#name").val();
					var town = $("#team-management-form").find("#town").val();
					var idgroup = parseInt($("#team-management-form").find(".group option:selected").val());
					var idliga = $("#team-management-form").find(".league option:selected").val();
					var gameday = $("#team-management-form").find("#gameday option:selected").val();
					var gamehour =  $("#team-management-form").find("#gamehour").val();
				
					var team = new Object();
					team.name = name;
					team.town = town;
					team.idgroup = idgroup;
					team.idliga = idliga;
					team.gameday = gameday;
					team.gamehour = gamehour;
					team.location = ret.results[0].geometry.location.lat+";"+ret.results[0].geometry.location.lng;
					var json = JSON.stringify(team);
					var postteam = $.ajax({
						url: "../api/v1/team",
						type: "POST",
						data: json
					});
					postteam.done(function(data, status, jqXHR){
						actLeagues();
						actGroup();
						actInput();
					});
				}else{
					alert(ret.status);
				}
			}
		});	
		return false;
	});
	$("#user-management-form").on("submit", function(){
		var password = $(this).find("#password").val();
		var re_password = $(this).find("#re_password").val();
		if(password == re_password){
			var username = $(this).find("#username").val();
			var email = $(this).find("#email").val();
			
			var user = new Object();
			user.username = username;	
			user.email = email;
			user.password = password;
			json = JSON.stringify(user);
			
			var postuser = $.ajax({
				url: "../api/v1/user",
				type: "POST",
				data: json
			});
			postuser.done(function(data, status, jqXHR){
				if(jqXHR.status == 200){
					actLeagues();
					actGroup();
					actInput();
				}
			});
		}else{
			alert("Las password no coinciden.");
		}
		return false;
	});
});
function actInput(){
	$('input').val("");
}
function actGroup(){
	var idliga = $(".league option:selected").attr("value");
	var group = $.ajax({
		url: "../api/v1/group?idliga="+idliga,
		type:"GET",
		data:null
	});
	group.done(function(data, status, jqXHR){
		if(jqXHR.status == 200){
			var data_in = $.parseJSON(data);
			$(".group").children().remove();
			$(".group").append($("<option value='null'>Select...</option>"));

			for(groupi in data_in){
				$(".group").append($("<option value='"+data_in[groupi].id+"'>"+data_in[groupi].name+"</option>"));
			}
		}
	});
}
function actLeagues(){
	var leagues = $.ajax({
		url: "../api/v1/league",
		type:"GET",
		data:null
	});
	leagues.done(function(data, status, jqXHR){
		if(jqXHR.status == 200){
			var data_in = $.parseJSON(data);
			$(".league").children().remove();
			$(".league").append($("<option value='null'>Select...</option>"));

			for(league in data_in){
				$(".league").append($("<option value='"+data_in[league].id+"'>"+data_in[league].name+"</option>"));
			}
		}
	});
}