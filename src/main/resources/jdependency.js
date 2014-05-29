function callbacks(modules, packages) {

    var $cols = $('colgroup');

    $("td[id*='cell']").live('mouseover', function(e){
	
        var colIndex = $(this).prevAll('td').length;
		var rowIndex = this.parentNode.rowIndex;
		
		var relX = e.pageX - $(document).scrollLeft();
		var relY = e.pageY - $(document).scrollTop();
		
		if(colIndex < modules.length && rowIndex < modules.length){
			var text = "<table><thead><tr><td>";
			text+=modules[rowIndex];
			text+="</td></tr><tr><td>depends</td></tr><tr><td>";
			text+=modules[colIndex];
			text+="</td></tr><tr><td></td></tr></thead>";
			
			packages[rowIndex][colIndex].forEach(function(elem){
				text+=("<tr><td>"+elem+"</td></tr>");
			});
			
			text+="</table>";
		
			$("#dependenciesInfo").html(text);
			$("#dependenciesInfo").css( {position: "fixed", top: (relY +35)+"px", left: (relX +35)+"px"} );
		}
		
        $(this).parent().addClass('hover')
        $($cols[colIndex]).addClass('hover');
    
    }).live('mouseout', function(){
        var colIndex = $(this).prevAll('td').length;
        $(this).parent().removeClass('hover');
        $($cols[colIndex]).removeClass('hover');
		
		$("#dependenciesInfo").css({
			'left': -9999,
		});
    }).live('click', function(){
        $(this).toggleClass('back-red');
    })
    
    $('table').mouseleave(function(){
        $cols.removeClass('hover');
    })
};