function autoChat() {
    var jqxhr = $.ajax({
        url: ctx + '/live/autoMsg',
    });
    jqxhr.done(function (data) {
        if (data.code == '1000') {
            //popWinTips(3, data.message);
        	var rs = data.data;
        	sendAutoMsg(rs.content,rs.name,rs.level);
        }
    });
    jqxhr.fail(function (data) {
        //popWinTips(3, stringMsg.serverErr);
    });
}
$(function () {
	 if (userInfo.groupId == 3) {
		setInterval(autoChat,auto_chat_time);
	 }
});