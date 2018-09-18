var getJsonParam = function($obj) {
	var result = {};
	$obj.find("input,select,textarea").each(function(index, data) {
		var $data = $(data);
		if ($data.attr("name") && $data.val().trim() != "") {
			result[$data.attr("name")] = $data.val().trim();
		}
	});
	return result;
};

var showBottomMessage = function(data) {
	result = eval(data);
	if (result.code == '1000') {
		$.messager.show({
			title : '系统消息',
			msg : '操作完成',
			timeout : 5000,
			showType : 'slide'
		});
	} else {
		$.messager.show({
			title : '系统消息',
			msg : '操作失败',
			timeout : 5000,
			showType : 'slide'
		});
	}
};
