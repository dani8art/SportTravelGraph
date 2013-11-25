$(document).ready(function(){
	downArrow();
	$("#arrow").on("click", function(){
		$(this).removeClass("visible-md");
		$(this).removeClass("visible-lg");
		$(this).css("display", "none");
		$(this).clearQueue();
	});
	$("#league_select").on("change",function(){
		$("#arrow").animate({left:"400px"},300);
	});
	$("#group_select").on("change",function(){
		$("#arrow").animate({left:"635px"},300);
	});
	$("#team_select").on("change",function(){
		$("#arrow").removeClass("visible-md");
		$("#arrow").removeClass("visible-lg");
		$("#arrow").css("display", "none");
		$("#arrow").clearQueue();
	});
});

function upArrow(selector, cantidad){
	$("#arrow").animate({top:"50px"},300,downArrow);
}
function downArrow(){
	$("#arrow").animate({top:"53px"},300,upArrow);
}