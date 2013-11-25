var map;
var markerArray = [];
var teams = [];
var geoEdges = [];
var geoCenterTeam;
var geoCenterLatLng;

$(document).ready(function(){
	$("#map_canvas").height($(window).height() - 50);
	setSelects();
	var mapDiv = document.getElementById('map_canvas');
	map = new google.maps.Map(mapDiv, {
			center: new google.maps.LatLng(37.32216274599151,-5.633450227831409),
			zoom: 10,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	});
	
	$('#league_select').on('change',function(){
		actGroup();
	});
	$('#group_select').on('change',function(){
		clearEdges()
		actTeam();
		clearMarket();
	});
	$('#team_select').on('change', function(){
		setGeoCenter($(this).find("option:selected").val());
		addEdges()
	});
	
});
function paintTeam(){
	clearMarket();
	
	for (team in teams){
		var coord = teams[team].location.split(";");
		var myLatlng = new google.maps.LatLng(coord[0],coord[1]);
		var marker = new google.maps.Marker({
			position: myLatlng,
			map: map,
			animation: google.maps.Animation.DROP,
			//icon: image,
			title:"Hacer 'click' para ver información"
		});
		markerArray.push(marker);
	}	
}
function actTeam(){
	var idliga = $("#league_select option:selected").attr("value");
	var idgroup = $("#group_select option:selected").attr("value");
	var group = $.ajax({
		url: "api/v1/team?idliga="+idliga+"&idgroup="+idgroup,
		type:"GET",
		data:null
	});
	group.done(function(data, status, jqXHR){
		if(jqXHR.status == 200){
			var data_in = $.parseJSON(data);
			$("#team_select").children().remove();
			$("#team_select").append($("<option value='null'>Seleciona tu equipo</option>"));
			teams = [];
			for(teami in data_in){
				$("#team_select").append($("<option value='"+data_in[teami].name+"'>"+data_in[teami].name+"</option>"));
				teams.push(data_in[teami]);
			}

			paintTeam();
		}
	});
}
function actGroup(){
	var idliga = $("#league_select option:selected").attr("value");
	var group = $.ajax({
		url: "api/v1/group?idliga="+idliga,
		type:"GET",
		data:null
	});
	group.done(function(data, status, jqXHR){
		if(jqXHR.status == 200){
			var data_in = $.parseJSON(data);
			$("#group_select").children().remove();
			$("#group_select").append($("<option value='null'>Seleciona tu grupo</option>"));

			for(groupi in data_in){
				$("#group_select").append($("<option value='"+data_in[groupi].id+"'>"+data_in[groupi].name+"</option>"));
			}
		}
	});
}
function addEdges(){
	for(team in teams){
		coord = teams[team].location.split(";");
		var myLatlng = new google.maps.LatLng(coord[0],coord[1]);
		var flightPlanCoordinates = [
		                            myLatlng,geoCenterLatLng];
	    var edge =  new google.maps.Polyline({
	         path : flightPlanCoordinates,
	         strokeColor : "#FF0000",
	         strokeOpacity : 1.0,
	         strokeWeight : 2
	     });
	     edge.setMap(map);
	     geoEdges.push(edge);
	}
}
function setGeoCenter(name){
	clearEdges();
	for (team in teams){
		if(teams[team].name == name){
			 var coord = teams[team].location.split(";");
			 var myLatlng = new google.maps.LatLng(coord[0],coord[1]);
//			 var marker = new google.maps.Marker({
//			      position: myLatlng,
//			      map: map,
//			      title:"Centro"
//			  });
//			 markerArray.push(marker);
			 geoCenterLatLng = myLatlng;
		}
	}
}
function setSelects(){
	var request = $.ajax({
		url: "api/v1/league",
		type:"GET",
		data:null
	});
	request.done(function(data, status, jqXHR){
		if(jqXHR.status == 200){
			var data_in = $.parseJSON(data);
			for (option in data_in){
				$("#league_select").append($("<option value='"+data_in[option].id+"'>"+data_in[option].name+"</option>"));
			}
		}
	});
}
function clearEdges(){
	for (edge in geoEdges){
		geoEdges[edge].setMap(null);
	}
}
function clearMarket(){
	for (mark in markerArray){
		markerArray[mark].setMap(null);
	}
}