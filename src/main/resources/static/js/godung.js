function openNotification(inputType, inputTitle, inputText) {
	if (inputType == "success" || inputType == "info" || inputType == "error") { /* SUCCESS,INFO,ERROR */
		new PNotify({
			title : inputTitle,
			text : inputText,
			type : inputType,
			styling : 'bootstrap3'
		});
	} else if (inputType == "notice") { /* NOTICE */
		new PNotify({
			title : inputTitle,
			text : inputText,
			styling : 'bootstrap3'
		});
	} else if (inputType == "dark") { /* DARK */
		new PNotify({
			title : inputTitle,
			text : inputText,
			type : inputType,
			styling : 'bootstrap3',
			addclass : 'dark'
		});
	} else {
		new PNotify({ /* NOTICE */
			title : inputTitle,
			text : inputText,
			styling : 'bootstrap3'
		});
	}
}

/* Start Validation */ 
function validationErrorShow(obj){
	$(obj).each(function(i, item){
		var messageOld = $("#error_" + item.field).html();
		var concat = "";
		if(messageOld != ""){concat = "<br/>";}
		
		$("#error_" + item.field).html(messageOld + concat + item.defaultMessage);
		$("#error_" + item.field).show();
	});
}

function resetValidationError(){
	$("[id^='error_']").html("");
	$("[id^='error_']").hide();
}
/* End Validation */ 