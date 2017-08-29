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