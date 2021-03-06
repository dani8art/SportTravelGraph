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
			title:"Hacer 'click' para ver informaci�n",
			team: teams[team]
		});

		createDivTeam(teams[team]);
		
		google.maps.event.addListener(marker, 'click', returnDiv(marker));
		
		markerArray.push(marker);
	}	
}

function returnDiv(marker){
	return function(){
		var selector = marker.team.name.toLowerCase().replace(/\s/g,'').replace(/\./g,"");
		$("#"+selector).modal('toggle');
	};
}
function createDivTeam (team){
	var selector = team.name.toLowerCase().replace(/\s/g,'').replace(/\./g,"")
	var wrapper = $('<div id = "'+selector+'" class="modal fade in"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>');
	var dialog = $('<div class="modal-dialog" ></div>');
	var content = $('<div class="modal-content"></div>');
	var header = $('<div class="modal-header">'+
			'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">�</button>'+
			'<img class="img-escudo img-thumbnail img-responsive" src ="img/noDisponible.jpg" title="Escudo"/>'+
			'<h3 class="modal-title" id="myModalLabel" style="display:inline;">'+team.name+'</h3></div>');
	var body = $('<div class="modal-body">'+
					'<div class="container">'+
						'<div class="row">'+
							'<div class="col-md-4">'+
								'<div class="panel panel-primary">'+
									'<div class="panel-heading">'+
										'<h3 class="panel-title">Informaci�n del equipo</h3>'+
									'</div>'+
									'<div class="panel-body">'+
										'<p><strong>Ciudad: </strong>'+team.town+'</p>'+
										'<p><strong>D�a de juego: </strong>'+team.gameday+'</p>'+
										'<p><strong>Hora de juego: </strong>'+team.gamehour+'</p>'+
										'<p><strong>Coordenadas: </strong>'+team.location+'</p>'+
									'</div>'+
								'</div>'+
							'</div>'+
							'<div class="col-md-8">'+
								'<div class="panel panel-primary">'+
									'<div class="panel-heading">'+
										'<h3 class="panel-title">Imagenes</h3>'+
									'</div>'+
									'<div class="panel-body">'+
										'<div class="row">'+
											'<div class="col-md-4">'+
												'<p class="img-title"><strong>Titulo de la imagen</strong></p>'+
												'<img class="img-galeria img-thumbnail img-responsive" src ="img/noDisponible.jpg"/>'+
												'<p class="description">descripcion de la img</p>'+
											'</div>'+
											'<div class="col-md-4">'+
												'<p class="img-title"><strong>Titulo de la imagen</strong></p>'+
												'<img class="img-galeria img-thumbnail img-responsive" src ="img/noDisponible.jpg"/>'+
												'<p class="description">descripcion de la img</p>'+
											'</div>'+
											'<div class="col-md-4">'+
												'<p class="img-title"><strong>Titulo de la imagen</strong></p>'+
												'<img class="img-galeria img-thumbnail img-responsive" src ="img/noDisponible.jpg"/>'+
												'<p class="description">descripcion de la img</p>'+
											'</div>'+
										'</div>'+
									'</div>'+
								'</div>'+
							'</div>'+
						'</div>'+
					'</div>'+
				 '</div>');
	content.append(header);
	content.append(body);
	dialog.append(content);
	wrapper.append(dialog);
	$('body').append(wrapper);
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