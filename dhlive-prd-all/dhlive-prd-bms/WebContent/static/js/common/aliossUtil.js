function getAliossUploadData(fileName,type){
	var result={};
	var jqxhr = $.ajax({
		url: g_requestContextPath + '/alioss/getSignature',
		data: {"type": type,"fileName":fileName},
		async:false
	});
	jqxhr.done(function(data) {
		if (data.code != '1000') {
        	popLayer(data.message);
        }else{
        	var obj = data.data;
        	result.host = obj['host'];
        	result.policy = obj['policy'];
        	result.OSSAccessKeyId = obj['accessid'];
        	result.signature = obj['signature'];
        	result.fileId = obj['fileId'];
            result.callback = obj['callback'];
        	result.key = obj['dir']+'/'+result.fileId;
        	result.success_action_status = '200';
        };
  	});
	jqxhr.fail(function(data) {
		popLayer(stringMsg.serverErr);
  	});
	return result;
}

function getVideoUploadInfo(fileName,title){
	var result={};
	var jqxhr = $.ajax({
		url: g_requestContextPath + '/alioss/getUploadInfo',
		data: {"title": title,"fileName":fileName},
		async:false
	});
	jqxhr.done(function(data) {
		if (data.code != '1000') {
        	popLayer(data.message);
        }else{
        	var obj = data.data;
        	result.uploadAddress = obj['uploadAddress'];
        	result.uploadAuth = obj['uploadAuth'];
        	result.videoId = obj['videoId'];
        };
  	});
	jqxhr.fail(function(data) {
		popLayer(stringMsg.serverErr);
  	});
	return result;
}

function getStsInfo(fileName,type){
	var result={};
	var jqxhr = $.ajax({
		url: g_requestContextPath + '/alioss/getStsInfo',
		data: {"type": type,"fileName":fileName},
		async:false
	});
	jqxhr.done(function(data) {
		if (data.code != '1000') {
        	popLayer(data.message);
        }else{
        	var obj = data.data;
        	result.host = obj['host'];
        	result.accessid = obj['accessid'];
        	result.keySecret = obj['keySecret'];
        	result.securityToken = obj['securityToken'];
        	result.fileId = obj['fileId'];
        	result.key = obj['dir']+'/'+result.fileId;
        	result.expiration=obj['expiration'];
        	result.endPoint = obj['endPoint'];
        	result.bucketName = obj['bucketName'];
        	result.callback = obj['callback'];
        	//result.signature = obj['signature'];
        	result.userData = '{"Vod":{"Title":"this is title.我是标题","Description":"this is desc.我是描述","CateId":"19",\
                "Tags":"tag1,tag2,标签3","UserData":"user data"}}';
        };
  	});
	jqxhr.fail(function(data) {
		popLayer(stringMsg.serverErr);
  	});
	return result;
}
