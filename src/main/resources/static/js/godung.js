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

/* Start Modal */ 
$(document).ready(function() {
 	$('.modal').on('show.bs.modal', function() {
        var nModals = $('.modal.in').length;
        $(this).attr('data-nmodals', nModals+1);
        backdropStack(nModals);
    });
    function backdropStack(nModals) {
        setTimeout(function() {
            $('.modal-backdrop').each(function() {
                if(!$(this).attr('data-nmodals')) {
                    $(this).attr('data-nmodals', nModals+1);
                }
            });
            modalStack();
        }, 100);
    };
    function modalStack() {
        $('.modal').each(function() {
            $n = $(this).data('nmodals');
            $z = $(this).css('z-index');
            $(this).css('z-index', $n*$z);
        });
        $('.modal-backdrop').each(function() {
            $n = $(this).data('nmodals');
            $z = $(this).css('z-index');
            $(this).css('z-index', $n*$z);
        });
    }
});
/* Start Modal */ 