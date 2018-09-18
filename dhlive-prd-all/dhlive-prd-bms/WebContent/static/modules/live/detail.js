var getDataList;
$(function () {
    // 列表
    var fnData = function (obj, pageIndex, recordsNum, pages) {
        var htmls = "";
        console.log(obj);
        for (var i = 0; i < obj.length; i++) {
            var record = obj[i];
            htmls += [
                '<div class="tableContent tableCtHover mt5">',
                '<div class="flexWrap">',
                '<div class="flexWrap flexAgCen" style="width: 15%; text-align:center;  min-height: 70px;""> <div class="flexCon"> '
                + record.userNickName + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 15%; text-align:center; "> <div class="flexCon"> '
                + record.userNickName + '</div></div>',

                '<div class="flexWrap flexAgCen" style="width: 30%; text-align:center; "> <div class="flexCon"> '
                + record.userIntroduction + '</div></div>',

                '<div class="flexWrap flexAgCen"  style="width: 40%; text-align:center; "> <div class="flexCon">'
                + record.userSpecialty + '</div></div>',

                '</div>', '</div>'].join('');
        }
        return htmls;
    };


    // 列表数据
    getDataList = function () {
        $.ajaxGetData({
            "ajaxUrl": g_requestContextPath + "/live/getLiveRoomTeacherList",
            "fnData": fnData,
            "postData": {
                'roomId': $('#roomId').val()
            },
            "headtype": 1
        });
    };
    getDataList();
});
