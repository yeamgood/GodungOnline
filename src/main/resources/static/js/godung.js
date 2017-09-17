var stack_bottomright = {
"dir1": "up",
"dir2": "left",
"firstpos1": 25,
"firstpos2": 25
};


function openNotification(inputType, inputTitle, inputText) {
	var opts;
	if (inputType == "success" || inputType == "info" || inputType == "error") { /* SUCCESS,INFO,ERROR */
		opts = {
			delay: 3000,
			title : inputTitle,
			text : inputText,
			type : inputType,
			styling : 'bootstrap3',
			addclass: "stack-bottomright",
			stack : stack_bottomright
		};
	} else if (inputType == "notice") { /* NOTICE */
		opts = {
			delay: 3000,
			title : inputTitle,
			text : inputText,
			styling : 'bootstrap3',
			addclass: "stack-bottomright",
			stack : stack_bottomright
		};
	} else if (inputType == "dark") { /* DARK */
		opts = {
			delay: 3000,
			title : inputTitle,
			text : inputText,
			type : inputType,
			styling : 'bootstrap3',
			addclass: "stack-bottomright",
			addclass : 'dark',
			stack : stack_bottomright
		};
	} else {/* NOTICE */
		opts = {
			delay: 3000,
			title : inputTitle,
			text : inputText,
			styling : 'bootstrap3',
			addclass: "stack-bottomright",
			stack : stack_bottomright
		};
	}
	 new PNotify(opts);
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

function validationErrorShowWithPrefix(prefix,obj){
	$(obj).each(function(i, item){
		var messageOld = $("#" + prefix + item.field).html();
		var concat = "";
		if(messageOld != ""){concat = "<br/>";}
		
		$("#" + prefix + item.field).html(messageOld + concat + item.defaultMessage);
		$("#" + prefix + item.field).show();
	});
}

function resetValidationErrorWithPrefix(prefix){
	$("[id^='"+prefix+"']").html("");
	$("[id^='"+prefix+"']").hide();
}
/* End Validation */ 

/* Start Modal */ 
/* 
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
*/ 
/* Start Modal */ 